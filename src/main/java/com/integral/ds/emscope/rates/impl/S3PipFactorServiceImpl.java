package com.integral.ds.emscope.rates.impl;

import com.integral.ds.commons.util.CcyPairPipMultiplyFactor;
import com.integral.ds.emscope.rates.PipFactorService;

/**
 * Created by bhattacharjeer on 12/11/2016.
 */
public class S3PipFactorServiceImpl implements PipFactorService {

    private CcyPairPipMultiplyFactor s3PipFactorService = new CcyPairPipMultiplyFactor();

    @Override
    public Long getPipFactor(String ccyPair) {
        return s3PipFactorService.getPipConversionFactor(ccyPair);
    }
}
