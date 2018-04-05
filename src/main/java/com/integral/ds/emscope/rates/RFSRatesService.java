package com.integral.ds.emscope.rates;

import java.util.List;

/**
 * @author Rahul Bhattacharjee
 */
public interface RFSRatesService {
    public List<RFSRate> getRates(String transactionId) throws RFSRatesException;
}
