package com.integral.ds.emscope.rates;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Combined rates service which would use both the service from S3 and Chronicle.
 * Will use chronicle rates service first , if no rates are returned then it would fallback to
 * S3 rates service. It will be useful during transition phase.
 *
 * @author Rahul Bhattacharjee
 */
public class CombinedRatesService implements RatesService {

    private static final Logger LOGGER = Logger.getLogger(CombinedRatesService.class);

    private RatesService primary;
    private RatesService secondary;

    @Override
    public List<RestRatesObject> getRates(String provider, String stream, String ccyPair, Date fromTime, Date endTime) {
        List<RestRatesObject> result = null;
        try {
            result = primary.getRates(provider,stream,ccyPair,fromTime,endTime);
        } catch (Exception e) {
            LOGGER.error("Exception while getting rates from primary rates service.",e);
        }
        // fallback to secondary rates service.
        if(result == null || result.isEmpty()) {
            LOGGER.info("Primary rates didn't return any result , so falling back to secondary service.");
            return secondary.getRates(provider,stream,ccyPair,fromTime,endTime);
        } else {
            LOGGER.debug("Rates returned from primary service " + result.size());
            return result;
        }
    }

    @Override
    public Map getRates(String provider, String stream, List ccyPairs, Date fromTime, Date endTime) {
        Map<String,RestRatesObject> result = null;
        try {
            result = primary.getRates(provider,stream,ccyPairs,fromTime,endTime);
        } catch (Exception e) {
            LOGGER.error("Exception while getting rates from primary rates service.",e);
        }
        // fallback to secondary rates service.
        if(result == null || result.isEmpty()) {
            LOGGER.info("Primary rates didn't return any result , so falling back to secondary service.");
            return secondary.getRates(provider,stream,ccyPairs,fromTime,endTime);
        } else {
            LOGGER.debug("Rates returned from primary service " + result.size());
            return result;
        }
    }

    public void setPrimary(RatesService primary) {
        this.primary = primary;
    }

    public void setSecondary(RatesService secondary) {
        this.secondary = secondary;
    }
}
