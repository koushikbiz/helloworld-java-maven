package com.integral.ds.emscope.rates;

import com.integral.ds.dto.RatePriceSizeQuote;

import java.util.Date;
import java.util.List;

/**
 * Rate price at size service.
 *
 * @author Rahul Bhattacharjee
 */
public interface RatePriceAtSizeService {

    public List<RatePriceSizeQuote> getPriceAtSizeRates(String provider, String stream, String ccyPair, List<Long> volumes, Date fromTime, Date endTime);
}
