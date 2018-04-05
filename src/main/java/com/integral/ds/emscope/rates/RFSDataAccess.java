package com.integral.ds.emscope.rates;

import java.util.List;

/**
 * Created by bhattacharjeer on 8/27/2016.
 */
public interface RFSDataAccess {
    public List<RFSRate> getRfsRates(String tradeId);

    public void insert(List<ExtRFSRate> eRates);
}
