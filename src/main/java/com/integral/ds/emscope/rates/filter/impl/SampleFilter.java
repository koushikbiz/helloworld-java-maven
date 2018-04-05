package com.integral.ds.emscope.rates.filter.impl;

import com.integral.ds.emscope.rates.RateRule;
import com.integral.ds.emscope.rates.RestRatesObject;

import java.util.*;

/**
 *
 * Sampled filter for rates.
 *
 * @author Rahul Bhattacharjee
 * @param <T>
 */
public class SampleFilter<T> implements RateRule<RestRatesObject>{

    private int sample;
    public final static long NON_SAMPLE_DURATION = 11 * 60 * 1000;
    public final static int TOTAL_SAMPLE  = 720;

    @Override
    public void init(List<String> params) {
        if(params != null && !params.isEmpty()) {
            sample = Integer.parseInt(params.get(0));
        } else {
            sample = TOTAL_SAMPLE;
        }
    }

    @Override
    public List<RestRatesObject> filter(List<RestRatesObject> input) {
        if(input.isEmpty()) return input;
        Collections.sort(input);
        long start = input.get(0).getTmstmp();
        long end = input.get(input.size() - 1).getTmstmp();
        List<List<RestRatesObject>> sampledAndGrouped = SamplingUtility.sample(groupedRates(input), start, end);
        List<RestRatesObject> result = new ArrayList<RestRatesObject>();
        for(List<RestRatesObject> rate : sampledAndGrouped) {
            result.addAll(rate);
        }
        return result;
    }

    List<List<RestRatesObject>> groupedRates(List<RestRatesObject> input) {
        List<List<RestRatesObject>> result = new ArrayList<List<RestRatesObject>>();
        Map<Long,List<RestRatesObject>> group = new TreeMap<Long,List<RestRatesObject>>();

        for(RestRatesObject rate : input) {
            long time = rate.getTmstmp();
            List<RestRatesObject> value = group.get(time);
            if(value == null) {
                value = new ArrayList<>();
                group.put(time,value);
            }
            value.add(rate);
        }
        Iterator<Long> iterator = group.keySet().iterator();
        while(iterator.hasNext()) {
            result.add(group.get(iterator.next()));
        }
        return result;
    }

    @Override
    public String toString() {
        return "SampleFilter{" +
                "param=" + sample +
                '}';
    }
}
