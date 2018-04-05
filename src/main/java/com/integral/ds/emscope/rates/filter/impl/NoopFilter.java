package com.integral.ds.emscope.rates.filter.impl;

import com.integral.ds.emscope.rates.RateRule;
import com.integral.ds.emscope.rates.RestRatesObject;

import java.util.List;

/**
 * Created by bhattacharjeer on 4/27/2016.
 */
public class NoopFilter implements RateRule<RestRatesObject> {
    @Override
    public void init(List<String> params) {}

    @Override
    public List<RestRatesObject> filter(List<RestRatesObject> input) {
        return input;
    }
}
