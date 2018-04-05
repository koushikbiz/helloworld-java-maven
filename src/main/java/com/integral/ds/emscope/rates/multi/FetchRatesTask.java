package com.integral.ds.emscope.rates.multi;

import com.integral.ds.dao.s3.S3FilesRatesDao;
import com.integral.ds.emscope.rates.RatesService;
import com.integral.ds.emscope.rates.RestRatesObject;
import com.integral.ds.s3.Filter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Task to fetch rates for a small chunk.
 * @author Rahul Bhattacharjee
 *
 */
public class FetchRatesTask implements Callable<RatesFetchResponse> {

    private RatesFetchInput input;
    private RatesService ratesService;

    public FetchRatesTask(RatesFetchInput input, RatesService ratesService) {
        this.input = input;
        this.ratesService = ratesService;
    }

    @Override
    public RatesFetchResponse call() throws Exception {
        String provider = input.getProvider();
        String stream = input.getStream();
        String ccyPair = input.getCcyPair();
        Date start = input.getStart();
        Date end = input.getEnd();
        Filter<RestRatesObject> filter = input.getFilter();

        List<RestRatesObject> rates = ratesService.getRates(provider,stream,ccyPair,start,end);
        rates = filter(rates,filter);
        RatesFetchResponse response = new RatesFetchResponse();
        response.setRateFetchInput(input);
        response.setRatesObjectList(rates);
        return response;
    }

    private List<RestRatesObject> filter(List<RestRatesObject> rates, Filter<RestRatesObject> filter) {
        if(filter != null) {
            List<RestRatesObject> filteredRates = new ArrayList<RestRatesObject>();
            for(RestRatesObject rate : rates) {
                if(!filter.shouldFilter(rate)) {
                    filteredRates.add(rate);
                }
            }
            return filteredRates;
        }
        return rates;
    }
}
