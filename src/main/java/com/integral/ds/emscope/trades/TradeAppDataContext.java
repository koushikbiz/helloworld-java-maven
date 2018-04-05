package com.integral.ds.emscope.trades;

import com.integral.ds.dao.TradeDAO;

public class TradeAppDataContext {
	
	private TradeDAO tradeDAO;
	
	public void setTradeDAO(TradeDAO tradeDAO) {
		this.tradeDAO = tradeDAO;
	}

	public String getTradeList (String tradeID,String orderID)	{
		return tradeDAO.getTradeDetails(tradeID, orderID);
	}

    public String getTradeSummaryTypeStats(String timerange, String org)
    {
        return tradeDAO.getTradeTypeStatistics(timerange,org) ;
    }

    public String getTradeSummaryStatusStats(String timerange, String org)
    {
        return tradeDAO.getTradeStatsStatistics( timerange,org) ;
    }

    public String getTopnCCyPairs( String timerange, String org )
    {

        return tradeDAO.getTopnCCyPairs(timerange,org);
    }

    public String getTopnOrgs( String timerange, String org )
    {

        return tradeDAO.getTopnOrgs(timerange,org);
    }

    public String getTopnOrderCCyPairs( String query )
    {
        return tradeDAO.getTopnOrderCCyPairs( query );
    }

    public String getTopnOrderOrgs( String query )
    {
        return tradeDAO.getTopnOrderOrgs( query );
    }
}