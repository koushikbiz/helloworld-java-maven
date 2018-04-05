package com.integral.ds.emscope.rates;

import java.util.List;

/**
 * Created by bhattacharjeer on 7/12/2016.
 */
public class ExtRFSRate {
    private String transactionId;
    private List<RFSRate> rates;

    public ExtRFSRate(String transactionId, List<RFSRate> rates) {
        this.transactionId = transactionId;
        this.rates = rates;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<RFSRate> getRates() {
        return rates;
    }

    public void setRates(List<RFSRate> rates) {
        this.rates = rates;
    }
}
