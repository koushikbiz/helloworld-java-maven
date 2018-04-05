package com.integral.ds.emscope.orders.narrative;

import com.integral.ds.emscope.orders.narrative.impl.ESPNarrativeGeneratorImpl;
import com.integral.ds.emscope.orders.narrative.impl.OnlyTradeNarrativeGenerator;
import com.integral.ds.emscope.orders.narrative.impl.RFSNarrativeGeneratorImpl;
import com.integral.ds.emscope.orderswithtrade.WorkflowTypeEnum;

/**
 * Created by bhattacharjeer on 5/12/2016.
 */
public class NarrativeGeneratorFactory {

    private static final NarrativeGenerator ESP_GENERATOR = new ESPNarrativeGeneratorImpl();
    private static final NarrativeGenerator RFS_GENERATOR = new RFSNarrativeGeneratorImpl();
    private static final NarrativeGenerator ONE_TRADE_GENERATOR = new OnlyTradeNarrativeGenerator();

    public static NarrativeGenerator getNarrativeGenerator(WorkflowTypeEnum type) {
        switch(type) {
            case ESP: return ESP_GENERATOR;
            case RFS: return RFS_GENERATOR;
            case ONE_TRADE_NO_ORDER: return ONE_TRADE_GENERATOR;
            default:  return ESP_GENERATOR;
        }
    }
}
