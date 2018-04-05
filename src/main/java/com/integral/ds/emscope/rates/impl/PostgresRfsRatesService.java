package com.integral.ds.emscope.rates.impl;

import com.integral.ds.dao.RFSRatesDao;
import com.integral.ds.emscope.rates.RFSRate;
import com.integral.ds.emscope.rates.RFSRatesException;
import com.integral.ds.emscope.rates.RFSRatesService;

import java.util.List;

/**
 * Postgres rfs service implementation.
 *
 * @author Rahul Bhattacharjee
 */
public class PostgresRfsRatesService implements RFSRatesService {

    private RFSRatesDao rfsDao;

    @Override
    public List<RFSRate> getRates(String transactionId) throws RFSRatesException {
        return rfsDao.getRfsRates(transactionId);
    }

    public void setRfsDao(RFSRatesDao rfsDao) {
        this.rfsDao = rfsDao;
    }
}
