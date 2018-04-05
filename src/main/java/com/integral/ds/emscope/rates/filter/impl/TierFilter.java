package com.integral.ds.emscope.rates.filter.impl;

import com.integral.ds.emscope.rates.RateRule;
import com.integral.ds.emscope.rates.RestRatesObject;
import com.integral.ds.s3.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Filters every tier other than whats specified in the constructor.
 *
 * @author Rahul Bhattacharjee
 */
public class TierFilter implements RateRule<RestRatesObject> {

    private List<Integer> allowedTiers;
    private boolean isAllowAll;

    public TierFilter() {}

    @Override
    public void init(List<String> params) {
        List<Integer> tiers = new ArrayList<Integer>();
        if(checkIfAllowAllTiers(params)) {
            isAllowAll = true;
        } else {
            for (String param : params) {
                try {
                    tiers.add(Integer.parseInt(param));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.allowedTiers = tiers;
        }
    }

    private boolean checkIfAllowAllTiers(List<String> params) {
        if(params != null && !params.isEmpty() && params.size() == 1){
            String param = params.get(0);
            if("*".equalsIgnoreCase(param)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<RestRatesObject> filter(List<RestRatesObject> input) {
        if(isAllowAll) return input;
        List<RestRatesObject> result = new ArrayList<RestRatesObject>();
        for(RestRatesObject rate : input) {
            if(!shouldFilter(rate)) result.add(rate);
        }
        return result;
    }

    private boolean shouldFilter(RestRatesObject obj) {
        int level = obj.getLvl().intValue();
        if(allowedTiers != null) {
            return !(allowedTiers.contains(level));
        } else {
            return true;
        }
    }

    public void setAllowedTiers(List<Integer> allowedTiers) {
        this.allowedTiers = allowedTiers;
    }

    @Override
    public String toString() {
        return "TierFilter{" +
                "allowedTiers=" + allowedTiers +
                ", isAllowAll=" + isAllowAll +
                '}';
    }
}
