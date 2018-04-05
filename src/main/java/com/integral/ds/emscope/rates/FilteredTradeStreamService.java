package com.integral.ds.emscope.rates;

import com.integral.ds.dto.TradeStreamDetails;
import com.integral.ds.emscope.rates.multi.MultiStreamRatesFetcher;
import com.integral.ds.emscope.rates.multi.RatesFetchInput;
import com.integral.ds.emscope.rates.multi.RatesFetchResponse;
import com.integral.ds.emscope.trades.meta.TradeProviderStreamInfoService;
import com.integral.ds.s3.Filter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author Rahul Bhattacharjee.
 */
public class FilteredTradeStreamService {

    private  final Log LOGGER = LogFactory.getLog(FilteredTradeStreamService.class);

    private TradeProviderStreamInfoService tradeProviderStreamInfoService;
    private MultiStreamRatesFetcher multiStreamRatesFetcher;

    public Map<String,List<RestRatesObject>> getFilteredRates(List<String> tradeIds,Date start, Date end) {
        Map<String,TradeStreamDetails> tradeStreamDetailsMap = tradeProviderStreamInfoService.getStreamDetailsForTrades(tradeIds);
        Iterator<TradeStreamDetails> tradeStreamDetailsIterator = tradeStreamDetailsMap.values().iterator();

        List<RatesFetchInput> parameters = new ArrayList<RatesFetchInput>();
        while(tradeStreamDetailsIterator.hasNext()) {
            TradeStreamDetails tradeStreamDetail = tradeStreamDetailsIterator.next();
            RatesFetchInput input = new RatesFetchInput();
            input.setProvider(tradeStreamDetail.getProvider());
            input.setStream(tradeStreamDetail.getStream());
            input.setCcyPair(tradeStreamDetail.getCcyPair());
            input.setStart(start);
            input.setEnd(end);
            input.setTier(tradeStreamDetail.getTier());
            input.setFilter(new TierBasedFilter(tradeStreamDetail.getTier()));
            parameters.add(input);
        }

        List<RatesFetchResponse> responses = multiStreamRatesFetcher.getRates(parameters);
        Map<String,List<RestRatesObject>> result = new HashMap<String,List<RestRatesObject>>();
        for(RatesFetchResponse response : responses) {
            if(!response.isEmpty()) {
                RatesFetchInput input = response.getRateFetchInput();
                result.put(input.getStreamUniqueName(),response.getRatesObjectList());
            }
        }
        return result;
    }

    private static class TierBasedFilter implements Filter<RestRatesObject> {
        private int tier;

        private TierBasedFilter(int tier) {
            this.tier = tier;
        }

        @Override
        public boolean shouldFilter(RestRatesObject obj) {
            if(obj == null) return true;
            return tier != (obj.getLvl().intValue());
        }
    }


    public void setTradeProviderStreamInfoService(TradeProviderStreamInfoService tradeProviderStreamInfoService) {
        this.tradeProviderStreamInfoService = tradeProviderStreamInfoService;
    }

    public void setMultiStreamRatesFetcher(MultiStreamRatesFetcher multiStreamRatesFetcher) {
        this.multiStreamRatesFetcher = multiStreamRatesFetcher;
    }
}
