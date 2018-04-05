package com.integral.ds.emscope.rates.impl;

import com.integral.ds.emscope.rates.PipFactorService;

/**
 * Created by bhattacharjeer on 12/11/2016.
 */
public class ChainedPipFactorService implements PipFactorService {

    private PipFactorService primary;
    private PipFactorService secondary;

    @Override
    public Long getPipFactor(String ccyPair) {
        Long pip = primary.getPipFactor(ccyPair);
        if(pip == null) {
            pip = secondary.getPipFactor(ccyPair);
        }
        return pip == null ? -1 : pip;
    }

    public void setPrimary(PipFactorService primary) {
        this.primary = primary;
    }

    public void setSecondary(PipFactorService secondary) {
        this.secondary = secondary;
    }
}
