package com.integral.ds.emscope.orders.narrative;

import com.integral.ds.dto.Order;
import com.integral.ds.dto.TradeInfo;

import java.util.List;

/**
 * Created by bhattacharjeer on 5/12/2016.
 */
public interface NarrativeGenerator {
    public String generateNarrativeForOrder(Order order,List<TradeInfo> tradeInfo);
}
