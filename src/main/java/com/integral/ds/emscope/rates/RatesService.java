package com.integral.ds.emscope.rates;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Rates service interface.
 *
 * @author Rahul Bhattacharjee
 */
public interface RatesService<T> {

    public List<T> getRates(String provider, String stream, String ccyPair, Date fromTime, Date endTime);

    public Map<String,List<T>> getRates(String provider, String stream, List<String> ccyPairs, Date fromTime, Date endTime);
}
