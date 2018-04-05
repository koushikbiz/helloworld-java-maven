package com.integral.ds.emscope.trades.weights;

import com.integral.ds.dao.TradeProbabilityDao;
import com.integral.ds.dao.TradeWeightDao;
import com.integral.ds.dto.TradeProbabilityInfo;
import com.integral.ds.dto.TradeWeightInfo;
import com.integral.ds.emscope.common.ACLAuthorizationService;
import com.integral.ds.util.PropertyReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Trade Weight service for trades.
 *
 * @author Rahul Bhattacharjee
 */
public class TradeWeightService {

    private final Log LOGGER = LogFactory.getLog(TradeWeightService.class);

    private TradeWeightDao tradeWeightDao;
    private TradeProbabilityDao tradeProbabilityDao;
    private ACLAuthorizationService authorizationService;

    public Map<String,List<AttributeWeight>> getTradeWeights(String tradeId) {
        List<TradeWeightInfo> tradeWeightInfos = tradeWeightDao.getWeightsForTrade(tradeId);

        List<AttributeWeight> weightList = new ArrayList<AttributeWeight>();
        for(TradeWeightInfo weightInfo : tradeWeightInfos) {
            AttributeWeight attributeWeight = new AttributeWeight();
            attributeWeight.setWeight(weightInfo.getFactor());
            attributeWeight.setAttributeName(getAttributeFromDescription(weightInfo.getWeightDescription()));
            weightList.add(attributeWeight);
        }
        addTradeProbability(weightList,tradeId);
        return sort(weightList);
    }

    public Map<String,String> getAvailabilityMapForTrades(List<String> tradeIds) {
        if(tradeIds == null || tradeIds.isEmpty()) {
            return Collections.EMPTY_MAP;
        }

        Map<String,String> result = new HashMap<String,String>();
        for(String trade : tradeIds) {
            result.put(trade,"-1");
        }

        if(!isAuthorizedToInvoke()) {
            return result;
        }

        List<TradeProbabilityInfo> tradeProbabilityInfos = tradeProbabilityDao.getProbabilityForTrades(tradeIds);
        for(TradeProbabilityInfo tradeProbabilityInfo : tradeProbabilityInfos) {
            result.put(tradeProbabilityInfo.getTradeId(),Float.toString(tradeProbabilityInfo.getProbability()));
        }
        return result;
    }

    private boolean isAuthorizedToInvoke() {
        return authorizationService.isAuthorizedToInvoke(PropertyReader.KEY_TRADE_WEIGHT_ACL);
    }

    private void addTradeProbability(List<AttributeWeight> weightList, String tradeId) {
        TradeProbabilityInfo tradeProbabilityInfo = tradeProbabilityDao.getProbabilityForTrade(tradeId);
        if(tradeProbabilityInfo != null) {
            AttributeWeight attributeWeight = new AttributeWeight();
            attributeWeight.setAttributeName("probability");
            attributeWeight.setWeight(tradeProbabilityInfo.getProbability());
            weightList.add(attributeWeight);
        }
    }

    private Map<String,List<AttributeWeight>> sort(List<AttributeWeight> weightList) {
        List<AttributeWeight> positive = new ArrayList<AttributeWeight>();
        List<AttributeWeight> negative = new ArrayList<AttributeWeight>();
        for(AttributeWeight weight : weightList) {
            float factor = weight.getWeight();
            if(factor >= 0) {
                positive.add(weight);
            } else {
                negative.add(weight);
            }
        }

        Collections.sort(positive, new Comparator<AttributeWeight>() {
            @Override
            public int compare(AttributeWeight o1, AttributeWeight o2) {
                Float firstFactor = o1.getWeight();
                Float secondFactor = o2.getWeight();
                int side = Float.compare(firstFactor,secondFactor);
                return side == 0 ? side : -side;
            }
        });

        Collections.sort(negative , new Comparator<AttributeWeight>() {
            @Override
            public int compare(AttributeWeight o1, AttributeWeight o2) {
                Float firstFactor = o1.getWeight();
                Float secondFactor = o2.getWeight();
                return Float.compare(firstFactor,secondFactor);
            }
        });

        Map<String,List<AttributeWeight>> result = new HashMap<String,List<AttributeWeight>>();
        result.put("POSITIVE_ATTRIBUTES",positive);
        result.put("NEGATIVE_ATTRIBUTES",negative);
        return result;
    }

    private String getAttributeFromDescription(String description) {
        try {
            String [] splits = description.split("-");
            return splits.length == 2  ? splits[1] : description ;
        } catch (Exception e) {
            LOGGER.error("Exception while calculating description from attribute name.",e);
        }
        return null;
    }

    public void setTradeWeightDao(TradeWeightDao tradeWeightDao) {
        this.tradeWeightDao = tradeWeightDao;
    }

    public void setTradeProbabilityDao(TradeProbabilityDao tradeProbabilityDao) {
        this.tradeProbabilityDao = tradeProbabilityDao;
    }

    public void setAuthorizationService(ACLAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
}
