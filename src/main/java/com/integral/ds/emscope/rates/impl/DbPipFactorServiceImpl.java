package com.integral.ds.emscope.rates.impl;

import com.integral.ds.dao.PipFactorDao;
import com.integral.ds.emscope.rates.PipFactorService;

import java.util.Map;

/**
 * Created by bhattacharjeer on 12/11/2016.
 */
public class DbPipFactorServiceImpl implements PipFactorService {

    private PipFactorDao pipFactorDao;
    private Map<String,Long> pipFactorCache;

    public void init(){
        pipFactorCache = pipFactorDao.getPipFactorMap();
    }

    @Override
    public Long getPipFactor(String ccyPair) {
        return pipFactorCache.get(ccyPair);
    }

    public void setPipFactorDao(PipFactorDao pipFactorDao) {
        this.pipFactorDao = pipFactorDao;
    }
}
