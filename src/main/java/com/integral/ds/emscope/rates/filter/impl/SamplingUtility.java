package com.integral.ds.emscope.rates.filter.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Sampling utility.
 *
 * @author Rahul Bhattacharjee
 */
public class SamplingUtility {

    public static <T> List<T> sample(List<T> input , long start, long end) {
        if(input == null || input.isEmpty() || !SamplingUtility.isExceedingTimeRange(start,end)) return input;

        if(input.size() > SampleFilter.TOTAL_SAMPLE) {
            int gap = input.size() / SampleFilter.TOTAL_SAMPLE;
            if(gap > 0) {
                List<T> result = new ArrayList<T>();
                for(int i = 0 ; i < input.size() ; i++) {
                    if((i%gap)== 0) result.add(input.get(i));
                }
                return result;
            }
        }
        return input;
    }

    public static boolean isExceedingTimeRange(long fromtime, long toTime) {
        return (Math.abs(toTime - fromtime) > SampleFilter.NON_SAMPLE_DURATION) ? true : false;
    }
}
