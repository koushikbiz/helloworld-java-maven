package com.integral.ds.emscope.rates;

import com.integral.ds.dto.UhfbmRate;

import java.util.Date;
import java.util.List;

/**
 * Ultra high frequency benchmark.
 *
 * @author Rahul Bhattacharjee
 */
public interface UHFBMService {

    public List<UhfbmRate> getUhfbm(String ccyPair,Date start,Date end);

}
