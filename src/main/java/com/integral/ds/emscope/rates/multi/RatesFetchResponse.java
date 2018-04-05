package com.integral.ds.emscope.rates.multi;

import com.integral.ds.emscope.rates.RestRatesObject;

import java.util.List;

/**
 * Rates fetch response.
 *
 * @author Rahul Bhattacharjee
 */
public class RatesFetchResponse {

    private RatesFetchInput rateFetchInput;
    private List<RestRatesObject> ratesObjectList;

    public RatesFetchInput getRateFetchInput() {
        return rateFetchInput;
    }

    public void setRateFetchInput(RatesFetchInput rateFetchInput) {
        this.rateFetchInput = rateFetchInput;
    }

    public List<RestRatesObject> getRatesObjectList() {
        return ratesObjectList;
    }

    public void setRatesObjectList(List<RestRatesObject> ratesObjectList) {
        this.ratesObjectList = ratesObjectList;
    }

    public boolean isEmpty() {
        if(ratesObjectList != null) return ratesObjectList.isEmpty();
        return false;
    }
}
