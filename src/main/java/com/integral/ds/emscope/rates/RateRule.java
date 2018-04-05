package com.integral.ds.emscope.rates;

import java.util.List;

/**
 * Rate rule interface. For specifying rules for post rate processing.
 *
 * @author Rahul Bhattacharjee
 */
public interface RateRule<T> {

    /**
     * The framework will populate the parameters required for processing of this rule.
     * @param params
     */
    public void init(List<String> params);

    /**
     * Logic of the rule.
     * @param input
     * @return
     */
    public List<T> filter(List<T> input);
}
