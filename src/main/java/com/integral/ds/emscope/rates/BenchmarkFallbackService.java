package com.integral.ds.emscope.rates;

import com.integral.ds.dao.S3FilesBenchmarkService;
import com.integral.ds.dto.BenchmarkRate;
import com.integral.ds.dto.ProviderStream;
import com.integral.ds.emscope.benchmark.BenchmarkAppDataContext;
import com.integral.ds.util.PropertyReader;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by bhattacharjeer on 6/15/2017.
 */
public class BenchmarkFallbackService {

    private static final Logger LOGGER = Logger.getLogger(BenchmarkFallbackService.class);

    public enum FallbackOrder {Batch,Realtime,All}

    private S3FilesBenchmarkService s3BenchmarkService;
    private BenchmarkAppDataContext tableBenchmarkService;
    private RatesService realtimeRatesService;
    private RatesService batchRates;
    private Set<String> CRYPTO_CURRENCYS = null;

    private final ProviderStream CRYPTO = new ProviderStream("CCXB","CryptoB");
    private final ProviderStream NON_CRYPTO = new ProviderStream("FXB","Benchmark3");

    public BenchmarkFallbackService() {
        this.CRYPTO_CURRENCYS = PropertyReader.getCryptoList();
    }

    /**
     * We should always use this benchmark call. It has fallback.
     *
     * @param ccyPair
     * @param fromTime
     * @param toTime
     * @return
     */
    public List<BenchmarkRate> getBenchmarkRate(String ccyPair, long fromTime, long toTime,FallbackOrder order) {
        switch (order) {
            case All:
            case Realtime: {
                try {
                    ProviderStream providerStream = getProviderStream(ccyPair);
                    List<RestRatesObject> rtRates = this.realtimeRatesService.getRates(providerStream.getProvider(), providerStream.getStream(), ccyPair, new Date(fromTime), new Date(toTime));
                    if (!isEmpty(rtRates)) {
                        return transformRates(ccyPair, rtRates);
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception while fetching realtime BM rates",e);
                }
            }
            case Batch: {
                try {
                    if(isCrypto(ccyPair)) {
                        List<RestRatesObject> rates = this.batchRates.getRates(CRYPTO.getProvider(),CRYPTO.getStream(),ccyPair,new Date(fromTime), new Date(toTime));
                        if (!isEmpty(rates)) {
                            return transformRates(ccyPair, rates);
                        }
                    }  else {
                        List<BenchmarkRate> rates = this.s3BenchmarkService.getBenchmark(ccyPair, new Date(fromTime), new Date(toTime));
                        if (!isEmpty(rates)) {
                            return rates;
                        } else {
                            rates = this.tableBenchmarkService.getBenchmarkRateObjects(ccyPair, new Date(fromTime), new Date(toTime), 1);
                            if (!isEmpty(rates)) {
                                return rates;
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Exception while fetching batch BM rates",e);
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    private boolean isCrypto(String ccyPair) {
        ccyPair = ccyPair.replace("/","");
        return CRYPTO_CURRENCYS.contains(ccyPair);
    }

    private ProviderStream getProviderStream(String ccyPair) {
        ccyPair = ccyPair.replace("/","");
        return CRYPTO_CURRENCYS.contains(ccyPair) ? CRYPTO : NON_CRYPTO;
    }

    private boolean isEmpty(List list) {
        return list == null ? true : list.isEmpty();
    }

    private List<BenchmarkRate> transformRates(String ccyPair, List<RestRatesObject> rtRates) {
        List<BenchmarkRate> result = new ArrayList<BenchmarkRate>();
        for(RestRatesObject rate : rtRates) {
            BenchmarkRate bm = new BenchmarkRate();
            bm.setTier(1);
            bm.setTimeStamp(rate.getTmstmp());
            bm.setMidPriceMedian(rate.getAsk_price().doubleValue());
            bm.setMidPriceMean(rate.getAsk_price().doubleValue());
            bm.setCurrencyPair(ccyPair);
            result.add(bm);
        }
        return result;
    }

    public void setS3BenchmarkService(S3FilesBenchmarkService s3BenchmarkService) {
        this.s3BenchmarkService = s3BenchmarkService;
    }

    public void setTableBenchmarkService(BenchmarkAppDataContext tableBenchmarkService) {
        this.tableBenchmarkService = tableBenchmarkService;
    }

    public void setRealtimeRatesService(RatesService realtimeRatesService) {
        this.realtimeRatesService = realtimeRatesService;
    }

    public void setBatchRates(RatesService batchRates) {
        this.batchRates = batchRates;
    }
}
