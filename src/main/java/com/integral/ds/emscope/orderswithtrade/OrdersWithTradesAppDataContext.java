package com.integral.ds.emscope.orderswithtrade;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hazelcast.core.MapEntry;
import com.integral.ds.dao.TradeMasterDao;
import com.integral.ds.dao.auxiliary.AuxFields;
import com.integral.ds.dao.auxiliary.AuxFieldsCache;
import com.integral.ds.dao.auxiliary.AuxFieldsHelper;
import com.integral.ds.dto.*;
import com.integral.ds.emscope.orders.narrative.NarrativeGenerator;
import com.integral.ds.emscope.orders.narrative.NarrativeGeneratorFactory;
import com.integral.ds.emscope.portfolio.PortfolioService;
import com.integral.ds.util.AuditTrailParsingUtil;
import com.integral.ds.util.PropertyReader;
import com.integral.ds.util.RFSEventParser;
import com.integral.ds.util.demo.DemoUtility;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.ds.dao.OrdersWithTradesDAO;
import com.integral.ds.emscope.trades.TradeGrouping;
import com.integral.ds.emscope.trades.meta.TradeProviderStreamInfoService;
import com.integral.ds.emscope.trades.weights.TradeWeightService;
import com.integral.ds.model.data.DataQuery;
import com.integral.ds.representation.RepresentationModel;
import com.integral.ds.util.DataQueryUtils;
import com.owlike.genson.Genson;


public class OrdersWithTradesAppDataContext{

    private static final Log log = LogFactory.getLog( OrdersWithTradesAppDataContext.class );

    private OrdersWithTradesDAO ordersWithTradesDAO;
    private TradeWeightService tradeWeightService;
    private TradeProviderStreamInfoService tradeProviderStreamInfoService;
    private TradeMasterDao tradeMasterDao;
    private AuxFieldsCache auxFieldsCache;
    private PortfolioService portfolioService;

    final static int STATUS_COUNTER = 0;
    final static int DEALTAMOUNT_COUNTER = 1;
    final static int NAME_COUNTER = 2;
    final static int TIER_COUNTER = 3;
    final static int RATE_COUNTER = 4;
    final static int SPOTRATE_COUNTER = 5;
    final static int FWDPOINTS_COUNTER = 6;
    final static int BIDOFFERMODE_COUNTER = 7;
    final static int VALUEDATE_COUNTER = 8;
    final static int PROVIDER_COUNTER = 9;
    final static int TIMESTAMP_COUNTER = 10;
    final static int SORTORDER_COUNTER = 11;
    final static char TILDA = '~';
    final static char PIPE = '|';
    final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd" );
    final static SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
    final static Gson GSON_SERIALIZER = new Gson();

    public void setOrdersWithTradesDAO( OrdersWithTradesDAO ordersWithTradesDAO ) {
        this.ordersWithTradesDAO = ordersWithTradesDAO;
    }

    @SuppressWarnings( {"rawtypes", "unchecked"} )
    public Map getOrderDetailsForOrderID(String entityId ) {
        Map returnMap = new HashMap<>();
        Order order = ordersWithTradesDAO.getOrderByOrderID(entityId);

        if(order == null) {
            List<String> orderIds = tradeMasterDao.getAllOrderIdsForTrade(entityId);
            orderIds = DataQueryUtils.getFilteredOrderIds(orderIds);

            if(orderIds.size() > 1) {
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("SHOW_ORDERS", orderIds);
                return result;
            } else {
                if(!orderIds.isEmpty()) {
                    entityId = orderIds.get(0);
                    order = ordersWithTradesDAO.getOrderByOrderID(entityId);
                } else {
                    return getTradeDetailsMap(entityId);
                }
            }
        }
        WorkflowTypeEnum orderType = getNormalizedOrderType(order);
        String eventTime = order.getEventTime();
        List<Trade> tradeList = ordersWithTradesDAO.getTradesByOrderIDAndMonth(entityId,orderType);
        fixStatusFieldForTrades(tradeList);
        AuxFieldsHelper.fillGap(auxFieldsCache,tradeList);
        List<String> tradeIds = DataQueryUtils.getTradeIdList(tradeList);
        DataQuery orderFormatter = new DataQuery();
        DataQuery tradeFormatter = new DataQuery();
        List<Order> orderList = new ArrayList<>();
        orderList.add( order );
        double coveredOrder =  ordersWithTradesDAO.getCoverOrderFillRate( entityId );
        order.setCoverOrderFillRate(coveredOrder  );

        Map<String,Collection<TradeStreamDetails>> tradeStreamDetails = getTradeStreamDetails(tradeIds);
        Collection formattedOrders = orderFormatter.apply(orderList, RepresentationModel.COMPLETE, false);
        postProcessOrderDetails(formattedOrders);
        Collection formattedTrades = tradeFormatter.apply( tradeList, RepresentationModel.SUMMARY, false );
        postProcessTrades(formattedTrades);

        String narrative = getNarrative(order,tradeMasterDao,orderType);
        List<String> rateEvents = ordersWithTradesDAO.getTradeEventsForOrder( entityId );
        Map<String, TradeGrouping> executionSummary = generateExecutionSummary( formattedTrades, order.isTermCcy(),order);
        Map<String,TradeProbability> tradeProbabilityAvailability = getTradeProbabilityMap(tradeIds,tradeList);
        returnMap.put("ORDERDETAILS", formattedOrders );
        returnMap.put("TRADEDETAILS", formattedTrades );
        returnMap.put("ORDERNARRATIVE", narrative );
        returnMap.put("EXECUTIONSUMMARY", executionSummary );
        returnMap.put("TRADERATEEVENTS",rateEvents);
        returnMap.put("TRADEPROBABILITYAVAILABILITY",tradeProbabilityAvailability);
        returnMap.put("TRADESTREAMDETAILS",tradeStreamDetails);
        returnMap.put("ORDERTYPE",orderType);
        returnMap.put("EVENT_LOGS",getEventLogs(eventTime));
        return returnMap;
    }

    private void fixStatusFieldForTrades(List<Trade> trades) {
        for(Trade trade : trades) {
            Integer failed = trade.getFailed();
            if(failed != null && failed == 1) {
                trade.setSTATUS("F");
                trade.setTRADESTATUS("F");
            }
        }
    }

    private void postProcessTrades(Collection formattedTrades) {
        if(DemoUtility.isDemoMode()) {
            if (!formattedTrades.isEmpty()) {
                List<Map> trades = ((List<Map>) formattedTrades);
                for (Map trade : trades) {
                    String provider = (String) trade.get("RealLp");
                    String cpty = (String) trade.get("CPTY");
                    if(provider == null) provider = "****";
                    String stream = (String) trade.get("STREAM");
                    ProviderStream providerStream = new ProviderStream(provider,stream);
                    ProviderStream maskedProviderStream = DemoUtility.getMaskedProviderStream(providerStream);
                    String maskedCpty = DemoUtility.getMaskedProvider(cpty);
                    trade.put("CPTY", maskedCpty);
                    trade.put("MaskLP", maskedProviderStream.getProvider());
                    trade.put("RealLp", maskedProviderStream.getProvider());
                    trade.put("MakerOrg", maskedProviderStream.getProvider());
                    trade.put("ORG", "****");
                    trade.put("TickStream", maskedProviderStream.getStream());
                    trade.put("STREAM", maskedProviderStream.getStream());
                }
            }
        }
    }

    private void postProcessOrderDetails(Collection formattedOrders) {
        if(DemoUtility.isDemoMode()) {
            if (!formattedOrders.isEmpty()) {
                Map<String, Object> orderDetails = ((List<Map>) formattedOrders).get(0);
                orderDetails.put("Dealer", "****");
                orderDetails.put("Org", "****");
                orderDetails.put("Order_stream", "****");
            }
        }
    }

    private String getNarrative(Order order,TradeMasterDao tradeMasterDao,WorkflowTypeEnum type) {
        List<TradeInfo> tradeInfos = Collections.EMPTY_LIST;
        if(type == WorkflowTypeEnum.RFS) {
            List<TradeInfo> result = tradeMasterDao.getTradesForOrder(order.getOrderID());
            if(result.isEmpty()) {
                type = WorkflowTypeEnum.ESP;
            } else {
                tradeInfos = result;
            }
        }else {
            List<TradeInfo> result = tradeMasterDao.getTradesForOrder(order.getOrderID());
            if(result.isEmpty()) {
                type = WorkflowTypeEnum.ESP;
            } else {
                tradeInfos = result;
            }
        }
        populateMissingInTrade(tradeInfos);
        return getNarrative(order, tradeInfos, type);
    }

    private void populateMissingInTrade(List<TradeInfo> tradeInfos) {
        if(tradeInfos != null && !tradeInfos.isEmpty()) {
            List<String> ids = new ArrayList<String>();
            for (TradeInfo info : tradeInfos) {
                ids.add(info.getTradeId());
            }
            Map<String, AuxFields> fields = auxFieldsCache.getAuxFieldsForTrades(ids);
            for (TradeInfo info : tradeInfos) {
                AuxFields field = fields.get(info.getTradeId());
                if (field != null) {
                    info.setCcyPair(field.getCcyPair());
                    info.setDealt(field.getDealt());
                }
            }
        }
    }

    private String getNarrative(Order order , List<TradeInfo> tradeInfo , WorkflowTypeEnum type) {
        if(DemoUtility.isDemoMode()) {
            try {
                order = order.clone();
                order.setOrg("XXXX");
                order.setDealer("XXXX");
                order.setBrokerForCptyA("XXXX");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        NarrativeGenerator narrativeGenerator = NarrativeGeneratorFactory.getNarrativeGenerator(type);
        return narrativeGenerator.generateNarrativeForOrder(order, tradeInfo);
    }

    private Map<String,Map<String,String>> getEventLogs(String eventTime) {
        return RFSEventParser.parserRFSEvents(eventTime);
    }

    private WorkflowTypeEnum getNormalizedOrderType(Order order) {
        return "RFQ".equalsIgnoreCase(order.getOrderType()) ? WorkflowTypeEnum.RFS : WorkflowTypeEnum.ESP;
    }

    private Map getTradeDetailsMap(String tradeId) {
        Map returnMap = new HashMap();
        List<Trade> tradeList = ordersWithTradesDAO.getTradesByTradeId(tradeId, null);
        if(!tradeList.isEmpty()) {
            fixStatusFieldForTrades(tradeList);
            AuxFieldsHelper.fillGap(auxFieldsCache, tradeList);
            DataQuery tradeFormatter = new DataQuery();
            Collection formattedTrades = tradeFormatter.apply(tradeList, RepresentationModel.SUMMARY, false);
            TradeInfo tradeInfo = tradeMasterDao.getTrade(tradeId);
            returnMap.put("TRADEDETAILS", formattedTrades);
            returnMap.put("ORDERNARRATIVE", getNarrative(null, Arrays.asList(tradeInfo), WorkflowTypeEnum.ONE_TRADE_NO_ORDER));
            return returnMap;
        } else {
            Map<String,List<Map<String,Object>>> portfolioDetails = portfolioService.getPortfolioDetails(tradeId);
            if ((portfolioDetails.get("PORTFOLIO_TCA_SUMMARY") == null) &&
                    (portfolioDetails.get("PORTFOLIO_SPOT_EXECUTION_TCA_SUMMARY") == null) &&
                    (portfolioDetails.get("PORTFOLIO_SPOT_EXECUTION_GOTO_MARKET_TCA_SUMMARY") == null)) {
                try {
                    String endpoint = PropertyReader.getPropertyValue(PropertyReader.KEY_PORTFOLIO_TCA_URL);
                    if(!endpoint.endsWith("/")) {
                        endpoint += "/";
                    }
                    endpoint += tradeId;
                    URL url = new URL(endpoint);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    int respCode = urlConnection.getResponseCode();

                    if(respCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = null;
                        StringBuilder response = new StringBuilder();
                        try {
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(urlConnection.getInputStream()));
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                        } catch (Exception e) {
                            log.error("Exception while reading input stream.",e);
                        } finally {
                            IOUtils.closeQuietly(inputStream);
                        }
                        if (response.length() > 0) {
                            Map<String,Object> retMap = GSON_SERIALIZER.fromJson(response.toString(),
                                    new TypeToken<Map<String,Object>>() {}.getType());

                            Map parsedResult = new HashMap();
                            for(Map.Entry<String,Object> entry : retMap.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                List<Map<String,Object>> valueList = GSON_SERIALIZER.fromJson((String)value,
                                        new TypeToken<List<Map<String,Object>>>() {}.getType());
                                parsedResult.put(key, valueList);
                            }
                            portfolioDetails.putAll(parsedResult);
                        }
                    }
                } catch(Exception ex) {
                    log.error("Exception occurred while retrieving Portfolio TCA details.", ex);
                }
            }
            return portfolioDetails;
        }
    }

    private Map<String,Collection<TradeStreamDetails>> getTradeStreamDetails(List<String> tradeIds) {
        Map<String,TradeStreamDetails> tradeStreamDetailsMap = tradeProviderStreamInfoService.getStreamDetailsForTrades(tradeIds);
        Multimap<String,TradeStreamDetails> result = HashMultimap.create();
        Iterator<TradeStreamDetails> iterator = tradeStreamDetailsMap.values().iterator();
        while(iterator.hasNext()) {
            TradeStreamDetails streamDetails = iterator.next();
            String provider = streamDetails.getProvider();
            String stream = streamDetails.getStream();
            String key = provider + "-" + stream;
            result.put(key,streamDetails);
        }
        return result.asMap();
    }

    private Map<String,TradeProbability> getTradeProbabilityMap(List<String> tradeIds, List<Trade> tradeList) {
        Map<String,String> tradeProbabilityAvailability = tradeWeightService.getAvailabilityMapForTrades(tradeIds);
        Map<String,TradeProbability> result = new HashMap<String,TradeProbability>();

        for(Trade currentTrade : tradeList) {
            String status = currentTrade.getTRADESTATUS();
            String tradeId = currentTrade.getTRADEID();
            String probability = tradeProbabilityAvailability.get(tradeId);
            TradeProbability tradeProbability = new TradeProbability();
            tradeProbability.setProbability(probability).setTradeStatus(status);
            boolean probabilityExists = "-1".equalsIgnoreCase(probability) ? false : true;
            tradeProbability.setProbabilityExists(probabilityExists);
            result.put(tradeId,tradeProbability);
        }
        return result;
    }

    public String getExtendedOrderDetailsForOrderID( String userGroup, String orderID ) {
        return ordersWithTradesDAO.getExtendedOrderDetailsForOrderID(orderID);
    }

    public OrderLandscapeDetails getOrderSummaryWithTrades( String userGroup, String orgName, String ccyPair, String orderType, long minAmount, Date fromTime, Date toTime )
    {
        String newccyPair = ccyPair.substring( 0, 3 ) + "/" + ccyPair.substring( 3 );
        List<OrderSummary> orderSummList = ordersWithTradesDAO.getOrderSummaryForOrg( userGroup, orgName, newccyPair, orderType, minAmount, fromTime, toTime );
        List<TradeSummary> tradeSummaryList = ordersWithTradesDAO.getTradesSummaryForOrg( userGroup, orgName, newccyPair, orderType, minAmount, fromTime, toTime );

        Set<String> orderIDs = new HashSet<>();

        for ( OrderSummary orderSummary : orderSummList ) {
            orderIDs.add( orderSummary.getOrderID() );
        }

        Iterator<TradeSummary> tradeIterator = tradeSummaryList.iterator();
        while ( tradeIterator.hasNext() ) {
            if ( !( orderIDs.contains( tradeIterator.next().getORDERID() ) ) ) {
                tradeIterator.remove();
            }
        }

        OrderLandscapeDetails orderLandscapeDetails = new OrderLandscapeDetails();
        orderLandscapeDetails.setOrderSummary( orderSummList );
        orderLandscapeDetails.setTradeSummary( tradeSummaryList );
        return orderLandscapeDetails;
    }

    public EMScopeHolder getOrderAndTradeDetailsForOrg(String orgName, String ccyPair, String orderType, long minAmount, Date fromTime, Date toTime )
    {
        String newccyPair = ccyPair.substring( 0, 3 ) + "/" + ccyPair.substring( 3 );
        return ordersWithTradesDAO.getOrderAndTradeDetailsForOrg(orgName, newccyPair, orderType, minAmount, fromTime, toTime);
    }

    public OrderLandscapeDetails getTradesSummaryWithOrders( String orgName, String ccyPair, String orderType, long minAmount, Date fromTime, Date toTime )
    {
        String newccyPair = ccyPair.substring( 0, 3 ) + "/" + ccyPair.substring( 3 );

        List<TradeSummary> tradeSummaryList = ordersWithTradesDAO.getTradesInRange(orgName, newccyPair, orderType, minAmount, fromTime, toTime);
        List<String> orderIds = getOrderIdList(tradeSummaryList);

        log.info( "Order ids from trades " + orderIds );
        List<OrderSummary> orderSummList = ordersWithTradesDAO.getOrdersForTrades("", orderIds);
        log.info( "Size of orders summary list " + orderSummList.size() );

        OrderLandscapeDetails orderLandscapeDetails = new OrderLandscapeDetails();
        orderLandscapeDetails.setOrderSummary( orderSummList );
        orderLandscapeDetails.setTradeSummary( tradeSummaryList );
        return orderLandscapeDetails;
    }

    public EMScopeHolder getTradesAndOrdersDetailsForOrg(String orgName, String ccyPair, String orderType, long minAmount, Date fromTime, Date toTime )
    {
        String newccyPair = ccyPair.substring( 0, 3 ) + "/" + ccyPair.substring( 3 );
        return ordersWithTradesDAO.getTradeAndOrdersDetailsForOrg(orgName, newccyPair, orderType, minAmount, fromTime, toTime);
    }

    @SuppressWarnings( {"rawtypes", "unchecked"} )
    private Map<String, TradeGrouping> generateExecutionSummary(Collection formattedTrades, boolean isTermCcy, Order order) {
        List<Map> formattedTradeList = ( List<Map> ) formattedTrades;
        Map<String, TradeGrouping> amountByCptyMap = new HashMap<>();

        for ( Map map : formattedTradeList ) {
            String org = ( String ) map.get( "CPTY" );
            TradeGrouping cptySumary = amountByCptyMap.get( org );
            if ( cptySumary == null )
            {
                cptySumary = new TradeGrouping();
                cptySumary.setProviderName( org );
                amountByCptyMap.put( org, cptySumary );
            }
            cptySumary.processTrade( map, isTermCcy );
        }
        for (String org : amountByCptyMap.keySet()) {
			TradeGrouping tradeGrouping = amountByCptyMap.get(org);
			tradeGrouping.computeMatchingAmount();
            tradeGrouping.computeAverageResponseTime();
            tradeGrouping.calculatePriceImprovement(order);
		}
        return amountByCptyMap;
    }

    private List<String> getOrderIdList( List<TradeSummary> tradeSummaryList ) {
        List<String> orderIds = new ArrayList<String>();
        for ( TradeSummary tradeSummary : tradeSummaryList ) {
            orderIds.add(tradeSummary.getORDERID());
        }
        return orderIds;
    }

    public Map<String, List<Map<String, Object>>> getCoveredTrades( String tradeID )
    {
        Map<String, List<Map<String, Object>>> returnMap = new HashMap<>();
        List<Trade> coveredTrades = ordersWithTradesDAO.getCoveredTrades(tradeID);
        List<Trade> coveringTrades = ordersWithTradesDAO.getCoveringTrades(tradeID);
        DataQuery dq = new DataQuery();
        returnMap.put("COVEREDTRADES", (List<Map<String, Object>>) dq.apply(coveredTrades, RepresentationModel.SUMMARY, false));
        returnMap.put("COVERINGTRADES", (List<Map<String, Object>>) dq.apply(coveringTrades, RepresentationModel.SUMMARY, false));
        return returnMap;
    }

    public Map<String, List<Map<String, Object>>> getCoveredTradesForOrder( String tradeID , String orderId) {
        Map<String, List<Map<String, Object>>> returnMap = new HashMap<>();
        List<Trade> coveredTrades = ordersWithTradesDAO.getCoveredTradesWithOrderId( tradeID , orderId);
        List<Trade> coveringTrades = ordersWithTradesDAO.getCoveringTradesForOrders( tradeID ,orderId);

        AuxFieldsHelper.fillGap(auxFieldsCache,coveredTrades);
        AuxFieldsHelper.fillGap(auxFieldsCache,coveringTrades);

        DataQuery dq = new DataQuery();
        returnMap.put( "COVEREDTRADES", ( List<Map<String, Object>> ) dq.apply( coveredTrades, RepresentationModel.SUMMARY, false ) );
        returnMap.put( "COVERINGTRADES", ( List<Map<String, Object>> ) dq.apply( coveringTrades, RepresentationModel.SUMMARY, false ) );
        return returnMap;
    }

    public String getCoveredOrders( String orderID )
    {
        return ordersWithTradesDAO.getCoveredOrders(orderID);
    }

    public String getOriginatingOrder( String orderID )
    {
        return ordersWithTradesDAO.getOriginatingOrder(orderID);
    }

    public String getCoveringOrders( String orderID )
    {
        return ordersWithTradesDAO.getCoveringOrders(orderID);
    }

    public String getMarketSnapShotForTrade(String tradeID) {
        String rawMktData = ordersWithTradesDAO.getMarketSnapShot(tradeID) ;
        if(StringUtils.isBlank(rawMktData)) {
            Optional<String> marketSnapshot = AuxFieldsHelper.getSnapshot(auxFieldsCache,tradeID);
            if(marketSnapshot.isPresent()) rawMktData = marketSnapshot.get();
        }
        String retString = " ";
        if (StringUtils.isNotBlank(rawMktData)) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add( "TradeID", tradeID );
            populateMktSnapShotForTrade(rawMktData, objectBuilder);
            StringWriter stWriter = new StringWriter();
            JsonWriter jsonWriter = Json.createWriter(stWriter);
            jsonWriter.writeObject(objectBuilder.build());
            jsonWriter.close();
            retString= stWriter.toString();
            log.info(" Market snapshot for Trade ID : "+retString);
        }
        return retString;
    }

    public String getMarketSnapShotForTrade(String orderId,String tradeID) {
        String rawMktData = ordersWithTradesDAO.getMarketSnapShot(orderId,tradeID) ;
        if(StringUtils.isBlank(rawMktData)) {
            Optional<AuxFields> field = auxFieldsCache.getAuxFieldFor(orderId,tradeID);
            if(field.isPresent()) {
                rawMktData = field.get().getSnapshot();
            }
        }
        String retString = " ";
        if (StringUtils.isNotBlank(rawMktData)) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add( "TradeID", tradeID );
            populateMktSnapShotForTrade(rawMktData, objectBuilder);
            StringWriter stWriter = new StringWriter();
            JsonWriter jsonWriter = Json.createWriter(stWriter);
            jsonWriter.writeObject(objectBuilder.build());
            jsonWriter.close();
            retString= stWriter.toString();
            log.info(" Market snapshot for Trade ID : "+retString);
        }
        return retString;
    }

    public String getRateEventsForTrade(String tradeID) {
        String rawEventData = ordersWithTradesDAO.getRateEvents(tradeID) ;
        if(StringUtils.isBlank(rawEventData)) {
            Optional<String> events = AuxFieldsHelper.getRateLog(auxFieldsCache,tradeID);
            if(events.isPresent()) rawEventData = events.get();
        }
        String retString = " ";
        if (StringUtils.isNotBlank(rawEventData)) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("TradeID", tradeID);
            AuditTrailParsingUtil.createGeneratedEvents(rawEventData, objectBuilder);
            StringWriter stWriter = new StringWriter();
            JsonWriter jsonWriter = Json.createWriter(stWriter);
            jsonWriter.writeObject(objectBuilder.build());
            jsonWriter.close();
            retString= stWriter.toString();
            log.info(" Rate Events for Trade ID : "+retString);
        }
        retString = postProcessRateEvents(retString);
        return retString;
    }

    public String getRateEventsForTrade(String orderId,String tradeID) {
        String rawEventData = ordersWithTradesDAO.getRateEvents(orderId,tradeID) ;
        if(StringUtils.isBlank(rawEventData)) {
            Optional<AuxFields> fields = auxFieldsCache.getAuxFieldFor(orderId,tradeID);
            if(fields.isPresent()) {
                rawEventData = fields.get().getRateLog();
            }
        }
        String retString = " ";
        if (StringUtils.isNotBlank(rawEventData)) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder().add("TradeID", tradeID);
            AuditTrailParsingUtil.createGeneratedEvents(rawEventData, objectBuilder);
            StringWriter stWriter = new StringWriter();
            JsonWriter jsonWriter = Json.createWriter(stWriter);
            jsonWriter.writeObject(objectBuilder.build());
            jsonWriter.close();
            retString= stWriter.toString();
            log.info(" Rate Events for Trade ID : "+retString);
        }
        retString = postProcessRateEvents(retString);
        return retString;
    }

    private String postProcessRateEvents(String retString) {
        if(DemoUtility.isDemoMode() && StringUtils.isNotBlank(retString)) {
            Map deSer = (Map) DemoUtility.deSerialize(retString);
            List attributes = (List) deSer.get("RateEvents");
            for(Object attr : attributes) {
                Map temp = (Map) attr;
                temp.put("QuoteGuid","*****");
            }
            return DemoUtility.serialize(deSer);
        }
        return retString;
    }

    public String getTradeTypeForOrder(String orderID) {
        return ordersWithTradesDAO.getTradeTypeForOrder(orderID);
    }

    private void populateMktSnapShotForTrade(String record, JsonObjectBuilder objectBuilder)
    {
        StringBuilder builder = new StringBuilder();
        int counter = STATUS_COUNTER;

        char[] chars = record.toCharArray();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        JsonObjectBuilder snapBuilder = Json.createObjectBuilder();
        for ( char ch : chars )
        {
            switch ( ch )
            {
                case TILDA:
                    String str = builder.toString();
                    switch ( counter )
                    {
                        case STATUS_COUNTER:
                            char [] status =  {builder.charAt( 0 )};
                            snapBuilder.add( "status", new String(status) );
                            break;
                        case DEALTAMOUNT_COUNTER:
                            snapBuilder.add( "dealtAmount", Double.parseDouble( str ) );
                            break;
                        case NAME_COUNTER:
                            snapBuilder.add( "name", str );
                            break;
                        case TIER_COUNTER:
                            snapBuilder.add( "tier", Integer.parseInt( str ) );
                            break;
                        case RATE_COUNTER:
                            snapBuilder.add( "rate", Double.parseDouble( str ) );
                            break;
                        case SPOTRATE_COUNTER:
                            snapBuilder.add( "spotRate", Double.parseDouble( str ) );
                            break;
                        case FWDPOINTS_COUNTER:
                            snapBuilder.add( "fwdPoints", Double.parseDouble( str ) );
                            break;
                        case BIDOFFERMODE_COUNTER:
                            snapBuilder.add( "bidOfferMode", Integer.parseInt( str ) == 0 ? "BID" : "OFFER" );
                            break;
                        case VALUEDATE_COUNTER:
                            snapBuilder.add( "valueDate", DATE_FORMAT.format( new java.sql.Date( Long.parseLong( str ) ) ) );
                            break;
                        case PROVIDER_COUNTER:
                            snapBuilder.add( "provider", str );
                            break;
                        case TIMESTAMP_COUNTER:
                            if ( !"-1".equals( str ) )
                            {
                                snapBuilder.add( "timestamp", TIMESTAMP_FORMAT.format( new Timestamp( Long.parseLong( str ) ) ) );
                            }
                            break;
                        case SORTORDER_COUNTER:
                            snapBuilder.add( "sortOrder", Integer.parseInt( str ) );
                            break;
                    }
                    counter++;
                    builder = new StringBuilder();
                    break;
                case PIPE:
                    counter = STATUS_COUNTER;
                    arrayBuilder.add( snapBuilder );
                    snapBuilder = Json.createObjectBuilder();
                    break;
                default:
                    builder.append( ch );
            }
        }
        objectBuilder.add( "MarketSnapShot", arrayBuilder );
    }

    private void populateMktSnapshotForOrder(String snapshot, JsonObjectBuilder objectBuilder) {
        String[] levels = snapshot.split("\\|");

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        JsonObjectBuilder snapBuilder = Json.createObjectBuilder();

        for (String level : levels) {
            String[] snapshotRow = level.split(";", -1);
            try {
                String time     = snapshotRow[0];

                snapBuilder.add("timestamp", time);

                String provider = snapshotRow[1];
                snapBuilder.add("provider", provider);

                String size     = snapshotRow[2];
                snapBuilder.add("dealtAmount", size);

                String tier     = snapshotRow[3];
                snapBuilder.add("tier", tier);

                String price    = snapshotRow[4];
                snapBuilder.add("rate", price);

                snapBuilder.add( "bidOfferMode", "BID");
                arrayBuilder.add( snapBuilder );
                snapBuilder = Json.createObjectBuilder();
            } catch (Exception e) {
                //e.printStackTrace();
            }

            // Ask Side
            try {
                String time     = snapshotRow[9];
                snapBuilder.add("timestamp", time);

                String provider = snapshotRow[8];
                snapBuilder.add("provider", provider);

                String size     = snapshotRow[7];
                snapBuilder.add("dealtAmount", size);

                String tier     = snapshotRow[6];
                snapBuilder.add("tier", tier);

                String price    = snapshotRow[5];
                snapBuilder.add("rate", price);

                snapBuilder.add( "bidOfferMode", "OFFER");

                arrayBuilder.add( snapBuilder );
                snapBuilder = Json.createObjectBuilder();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        objectBuilder.add( "MarketSnapShot", arrayBuilder );
    }


    private  void populateMktSnapShotForOrder( String record, String date, JsonObjectBuilder objectBuilder )
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        JsonObjectBuilder snapBuilder = Json.createObjectBuilder();
        //14:24:59:910;JPM1;2000000.0;1;1.36444;1.36444;1;500000.0;DB3;14:24:59:926|14:24:59:040;MSFX;500000.0;2;1.36443;1.36445;2;1500000.0;DB3;14:24:59:926|
        // Timestamp					Provider	Bid Size		Tier	Bid Rate	Offer Rate	Tier	Offer Size	Provider	Timestamp
        // 2014/07/09 20:49:32 710 GMT	SUCD		1,000,000.00	1	7.8361	7.8389	1	996,000.00	SUCD	2014/07/09 20:49:32 710 GMT
        String [] rows = record.split("\\|");

        for (String row:rows)
        {

            String [] cols = row.split( ";" );
            if (cols.length >=5 )
            {
                snapBuilder.add( "BidTimestamp", cols[0].trim().length() > 0 ?date+" " +cols[0]:"" );
                snapBuilder.add( "BidProvider", cols[1] );
                snapBuilder.add( "BidSize", cols[2] );
                snapBuilder.add( "BidTier", cols[3] );
                snapBuilder.add( "BidRate", cols[4] );
                if ((cols.length >=6 )  && (cols.length <8))
                {
                    continue;
                }
                if ( cols.length >= 10 )
                {
                    snapBuilder.add( "OfferRate", cols[5] );
                    snapBuilder.add( "OfferTier", cols[6] );
                    snapBuilder.add( "OfferSize", cols[7] );
                    snapBuilder.add( "OfferProvider", cols[8] );
                    snapBuilder.add( "OfferTimestamp", date+" " +cols[9] );
                }
                else
                {
                    snapBuilder.add( "OfferRate", "" );
                    snapBuilder.add( "OfferTier", "" );
                    snapBuilder.add( "OfferSize", "" );
                    snapBuilder.add( "OfferProvider", "" );
                    snapBuilder.add( "OfferTimestamp", "" );
                }

                arrayBuilder.add( snapBuilder );
                snapBuilder = Json.createObjectBuilder();
            }
        }
        objectBuilder.add( "MarketSnapShot", arrayBuilder );
    }

    public String getMarketSnapShotForOrder( String orderID ) {
        String rawMktDataWithDate = ordersWithTradesDAO.getMarketSnapShotForOrder(orderID) ;

        String retString = " ";
        if (StringUtils.isNotBlank(rawMktDataWithDate))
        {
            // parse the data only if it is not empty

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                    .add( "orderID", orderID );
            String [] data = rawMktDataWithDate.split( "~" );

            String rawMktData=data[1];
            if ("null".equalsIgnoreCase(rawMktData)) {
                return StringUtils.SPACE;
            }
            populateMktSnapShotForOrder(rawMktData,data[0],objectBuilder);
            StringWriter stWriter = new StringWriter();
            JsonWriter jsonWriter = Json.createWriter(stWriter);
            jsonWriter.writeObject(objectBuilder.build());
            jsonWriter.close();
            retString= stWriter.toString();
            log.info(" Market snapshot for orderID : "+retString);
        }
        return retString;
    }

    public String generateOrderCollectionQuery(String takerorg, String ccypair,long from, long to,String orderType,
                                String status, String channel, String tif, String origorg , String buysell,int offset)
    {
        String subChannel = channel.equals( "none" ) ? channel : channel.replace( '-','/' ) ;

        String[] ccyList = ccypair.split( "," );
        String finalCcys="";
        if(!ccyList[0].equals( "none" ))
        {
            for ( int i = 0; i < ccyList.length; i++ )
            {
                finalCcys = finalCcys + ccyList[i].substring( 0, 3 ) + "/" + ccyList[i].substring( 3 );
                if ( i != ( ccyList.length - 1 ) )
                {
                    finalCcys = finalCcys + ",";
                }
            }
        }
        else
        {
            finalCcys="none";
        }

        String[] subchannelList = subChannel.split( "," );
        String finalchannel="";
        if(!subchannelList[0].equals( "none" ))
        {
            for ( int i = 0; i < subchannelList.length; i++ )
            {
                finalchannel = finalchannel + subchannelList[i].replace( '-','/' );
                if ( i != ( subchannelList.length - 1 ) )
                {
                    finalchannel = finalchannel + ",";
                }
            }
        }
        else
        {
            finalchannel="none";
        }
       // String newccyPair = ccypair.equals("none")? ccypair:ccypair.substring( 0, 3 ) + "/" + ccypair.substring( 3 );
        Date fromDate = new Date(from);
        Date toDate = new Date(to);
        String orderQuery = "select array_to_json(array_agg(row_to_json(t)))"  +
                "                      from (SELECT CREATED,ORDERID, ORDERSTATE, ORG, CCYPAIR, BUYSELL, ORDERAMTUSD, ORDERAMT, FILLEDAMT, ORDERTYPE, TIMEINFORCE, CHANNEL" +
                " FROM ORDER_MASTER " +
                " WHERE CREATED >= '"+fromDate+  "' AND CREATED <=  '"+toDate +"' AND "+
                (!takerorg.equals( "none" ) ? "ORG IN ('" +takerorg+"')":"ORG = ORG " )   + " AND  "   +
                (!finalCcys.equals( "none" ) ? " CCYPAIR IN ('" +finalCcys +"')":"CCYPAIR=CCYPAIR") + " AND " +
                (!orderType.equals( "none" ) ? "ORDERTYPE IN ('"+orderType+"')":"ORDERTYPE=ORDERTYPE") +" AND " +
                (!status.equals( "none" ) ? "STATUS IN ('" +status +"')":"STATUS = STATUS") + "  AND  "   +
                (!subChannel.equals( "none" )? "CHANNEL IN ('" + finalchannel + "')":"CHANNEL = CHANNEL") + " AND " +
                (!tif.equals( "none" ) ? "TIMEINFORCE IN ('" +tif + "')": "TIMEINFORCE = TIMEINFORCE") + " AND " +
                (!origorg.equals("none") ?  "ORIGINATINGORG IN ('"+origorg +"')" :  "ORIGINATINGORG = ORIGINATINGORG")  + " AND  " +
                (!buysell.equals( "none" ) ? "BUYSELL IN ('"+buysell+"')" : "BUYSELL = BUYSELL ") +" ORDER BY CREATED " + "OFFSET " +offset+" LIMIT 10)t";

        String jsonData = ordersWithTradesDAO.getOrderCollection( orderQuery );

        return jsonData;
    }

    public String generateOrderCollectionQuery( String takerorg, String ccypair, String timeRange, String orderType,
                                                String status, String channel, String tif, String origorg, String buysell, int offset, String orderby, String groupby,
                                                boolean fireRowCount )
    {
        // split the time range into respective time zones.



        String subChannel = channel.equals( "none" ) ? channel : channel.replace( '-','/' ) ;

        String[] ccyList = ccypair.split( "," );
        String finalCcys="";
        if(!ccyList[0].equals( "none" ))
        {
            for ( int i = 0; i < ccyList.length; i++ )
            {
                finalCcys = finalCcys +"'"+ ccyList[i].substring( 0, 3 ) + "/" + ccyList[i].substring( 3 )+"'";
                if ( i != ( ccyList.length - 1 ) )
                {
                    finalCcys = finalCcys + ",";
                }
            }
        }
        else
        {
            finalCcys="none";
        }

        String[] subchannelList = subChannel.split( "," );
        String finalchannel="";
        if(!subchannelList[0].equals( "none" ))
        {
            for ( int i = 0; i < subchannelList.length; i++ )
            {
                finalchannel = finalchannel +"'"+ subchannelList[i].replace( '-','/' )+"'";
                if ( i != ( subchannelList.length - 1 ) )
                {
                    finalchannel = finalchannel + ",";
                }
            }
        }
        else
        {
            finalchannel="none";
        }

        String[] subbuysellList = buysell.split( "," );
        String finalbuysell="";
        if(!subbuysellList[0].equals( "none" ))
        {
            for ( int i = 0; i < subbuysellList.length; i++ )
            {
                finalbuysell = finalbuysell +"'"+ subbuysellList[i]+"'";
                if ( i != ( subbuysellList.length - 1 ) )
                {
                    finalbuysell = finalbuysell + ",";
                }
            }
        }
        else
        {
            finalbuysell="none";
        }

        String[] subtakerorgList = takerorg.split( "," );
        String finaltakerorg="";
        if(!subtakerorgList[0].equals( "none" ))
        {
            for ( int i = 0; i < subtakerorgList.length; i++ )
            {
                finaltakerorg = finaltakerorg +"'"+ subtakerorgList[i]+"'";
                if ( i != ( subtakerorgList.length - 1 ) )
                {
                    finaltakerorg = finaltakerorg + ",";
                }
            }
        }
        else
        {
            finaltakerorg="none";
        }


        String[] substatusList = status.split( "," );
        String finalstatus="";
        if(!substatusList[0].equals( "none" ))
        {
            for ( int i = 0; i < substatusList.length; i++ )
            {
                finalstatus = finalstatus +"'"+ substatusList[i]+"'";
                if ( i != ( substatusList.length - 1 ) )
                {
                    finalstatus = finalstatus + ",";
                }
            }
        }
        else
        {
            finalstatus="none";
        }


        String[] suborderTypeList = orderType.split( "," );
        String finalorderType="";
        if(!suborderTypeList[0].equals( "none" ))
        {
            for ( int i = 0; i < suborderTypeList.length; i++ )
            {
                finalorderType = finalorderType +"'"+ suborderTypeList[i].replace( '-','/' )+"'";
                if ( i != ( suborderTypeList.length - 1 ) )
                {
                    finalorderType = finalorderType + ",";
                }
            }
        }
        else
        {
            finalorderType="none";
        }


        String[] subtifList = tif.split( "," );
        String finaltif="";
        if(!subtifList[0].equals( "none" ))
        {
            for ( int i = 0; i < subtifList.length; i++ )
            {
                finaltif = finaltif +"'"+ subtifList[i]+"'";
                if ( i != ( subtifList.length - 1 ) )
                {
                    finaltif = finaltif + ",";
                }
            }
        }
        else
        {
            finaltif="none";
        }


        String[] suborigorgList = origorg.split( "," );
        String finalorigorg="";
        if(!suborigorgList[0].equals( "none" ))
        {
            for ( int i = 0; i < suborigorgList.length; i++ )
            {
                finalorigorg = finalorigorg +"'"+ suborigorgList[i]+"'";
                if ( i != ( suborigorgList.length - 1 ) )
                {
                    finalorigorg = finalorigorg + ",";
                }
            }
        }
        else
        {
            finalorigorg="none";
        }


        String []dates=timeRange.split(",");

        String orderQuery = "select array_to_json(array_agg(row_to_json(t)))"  +
                "                      from ( ";
        String rowCount = "select count(*) rowcount from (" ;
        for (int t=0;t <dates.length;t++)
        {
            String [] longTimes = dates[t].split( ":" );
            Date fromDate = new Date(Long.parseLong( longTimes[0]));
            Date toDate = new Date(Long.parseLong( longTimes[1]));


             String innerQuery=       "SELECT CREATED,ORDERID, ORDERSTATE, ORG, CCYPAIR, BUYSELL, ORDERAMTUSD, ORDERAMT, FILLEDAMT, ORDERTYPE, TIMEINFORCE, CHANNEL" +
                          " FROM ORDER_MASTER " +
                        " WHERE CREATED >= '"+fromDate+  "' AND CREATED <=  '"+toDate +"' AND "+
                        (!takerorg.equals( "none" ) ? "ORG IN (" +finaltakerorg+")":"ORG = ORG " )   + " AND  "   +
                        (!finalCcys.equals( "none" ) ? " CCYPAIR IN (" +finalCcys +")":"CCYPAIR=CCYPAIR") + " AND " +
                        (!orderType.equals( "none" ) ? "ORDERTYPE IN ("+finalorderType+")":"ORDERTYPE=ORDERTYPE") +" AND " +
                        (!status.equals( "none" ) ? "STATUS IN (" +finalstatus +")":"STATUS = STATUS") + "  AND  "   +
                        (!subChannel.equals( "none" )? "CHANNEL IN (" + finalchannel + ")":"CHANNEL = CHANNEL") + " AND " +
                        (!tif.equals( "none" ) ? "TIMEINFORCE IN (" +finaltif + ")": "TIMEINFORCE = TIMEINFORCE") + " AND " +
                        (!origorg.equals("none") ?  "ORIGINATINGORG IN ("+finalorigorg +")" :  "ORIGINATINGORG = ORIGINATINGORG")  + " AND  " +
                        (!buysell.equals( "none" ) ? "BUYSELL IN ("+finalbuysell+")" : "BUYSELL = BUYSELL ");


            orderQuery += innerQuery;
            rowCount += innerQuery;
            if ( t != ( dates.length - 1 ) )
            {
                orderQuery += " UNION " ;
                rowCount += " UNION " ;
            }

        }
        orderQuery += " ORDER BY " + (!orderby.equals("none")? orderby +" DESC " : "CREATED ") + "  OFFSET " +offset+" LIMIT 50)t";
        // String newccyPair = ccypair.equals("none")? ccypair:ccypair.substring( 0, 3 ) + "/" + ccypair.substring( 3 );

        /*String orderQuery = "select array_to_json(array_agg(row_to_json(t)))"  +
                "                      from (SELECT CREATED,ORDERID, ORDERSTATE, ORG, CCYPAIR, BUYSELL, ORDERAMTUSD, ORDERAMT, FILLEDAMT, ORDERTYPE, TIMEINFORCE, CHANNEL" +
                " FROM ORDER_MASTER " +
                " WHERE CREATED >= '"+fromDate+  "' AND CREATED <=  '"+toDate +"' AND "+
                (!takerorg.equals( "none" ) ? "ORG IN ('" +takerorg+"')":"ORG = ORG " )   + " AND  "   +
                (!finalCcys.equals( "none" ) ? " CCYPAIR IN ('" +finalCcys +"')":"CCYPAIR=CCYPAIR") + " AND " +
                (!orderType.equals( "none" ) ? "ORDERTYPE IN ('"+orderType+"')":"ORDERTYPE=ORDERTYPE") +" AND " +
                (!status.equals( "none" ) ? "STATUS IN ('" +status +"')":"STATUS = STATUS") + "  AND  "   +
                (!subChannel.equals( "none" )? "CHANNEL IN ('" + finalchannel + "')":"CHANNEL = CHANNEL") + " AND " +
                (!tif.equals( "none" ) ? "TIMEINFORCE IN ('" +tif + "')": "TIMEINFORCE = TIMEINFORCE") + " AND " +
                (!origorg.equals("none") ?  "ORIGINATINGORG IN ('"+origorg +"')" :  "ORIGINATINGORG = ORIGINATINGORG")  + " AND  " +
                (!buysell.equals( "none" ) ? "BUYSELL IN ('"+buysell+"')" : "BUYSELL = BUYSELL ") +" ORDER BY CREATED " + "OFFSET " +offset+" LIMIT 10)t";*/
        rowCount +=  ") t";
        int rowRum = 0;
        Map<String,String> jsonMap = new HashMap<String,String>(2);
        if (fireRowCount)
        {
             rowRum = ordersWithTradesDAO.getRowCount( rowCount );
            jsonMap.put("rowCount", String.valueOf( rowRum ) );
        }


        String jsonData = ordersWithTradesDAO.getOrderCollection( orderQuery );
        jsonMap.put("orderSet",jsonData);

        Genson genson = new Genson();
        String returnJSON ="[]";
        try
        {
            returnJSON= genson.serialize(jsonMap);
        }
        catch (Exception e )
        {
            log.warn( "Exception serializing map",e);
        }
        if (fireRowCount)
        {
            return returnJSON;
        }
        else
        {
            return jsonData;
        }
    }

    public String generateOrderCollectionQuery( String takerorg, String ccypair, long from, long to, String orderType,
                                                String status, String channel, String tif, String origorg, String buysell, int offset, String orderby, String groupby,
                                                boolean fireRowCount )
    {
        // split the time range into respective time zones.


        boolean addAndClause=false;
        String subChannel = channel.equals( "none" ) ? channel : channel.replace( '-','/' ) ;

        String[] ccyList = ccypair.split( "," );
        String finalCcys="";
        if(!ccyList[0].equals( "none" ))
        {
            for ( int i = 0; i < ccyList.length; i++ )
            {
                finalCcys = finalCcys +"'"+ ccyList[i].substring( 0, 3 ) + "/" + ccyList[i].substring( 3 )+"'";
                if ( i != ( ccyList.length - 1 ) )
                {
                    finalCcys = finalCcys + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalCcys="none";
        }

        String[] subchannelList = subChannel.split( "," );
        String finalchannel="";
        if(!subchannelList[0].equals( "none" ))
        {
            for ( int i = 0; i < subchannelList.length; i++ )
            {
                finalchannel = finalchannel +"'"+ subchannelList[i].replace( '-','/' )+"'";
                if ( i != ( subchannelList.length - 1 ) )
                {
                    finalchannel = finalchannel + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalchannel="none";
        }

        String[] subbuysellList = buysell.split( "," );
        String finalbuysell="";
        if(!subbuysellList[0].equals( "none" ))
        {
            for ( int i = 0; i < subbuysellList.length; i++ )
            {
                finalbuysell = finalbuysell +"'"+ subbuysellList[i]+"'";
                if ( i != ( subbuysellList.length - 1 ) )
                {
                    finalbuysell = finalbuysell + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalbuysell="none";
        }

        String[] subtakerorgList = takerorg.split( "," );
        String finaltakerorg="";
        if(!subtakerorgList[0].equals( "none" ))
        {
            for ( int i = 0; i < subtakerorgList.length; i++ )
            {
                finaltakerorg = finaltakerorg +"'"+ subtakerorgList[i]+"'";
                if ( i != ( subtakerorgList.length - 1 ) )
                {
                    finaltakerorg = finaltakerorg + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finaltakerorg="none";
        }


        String[] substatusList = status.split( "," );
        String finalstatus="";
        if(!substatusList[0].equals( "none" ))
        {
            for ( int i = 0; i < substatusList.length; i++ )
            {
                finalstatus = finalstatus +"'"+ substatusList[i]+"'";
                if ( i != ( substatusList.length - 1 ) )
                {
                    finalstatus = finalstatus + ",";
                }
                addAndClause=true;
            }
        }
        else
        {
            finalstatus="none";
        }


        String[] suborderTypeList = orderType.split( "," );
        String finalorderType="";
        if(!suborderTypeList[0].equals( "none" ))
        {
            for ( int i = 0; i < suborderTypeList.length; i++ )
            {
                finalorderType = finalorderType +"'"+ suborderTypeList[i].replace( '-','/' )+"'";
                if ( i != ( suborderTypeList.length - 1 ) )
                {
                    finalorderType = finalorderType + ",";
                }
                addAndClause=true;
            }
        }
        else
        {
            finalorderType="none";
        }


        String[] subtifList = tif.split( "," );
        String finaltif="";
        if(!subtifList[0].equals( "none" ))
        {
            for ( int i = 0; i < subtifList.length; i++ )
            {
                finaltif = finaltif +"'"+ subtifList[i]+"'";
                if ( i != ( subtifList.length - 1 ) )
                {
                    finaltif = finaltif + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finaltif="none";
        }


        String[] suborigorgList = origorg.split( "," );
        String finalorigorg="";
        if(!suborigorgList[0].equals( "none" ))
        {
            for ( int i = 0; i < suborigorgList.length; i++ )
            {
                finalorigorg = finalorigorg +"'"+ suborigorgList[i]+"'";
                if ( i != ( suborigorgList.length - 1 ) )
                {
                    finalorigorg = finalorigorg + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalorigorg="none";
        }




        String orderQuery = " ";
        String rowCount = "select count(*)  FROM SYS.ORDEREPA" ;
        /*for (int t=0;t <dates.length;t++)
        {
            String [] longTimes = dates[t].split( ":" );*/
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date fromDate = new Date(from);
        Date toDate = new Date(to);

        String dateFromString = format.format( fromDate ) ;

        String dateToString =  format.format(toDate) ;
        /*
          SELECT CREATED,ORDERID, ORDERSTATE, TAKERORG, CCYPAIR, SIDE, ORDERAMTUSD, ORDERAMT, FILLEDAMOUNT, ORDERTYPE, TIF, CHANNEL FROM SYS.ORDEREPA
        WHERE CREATED >= TIMESTAMP '2014-12-01 00:00:00' AND CREATED <=  TIMESTAMP '2014-12-31 00:00:00' AND TAKERORG = TAKERORG  AND   CCYPAIR IN ('EUR/USD')
        AND ORDERTYPE=ORDERTYPE AND ORDERSTATE = ORDERSTATE  AND  CHANNEL = CHANNEL AND TIF = TIF AND ORIGINATINGORG = ORIGINATINGORG AND
        SIDE = SIDE  ORDER BY CREATED    LIMIT 50
         */

        String innerQuery=       "SELECT CREATED,ORDERID, ORDERSTATE, TAKERORG, CCYPAIR, SIDE, ORDERAMTUSD, ORDERAMT, FILLEDAMOUNT, ORDERTYPE, TIF, CHANNEL" +
                " FROM SYS.ORDEREPA ";

                String whereClause=" WHERE CREATED >= TIMESTAMP '"+dateFromString+  "' AND CREATED <= TIMESTAMP '"+dateToString +"' "+
                (!takerorg.equals( "none" ) ? "AND TAKERORG IN (" +finaltakerorg+")  ":" " )   +
                (!finalCcys.equals( "none" ) ? "AND CCYPAIR IN (" +finalCcys +") ":" ") +
                (!orderType.equals( "none" ) ? "AND ORDERTYPE IN ("+finalorderType+") ":" ") +
                (!status.equals( "none" ) ? "AND ORDERSTATE IN (" +finalstatus +") ":" ")    +
                (!subChannel.equals( "none" )? "AND CHANNEL IN (" + finalchannel + ") ":" ") +
                (!tif.equals( "none" ) ? "AND TIF IN (" +finaltif + ") ": " ") +
                (!origorg.equals("none") ?  "AND ORIGINATINGORG IN ("+finalorigorg +") " :  "  ")   +
                (!buysell.equals( "none" ) ? "AND SIDE IN ("+finalbuysell+")" : " ");


        orderQuery += innerQuery+whereClause;
        rowCount += whereClause;
           /* if ( t != ( dates.length - 1 ) )
            {
                orderQuery += " UNION " ;
                rowCount += " UNION " ;
            }*/

        // }
        orderQuery += " ORDER BY " + (!orderby.equals("none")? orderby +" DESC " : "CREATED ") + " LIMIT 50  OFFSET " +offset;

        int rowRum = 0;
        Map<String,String> jsonMap = new HashMap<String,String>(2);
        if (fireRowCount)
        {
            rowRum = ordersWithTradesDAO.getRowCount( rowCount );
            jsonMap.put("rowCount", String.valueOf( rowRum ) );
        }


        String jsonData = ordersWithTradesDAO.getOrderCollection(orderQuery);
        jsonMap.put("orderSet",jsonData);

        Genson genson = new Genson();
        String returnJSON ="[]";
        try
        {
            returnJSON= genson.serialize(jsonMap);
        }
        catch (Exception e )
        {
            log.warn( "Exception serializing map",e);
        }
        if (fireRowCount)
        {
            return returnJSON;
        }
        else
        {
            return jsonData;
        }
    }

    public String generateOrderSummaryQuery( String takerorg, String ccypair, long from, long to, String orderType,
                                                String status, String channel, String tif, String origorg, String buysell, int offset, String orderby, String groupby,
                                                boolean fireRowCount )
    {
        // split the time range into respective time zones.


        boolean addAndClause=false;
        String subChannel = channel.equals( "none" ) ? channel : channel.replace( '-','/' ) ;

        String[] ccyList = ccypair.split( "," );
        String finalCcys="";
        if(!ccyList[0].equals( "none" ))
        {
            for ( int i = 0; i < ccyList.length; i++ )
            {
                finalCcys = finalCcys +"'"+ ccyList[i].substring( 0, 3 ) + "/" + ccyList[i].substring( 3 )+"'";
                if ( i != ( ccyList.length - 1 ) )
                {
                    finalCcys = finalCcys + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalCcys="none";
        }

        String[] subchannelList = subChannel.split( "," );
        String finalchannel="";
        if(!subchannelList[0].equals( "none" ))
        {
            for ( int i = 0; i < subchannelList.length; i++ )
            {
                finalchannel = finalchannel +"'"+ subchannelList[i].replace( '-','/' )+"'";
                if ( i != ( subchannelList.length - 1 ) )
                {
                    finalchannel = finalchannel + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalchannel="none";
        }

        String[] subbuysellList = buysell.split( "," );
        String finalbuysell="";
        if(!subbuysellList[0].equals( "none" ))
        {
            for ( int i = 0; i < subbuysellList.length; i++ )
            {
                finalbuysell = finalbuysell +"'"+ subbuysellList[i]+"'";
                if ( i != ( subbuysellList.length - 1 ) )
                {
                    finalbuysell = finalbuysell + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalbuysell="none";
        }

        String[] subtakerorgList = takerorg.split( "," );
        String finaltakerorg="";
        if(!subtakerorgList[0].equals( "none" ))
        {
            for ( int i = 0; i < subtakerorgList.length; i++ )
            {
                finaltakerorg = finaltakerorg +"'"+ subtakerorgList[i]+"'";
                if ( i != ( subtakerorgList.length - 1 ) )
                {
                    finaltakerorg = finaltakerorg + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finaltakerorg="none";
        }


        String[] substatusList = status.split( "," );
        String finalstatus="";
        if(!substatusList[0].equals( "none" ))
        {
            for ( int i = 0; i < substatusList.length; i++ )
            {
                finalstatus = finalstatus +"'"+ substatusList[i]+"'";
                if ( i != ( substatusList.length - 1 ) )
                {
                    finalstatus = finalstatus + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalstatus="none";
        }


        String[] suborderTypeList = orderType.split( "," );
        String finalorderType="";
        if(!suborderTypeList[0].equals( "none" ))
        {
            for ( int i = 0; i < suborderTypeList.length; i++ )
            {
                finalorderType = finalorderType +"'"+ suborderTypeList[i].replace( '-','/' )+"'";
                if ( i != ( suborderTypeList.length - 1 ) )
                {
                    finalorderType = finalorderType + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalorderType="none";
        }


        String[] subtifList = tif.split( "," );
        String finaltif="";
        if(!subtifList[0].equals( "none" ))
        {
            for ( int i = 0; i < subtifList.length; i++ )
            {
                finaltif = finaltif +"'"+ subtifList[i]+"'";
                if ( i != ( subtifList.length - 1 ) )
                {
                    finaltif = finaltif + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finaltif="none";
        }


        String[] suborigorgList = origorg.split( "," );
        String finalorigorg="";
        if(!suborigorgList[0].equals( "none" ))
        {
            for ( int i = 0; i < suborigorgList.length; i++ )
            {
                finalorigorg = finalorigorg +"'"+ suborigorgList[i]+"'";
                if ( i != ( suborigorgList.length - 1 ) )
                {
                    finalorigorg = finalorigorg + ",";
                }
            }
            addAndClause=true;
        }
        else
        {
            finalorigorg="none";
        }




        String orderQuery = " ";
       // String rowCount = "select count(*)  FROM SYS.ORDEREPA" ;
        /*for (int t=0;t <dates.length;t++)
        {
            String [] longTimes = dates[t].split( ":" );*/
        Date fromDate;
        Date toDate;
        String dateFromString=null;
        String dateToString=null;
        if (from >0 && to >0)
        {
            DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
            fromDate = new Date( from );
            toDate = new Date( to );

            dateFromString = format.format( fromDate );

            dateToString = format.format( toDate );
        }

        String innerQuery=       "SELECT TAKERORG,CCYPAIR, SUM(ORDERAMTUSD) as totalorderamtusd ,SUM(ORDERAMTUSD)/COUNT(*) as avgfillratio" +
                " FROM SYS.ORDEREPA ";

        String whereClause=" WHERE " +(dateFromString != null ? " CREATED >= TIMESTAMP '"+dateFromString+  "' AND CREATED <= TIMESTAMP '"+dateToString +"' ":" ")+
                (!takerorg.equals( "none" ) ? (addAndClause ?" AND ":" ")+"TAKERORG IN (" +finaltakerorg+")  ":" " )   +
                (!finalCcys.equals( "none" ) ? (addAndClause ? "AND": " ") +" CCYPAIR IN (" +finalCcys +") ":" ") +
                (!orderType.equals( "none" ) ? (addAndClause ? " AND ": " ") + "ORDERTYPE IN ("+finalorderType+") ":" ") +
                (!status.equals( "none" ) ?  (addAndClause ? " AND ": " ") +"ORDERSTATE IN (" +finalstatus +") ":" ")    +
                (!subChannel.equals( "none" )? (addAndClause ? " AND ": " " )+"CHANNEL IN (" + finalchannel + ") ":" ") +
                (!tif.equals( "none" ) ? (addAndClause ? " AND ":" ")+ "TIF IN (" +finaltif + ") ": " ") +
                (!origorg.equals("none") ? (addAndClause ? " AND " :" ")+ "ORIGINATINGORG IN ("+finalorigorg +") " :  "  ")   +
                (!buysell.equals( "none" ) ? (addAndClause ? " AND ":" ")+ "SIDE IN ("+finalbuysell+")" : " ");


        orderQuery += innerQuery+whereClause;
        //rowCount += whereClause;
           /* if ( t != ( dates.length - 1 ) )
            {
                orderQuery += " UNION " ;
                rowCount += " UNION " ;
            }*/

        // }
        orderQuery += " GROUP BY TAKERORG,CCYPAIR ORDER BY TAKERORG,CCYPAIR";

        int rowRum = 0;
        Map<String,String> jsonMap = new HashMap<String,String>(2);
        /*if (fireRowCount)
        {
            rowRum = ordersWithTradesDAO.getRowCount( rowCount );
            jsonMap.put("rowCount", String.valueOf( rowRum ) );
        }*/


        String jsonData = ordersWithTradesDAO.getOrderSummary(orderQuery);
        jsonMap.put("orderSet",jsonData);

        Genson genson = new Genson();
        String returnJSON ="[]";
        try
        {
            returnJSON= genson.serialize(jsonMap);
        }
        catch (Exception e )
        {
            log.warn( "Exception serializing map",e);
        }
        if (fireRowCount)
        {
            return returnJSON;
        }
        else
        {
            return jsonData;
        }
    }
    public List<String> getTradeEventsForOrders(String orderId) {
        return ordersWithTradesDAO.getTradeEventsForOrder(orderId);
    }

    public String getTradeEventsForOrder(String orderId) {

        Genson genson = new Genson();
        String tradesJSON ="[]";
        Map<String,String> tradeEventsMap = new HashMap<String,String>();
        // get the list of trade ids for the order.
        List <String> tradeIds = ordersWithTradesDAO.getTradeIDSForOrder(orderId);

        for (String id:tradeIds)
        {
            tradeEventsMap.put(id,getRateEventsForTrade(id));
        }
        try
        {
            tradesJSON= genson.serialize(tradeEventsMap);
        }
        catch (Exception e )
        {
            log.warn( "Exception serializing map", e );
        }
        return tradesJSON;
    }

    public void setTradeWeightService(TradeWeightService tradeWeightService) {
        this.tradeWeightService = tradeWeightService;
    }

    public void setTradeProviderStreamInfoService(TradeProviderStreamInfoService tradeProviderStreamInfoService) {
        this.tradeProviderStreamInfoService = tradeProviderStreamInfoService;
    }

    public void setTradeMasterDao(TradeMasterDao tradeMasterDao) {
        this.tradeMasterDao = tradeMasterDao;
    }

    public void setAuxFieldsCache(AuxFieldsCache auxFieldsCache) {
        this.auxFieldsCache = auxFieldsCache;
    }

    public void setPortfolioService(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }
}
