package com.integral.ds.emscope.orderswithtrade;

import com.integral.ds.emscope.trades.TradeGrouping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper for execution summary.
 *
 * @author Rahul Bhattacharjee
 */
public class ExecutionSummaryHelper {

    private Map<String, TradeGrouping> generateExecutionSummary( Collection formattedTrades, boolean isTermCcy ) {
        List<Map> formattedTradeList = ( List<Map> ) formattedTrades;
        Map<String, TradeGrouping> amountByCptyMap = new HashMap<>();

        for ( Map map : formattedTradeList )
        {
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
        }
        return amountByCptyMap;
    }

}
