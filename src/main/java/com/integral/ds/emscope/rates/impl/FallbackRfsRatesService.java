package com.integral.ds.emscope.rates.impl;

import com.integral.ds.emscope.rates.RFSRate;
import com.integral.ds.emscope.rates.RFSRatesException;
import com.integral.ds.emscope.rates.RFSRatesService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Fallback rfs rates service.
 *
 * @author Rahul Bhattacharjee
 */
public class FallbackRfsRatesService implements RFSRatesService {

    private static final Logger LOGGER = Logger.getLogger(FallbackRfsRatesService.class);

    private RFSRatesService primaryService;
    private RFSRatesService secondaryService;

    @Override
    public List<RFSRate> getRates(String transactionId) throws RFSRatesException {
        List<RFSRate> result = null;
        try {
            result = primaryService.getRates(transactionId);
        }catch (Exception e) {
            LOGGER.error("Exception while using primary service for " + transactionId);
        }
        if(result == null || result.isEmpty()) {
            LOGGER.info("Using secondard rfs service for " + transactionId);
            return secondaryService.getRates(transactionId);
        }
        return result;
    }

    public void setPrimaryService(RFSRatesService primaryService) {
        this.primaryService = primaryService;
    }

    public void setSecondaryService(RFSRatesService secondaryService) {
        this.secondaryService = secondaryService;
    }
}
