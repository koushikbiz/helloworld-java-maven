package com.integral.ds.emscope.rates.multi;

import com.google.common.util.concurrent.ListenableFuture;
import com.integral.ds.dao.s3.S3FilesRatesDao;
import com.integral.ds.emscope.common.Pipeline;
import com.integral.ds.emscope.rates.RatesService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Fetches rates using multiple threads.
 *
 * @author Rahul Bhattacharjee
 */
public class MultiStreamRatesFetcher {

    private static final Logger LOGGER = Logger.getLogger(MultiStreamRatesFetcher.class);

    private Pipeline pipeline;
    private RatesService ratesService;

    public List<RatesFetchResponse> getRates(List<RatesFetchInput> inputs) {
        List<ListenableFuture<RatesFetchResponse>> futures = new ArrayList<ListenableFuture<RatesFetchResponse>>();
        for(RatesFetchInput input : inputs) {
            futures.add(pipeline.submit(new FetchRatesTask(input,ratesService)));
        }
        List<RatesFetchResponse> responses = getResponsesWhenDone(futures);
        return responses;
    }

    private List<RatesFetchResponse> getResponsesWhenDone(List<ListenableFuture<RatesFetchResponse>> futures) {
        List<RatesFetchResponse> result = new ArrayList<RatesFetchResponse>();
        for(ListenableFuture<RatesFetchResponse> response : futures) {
            try {
                result.add(response.get());
            } catch (Exception e) {
                LOGGER.error("Exception while fetching response.",e);
            }
        }
        return result;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void setRatesService(RatesService ratesService) {
        this.ratesService = ratesService;
    }
}
