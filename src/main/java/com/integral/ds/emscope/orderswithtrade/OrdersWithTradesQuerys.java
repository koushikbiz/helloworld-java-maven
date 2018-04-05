package com.integral.ds.emscope.orderswithtrade;

public class OrdersWithTradesQuerys {

	public static final String Y_GET_SINGLE_ORDER_AND_TRADE = 
			"select CREATED AS CREATEDTIMESTAMP, "
			+ "O.CCYPAIR, O.BUYSELL as OrderBuySell, O.STATUS as ORDERSTATUS, O.ORG AS ORG, ORDERTYPE, O.ORDERID, PERSISTENT, "
			+ "CLIENTORDERID, extract(EPOCH from LASTEVENT)::INT8 * 1000 + extract(MILLISECONDS from LASTEVENT) LASTEVENT, "
			+ "O.TENOR, O.ACCOUNT, O.DEALER, O.DEALT, TIMEINFORCE, ORDERRATE, ORDERAMT, FILLEDAMT, (O.filledamt/O.orderamt)*100 As FILLEDPCT, "
			+ "FILLRATE, PI, ORDERAMTUSD AS OrdAmtUSD, FILLEDAMTUSD FILLAMTUSD, PIPNL AS PPNL,td3.stream, td3.orderid as orderid, baseAmt, "
			+ "termAmt, usdAmt, exectime, TD3.status as "
			+ "tradestatus,mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
			+ "td3.baseamt as baseamt,td3.rate as rate, TD3.stream from "
			+ "ORDER_MASTER O LEFT JOIN TRADES_TAKER TD3 USING (ORDERID)"
			+ " where O.ORDERID = :ORDERID";

    public static final String GET_SINGLE_ORDER_JSON_DETAILS =
            "select array_to_json(array_agg(row_to_json(t)))" +
                    "  from (select to_char(ord.CREATED,'YYYY-MM-DD HH24:MI:SS.MS') as CREATEDTIMESTAMP, ord.CCYPAIR, ord.BUYSELL, ord.STATUS AS OrderStatus, ord.ORG,ord.ORDER_STREAM as stream,"
                    + "ord.ORDERTYPE, ord.ORDERID, ord.PERSISTENT, ord.CLIENTORDERID, ord.LASTEVENT,"
                    + "ord.TENOR, ord.ACCOUNT, ord.DEALER, ord.DEALT, ord.TIMEINFORCE, ord.ORDERRATE, ord.ORDERAMT, ord.FILLEDAMT, " +
                    " ord.FILLEDPERCENT As FILLEDPCT, ord.FILLRATE, epa.PI, ord.ORDERAMTUSD AS OrdAmtUSD, " +
                    " ord.FILLEDAMTUSD FILLAMTUSD, ord.PIPNL AS PPNL, "   +
                    " to_char(cast(ord.MARKETRANGE as float),'9999D9999999') as \"MarketRange\",expirytime as \"Expiry Time\"," +
                    " ord.visibility as Visibility,server as Server,servername as \"Server Name\"," +
                    "servermanaged as \"Server Managed\"," +
                    "fixSessionId as \"FIX Session\"," +
                    "ord.originatingorg as \"Originating Org\",ord.ordertype as \"Order Type\",ord.coverExecMethod as \"Cover Execution Method\"," +
                    "ord.channel as Channel,ord.pricedisplay as \"Price Display\",ord.executionstrategy as \"Execution Strategy\"," +
                    "ord.fixingDate as \"Fixing Date\",ord.preferredProviders as \"Preferred Proviers\"," +
                    "ord.customSpreads as \"Custom Spot Spreads\",portfolioTID as \"Portfolio ID\",executionInstruction as \"Execution Instruction\"," +
                    "executiontype as \"Execution Type\",minfillqty as \"Min Fill Qty\",showamt as \"Show Amount\",ord.coveredcpty as \"Covered cpty\"," +
                    "ord.coveredcptyuser as \"Covered Cpty User\"" +
                    ",ord.originatinguser as \"Originating User\"   " +
                    ",ord.brokerforcptya , ord.externalaccountid " +
                    ", epa.orderid as epaOrderId, epa.orderstate , epa.originatingorg, epa.takerorg,"+
                    " epa.side, epa.created, ord.lastevent,"+
                    "  epa.numtrades, epa.confirmedtrades, epa.rejectedtrades,"+
                    "epa.failedtrades, epa.totalorderduration, epa.totaltradeduration, epa.numsweeps,"+
                    "epa.ismidmarket,  epa.marketrangepip,"+
                    "epa.pi, epa.pipip, epa.piusd, epa.pi1bucket, epa.pi2bucket, epa.pi3bucket, epa.filledamount,"+
                    "epa.filledamtusd, epa.fillratio, epa.filled1bucket, epa.filled2bucket, epa.filled3bucket,"+
                    "epa.israteinsnapshot, epa.epaerrorcode, epa.isinconsolidatedreport, epa.bratcreated,"+
                    "epa.brdiffcreated, epa.brdiffcreatedpip, epa.brdiffcreatedusd, epa.brlastevent,"+
                    "epa.brdifflastevent, epa.brdifflasteventpip, epa.brdifflasteventusd,"+
                    "epa.orderdifficultycode, epa.orderdifficultypip, epa.priceatsize, epa.sizeatprice "+
                    " from ORDER_MASTER ord  left join ORDEREPA epa using (orderid) where ord.ORDERID = ? ) t";

    public static final String GET_SINGLE_ORDER_JSON_DETAILS_WITHOUR_MARKET_RANGE =
            "select array_to_json(array_agg(row_to_json(t)))" +
                    "  from (select to_char(ord.CREATED,'YYYY-MM-DD HH24:MI:SS.MS') as CREATEDTIMESTAMP, ord.CCYPAIR, ord.BUYSELL, ord.STATUS AS OrderStatus, ord.ORG,ord.ORDER_STREAM as stream,"
                    + "ord.ORDERTYPE, ord.ORDERID, ord.PERSISTENT, ord.CLIENTORDERID, ord.LASTEVENT,"
                    + "ord.TENOR, ord.ACCOUNT, ord.DEALER, ord.DEALT, ord.TIMEINFORCE, ord.ORDERRATE, ord.ORDERAMT, ord.FILLEDAMT, " +
                    " ord.FILLEDPERCENT As FILLEDPCT, ord.FILLRATE, epa.PI, ord.ORDERAMTUSD AS OrdAmtUSD, " +
                    " ord.FILLEDAMTUSD FILLAMTUSD, ord.PIPNL AS PPNL, "   +
                    " ord.MARKETRANGE as \"MarketRange\",expirytime as \"Expiry Time\"," +
                    " ord.visibility as Visibility,server as Server,servername as \"Server Name\"," +
                    "servermanaged as \"Server Managed\"," +
                    "fixSessionId as \"FIX Session\"," +
                    "ord.originatingorg as \"Originating Org\",ord.ordertype as \"Order Type\",ord.coverExecMethod as \"Cover Execution Method\"," +
                    "ord.channel as Channel,ord.pricedisplay as \"Price Display\",ord.executionstrategy as \"Execution Strategy\"," +
                    "ord.fixingDate as \"Fixing Date\",ord.preferredProviders as \"Preferred Proviers\"," +
                    "ord.brokerforcptya , " +
                    "ord.customSpreads as \"Custom Spot Spreads\",portfolioTID as \"Portfolio ID\",executionInstruction as \"Execution Instruction\"," +
                    "executiontype as \"Execution Type\",minfillqty as \"Min Fill Qty\",showamt as \"Show Amount\",ord.coveredcpty as \"Covered cpty\"," +
                    "ord.coveredcptyuser as \"Covered Cpty User\"" +
                    ",ord.originatinguser as \"Originating User\"  , ord.externalaccountid " +
                    ", epa.orderid as epaOrderId, epa.orderstate , epa.originatingorg, epa.takerorg,"+
                    " epa.side, epa.created, ord.lastevent,"+
                    "  epa.numtrades, epa.confirmedtrades, epa.rejectedtrades,"+
                    "epa.failedtrades, epa.totalorderduration, epa.totaltradeduration, epa.numsweeps,"+
                    "epa.ismidmarket,  epa.marketrangepip,"+
                    "epa.pi, epa.pipip, epa.piusd, epa.pi1bucket, epa.pi2bucket, epa.pi3bucket, epa.filledamount,"+
                    "epa.filledamtusd, epa.fillratio, epa.filled1bucket, epa.filled2bucket, epa.filled3bucket,"+
                    "epa.israteinsnapshot, epa.epaerrorcode, epa.isinconsolidatedreport, epa.bratcreated,"+
                    "epa.brdiffcreated, epa.brdiffcreatedpip, epa.brdiffcreatedusd, epa.brlastevent,"+
                    "epa.brdifflastevent, epa.brdifflasteventpip, epa.brdifflasteventusd,"+
                    "epa.orderdifficultycode, epa.orderdifficultypip, epa.priceatsize, epa.sizeatprice"+
                    " from ORDER_MASTER ord  left join ORDEREPA epa using (orderid) where ord.ORDERID = ? ) t";


    public static final String IS_MARKET_RANGE_PRESENT = "select marketrange from order_master where orderid = ?";


    public static final String Y_ORDERS_SUMMARY_QUERY = "select created as CREATEDTIMESTAMP, "
			+ "O.ORDERID ORDERID, O.ORDERRATE "
			+ "AS ORDERRATE, O.ORDERAMT AS ORDERAMT from ORDER_MASTER o"
			+ " where O.ORG LIKE :ORG  and O.CCYPAIR = :CCYPAIR " 
			+ "and O.CREATED > :FROM and O.CREATED <  :TO and o.ordertype LIKE :ORDERTYPE and o.orderamt > :MINAMOUNT";
	
	public static final String Y_TRADES_SUMMARY_QUERY = "select  T.TRADEID AS TRADEID, ExecTime as EXECTIMESTAMP, "
			+ "T.rate as Rate, ORDERID, T.baseamt as baseamt, t.ccypair as CcyPair, t.status as Status from "
			+ "{trades_table_name} t "
			+ "where T.EXECTIME > :FROM and T.EXECTIME < :TO and T.CCYPAIR = :CCYPAIR and T.ORG = :ORG";
	
	public static final String GET_FULL_ORDER_AND_TRADE = 
			"select CREATED AS CREATEDTIMESTAMP, "
            + "O.CCYPAIR, O.BUYSELL as OrderBuySell, O.STATUS as ORDERSTATUS, O.ORG AS ORG, O.ORDERTYPE, O.ORDERID, PERSISTENT, "
			+ "CLIENTORDERID, extract(EPOCH from LASTEVENT) * 1000 AS  LASTEVENT, "
			+ "O.TENOR, O.ACCOUNT, O.DEALER, O.DEALT, TIMEINFORCE, ORDERRATE, ORDERAMT, FILLEDAMT, (o.filledamt/o.orderamt)*100 As FILLEDPCT, "
			+ "FILLRATE, PI, ORDERAMTUSD AS OrdAmtUSD, FILLEDAMTUSD FILLAMTUSD, PIPNL AS PPNL,td3.stream, O.orderid as orderid, baseAmt, "
			+ "termAmt, usdAmt, exectime as EXECTIMESTAMP, TD3.status as "
			+ "tradestatus,mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
			+ "td3.baseamt as baseamt,td3.rate as rate, Td3.stream as Maker_Stream , td3.spotrate as spotrate "
			+ "from ORDER_MASTER O FULL OUTER JOIN TRADE_MASTER TD3 ON O.ORDERID =TD3.ORDERID "
			+ " where  O.CREATED > :FROM and O.CREATED <= :TO AND O.ORG LIKE :ORG AND"
			+ "  O.CCYPAIR = :CCYPAIR AND "
			+ " O.ordertype LIKE :ORDERTYPE and o.orderamt > :MINAMOUNT ";

	public static final String ORDERS_BY_ID_QUERY = "select ord.CREATED as CREATEDTIMESTAMP, ord.CCYPAIR, ord.BUYSELL, ord.STATUS AS OrderStatus, ord.ORG,ord.ORDER_STREAM,"
			+ "ord.ORDERTYPE, ord.ORDERID, ord.PERSISTENT, ord.CLIENTORDERID, extract(EPOCH from ord.LASTEVENT) * 1000 AS  LASTEVENT, ord.provider , "
			+ "ord.TENOR, ord.ACCOUNT, ord.DEALER, ord.DEALT, ord.TIMEINFORCE, ord.ORDERRATE, ord.ORDERAMT, ord.FILLEDAMT, ord.FILLRATE, epa.PI, ord.ORDERAMTUSD AS OrdAmtUSD, ord.FILLEDAMTUSD FILLAMTUSD, " +
            "ord.PIPNL AS PPNL, ord.MARKETRANGE as MARKETRANGE, ord.EXECUTIONSTRATEGY as EXECUTIONSTRATEGY, ord.eventtime as eventtime, ord.brokerforcptya , "+
            "  epa.numtrades, epa.confirmedtrades, epa.rejectedtrades,"+
            "epa.failedtrades, epa.totalorderduration, epa.totaltradeduration, epa.numsweeps"
            + "   from ORDER_MASTER ord  left join ORDEREPA epa using (orderid) where ord.ORDERID = :ORDERID ";

	
	public static final String TRADES_BY_ORDER_IDS = "select td3.reallp , td3.stream, td3.orderid as orderid, org, baseAmt, termAmt, usdAmt, "
	 		+  	" exectime as EXECTIMESTAMP, status as TradeStatus, "
		 		+ "ccypair,  mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
		 		+ "td3.baseamt as baseamt,td3.rate as rate, stream as tickStream,masklp as maskLP, td3.termamt as termamt, " 
		 		+ "td3.matchingamount as matchingamount, td3.spotrate as spotrate,td3.sweepnumber as sweepnumber, td3.matchedrate as matchedrate , ratelog , responsetime , td3.ccypair as ccypair , maker_org as makerorg, td3.failed from {trades_table_name} TD3"
				+ " where ORDERID = :ORDERID ";

    public static final String TRADES_BY_ORDER_IDS_RFS = "select td3.reallp , td3.stream, td3.orderid as orderid, org, baseAmt, termAmt, usdAmt, "
            +  	" exectime as EXECTIMESTAMP, status as TradeStatus, "
            + "ccypair,  mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
            + "td3.baseamt as baseamt,td3.rate as rate, stream as tickStream,masklp as maskLP, td3.termamt as termamt, "
            + "td3.matchingamount as matchingamount, td3.spotrate as spotrate,td3.sweepnumber as sweepnumber, td3.matchedrate as matchedrate , ratelog , responsetime , td3.ccypair as ccypair , maker_org as makerorg,td3.failed from {trades_table_name} TD3"
            + " where TRADEID = :ORDERID ";

    public static final String TRADES_BY_TRADE_IDS = "select td3.stream, td3.orderid as orderid, org, baseAmt, termAmt, usdAmt, "
                +  	" exectime as EXECTIMESTAMP, status as TradeStatus, "
                + "ccypair,  mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
                + "td3.baseamt as baseamt,td3.rate as rate, stream as tickStream,masklp as maskLP, td3.termamt as termamt, "
                + "td3.matchingamount as matchingamount, td3.spotrate as spotrate,td3.sweepnumber as sweepnumber, td3.matchedrate as matchedrate , ratelog , responsetime , td3.ccypair as ccypair ,maker_org as makerorg ,td3.failed from {trades_table_name} TD3"
                + " where tradeid = :TRADEID ";
	
	public static final String COVEREDTRADE_QI = "select td3.stream, td3.orderid as orderid, org, baseAmt, termAmt, usdAmt, "
	 			+ "exectime as EXECTIMESTAMP, status as TradeStatus, "
		 		+ "ccypair,  mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
		 		+ "td3.baseamt as baseamt,td3.rate as rate, stream as tickStream from {trades_table_name} TD3 where TD3.TRADEID in "
		 		+ "(select COVEREDTRADEID from TRADE_MASTER TM2 where TM2.TRADEID = :TRADEID) order by td3.tradeid";


    public static final String COVEREDTRADE_WITH_ORDER_QI = "select td3.stream, td3.orderid as orderid, org, baseAmt, termAmt, usdAmt, "
            + "exectime as EXECTIMESTAMP, status as TradeStatus, "
            + "ccypair,  mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
            + "td3.baseamt as baseamt,td3.rate as rate, stream as tickStream from {trades_table_name} TD3 where TD3.TRADEID in "
            + "(select COVEREDTRADEID from TRADE_MASTER TM2 where TM2.TRADEID = :TRADEID and TM2.ORDERID = :ORDERID) order by td3.tradeid";


    public static final String COVEREDTRADE_Q2 = "select td3.stream, td3.orderid as orderid, org, baseAmt, termAmt, usdAmt, "
 			+ "exectime as EXECTIMESTAMP, status as TradeStatus, "
	 		+ "ccypair,  mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
	 		+ "td3.baseamt as baseamt,td3.rate as rate, stream as tickStream from {trades_table_name} TD3 where COVEREDTRADEID = :TRADEID order by td3.tradeid";

    public static final String COVEREDTRADE__WITH_ORDER_Q2 = "select td3.stream, td3.orderid as orderid, org, baseAmt, termAmt, usdAmt, "
            + "exectime as EXECTIMESTAMP, status as TradeStatus, "
            + "ccypair,  mkrtkr, td3.type as type, td3.buysell as buysell, td3.tradeid as tradeid, td3.cpty as cpty, stream, "
            + "td3.baseamt as baseamt,td3.rate as rate, stream as tickStream from {trades_table_name} TD3 where COVEREDTRADEID = :TRADEID and orderid = :ORDERID order by td3.tradeid";

    public static final String COVERINGORDER_Q = "select array_to_json(array_agg(row_to_json(t))) " +
            "  from (select created as CREATEDTIMESTAMP, "
            + "O.ORDERID AS ORDERID, O.ORDERRATE "
            + "AS ORDERRATE, O.ORDERAMT AS ORDERAMT from ORDER_MASTER O"
            + " WHERE  O.COVEREDORDERID = (select OM2.COVEREDORDERID from ORDER_MASTER OM2 where OM2.ORDERID = ? AND OM2.COVEREDORDERID  IS NOT NULL" +
            "   AND char_length(OM2.COVEREDORDERID) > 0) AND O.ORDERID !=? order by O.ORDERID ) t";

    public static final String COVEREDORDER_Q = "select array_to_json(array_agg(row_to_json(t))) " +
            "  from (select created as CREATEDTIMESTAMP, "
            + "O.ORDERID AS ORDERID, O.ORDERRATE "
            + "AS ORDERRATE, O.ORDERAMT AS ORDERAMT from ORDER_MASTER O"
            + " WHERE  O.COVEREDORDERID = ? order by CREATEDTIMESTAMP ) t";

    public static final String COVEREDFILLRATE_Q = "select created as CREATEDTIMESTAMP, "
            + "O.ORDERID AS ORDERID,O.FILLRATE, O.ORDERRATE "
            + "AS ORDERRATE, O.FILLEDAMT, O.ORDERAMT AS ORDERAMT from ORDER_MASTER O"
            + " WHERE  O.COVEREDORDERID = ? order by CREATEDTIMESTAMP";

    public static final String ORIGINATINORDER_Q = "select array_to_json(array_agg(row_to_json(t))) " +
            "  from (select created as CREATEDTIMESTAMP, "
            + "O.ORIGORDERID AS ORIGINATINGORDERID, O.ORDERRATE "
            + "AS ORDERRATE, O.ORDERAMT AS ORDERAMT from ORDER_MASTER O"
            + " WHERE  O.ORDERID = ?  AND O.ORIGORDERID IS NOT NULL AND O.ORIGORDERID !=? order by O.ORDERID) t";

    public static final String MARKETSNAPSHOT_TRADE_Q="SELECT SNAPSHOT FROM TRADE_MASTER WHERE TRADEID = ?";

	public static final String MARKETSNAPSHOT_TRADE_FOR_ORDER = "SELECT SNAPSHOT FROM TRADE_MASTER WHERE TRADEID = ? and orderid = ?";

    public static final String MARKETSNAPSHOT_ORDER_Q="SELECT CREATED,MARKETSNAPSHOT FROM ORDER_MASTER WHERE ORDERID = ?";

    public static final String TRADES_FOR_ORDER_Q ="select tradeid from trade_master where orderid = ?";

    public static final String RATEEVENTS_TRADE_Q="SELECT RATELOG FROM TRADE_MASTER WHERE TRADEID = ?";

	public static final String RATEEVENTS_TRADE_WITH_ORDER_Q="SELECT RATELOG FROM TRADE_MASTER WHERE TRADEID = ? and orderid = ?";

    public static final String TRADE_EVENT_Q="select tradeid,ratelog,status,rate from trade_master where orderid= ?";


	public static void main(String[] args) {
		System.out.println(Y_TRADES_SUMMARY_QUERY);
	}
}
