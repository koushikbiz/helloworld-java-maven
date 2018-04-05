package com.integral.ds.emscope.rates;

import com.integral.ds.dto.BenchmarkRate;
import com.integral.ds.dto.ProviderStream;
import com.integral.ds.dto.RestRateWithBmObject;
import com.integral.ds.emscope.RestServiceRuntimeException;
import com.integral.ds.emscope.rates.filter.RatesFilterFactory;
import com.integral.ds.model.ErrorObject;
import com.integral.ds.util.DataQueryUtils;
import com.integral.ds.util.RestResponseUtil;
import com.integral.ds.util.demo.DemoUtility;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;

@Path("/rates1")
@Component
public class RatesCollectionResource1 {

    private static final Logger LOGGER = Logger.getLogger(RatesCollectionResource1.class);
    private static final String UNEXPECTED_ERROR = "Unexpected error in EMScope.";

    @Autowired
    private RatesAppDataContext ratesAppDataContext;

    @Autowired
    @Qualifier("fallback_service")
    private RFSRatesService rfsRatesService;

    @Autowired
    private BenchmarkFallbackService benchmarkFallbackService;

    @GET
    @Path("/filtered/{prvdr}/{stream}/{ccyp}/{fromtime}/{totime}/{filter_type}/{filter_value}/{mode}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRatesWithFilter(@Context HttpServletRequest request,@PathParam("prvdr") String provider, @PathParam("stream") String stream,
                                     @PathParam("ccyp") String ccyPair, @PathParam("fromtime") long fromtime, @PathParam("totime") long toTime,
                                     @PathParam("filter_type") String filterType, @PathParam("filter_value") String filterValue,@PathParam("mode") String mode) {
        Object response = null;
        try {
            response = this.getRates(provider,stream,ccyPair,fromtime,toTime,filterType,filterValue,mode);
        } catch (Exception e) {
            LOGGER.error("Exception while serializing rates object list.", e);
            if (e instanceof RestServiceRuntimeException) {
                response = ((RestServiceRuntimeException) e).getErrorObject();
            } else {
                ErrorObject errorObject = new ErrorObject();
                errorObject.setType(ErrorObject.ErrorType.error);
                errorObject.setErrorMessage(UNEXPECTED_ERROR + " Message => " + e.getMessage());
                response = errorObject;
            }
        }
        return RestResponseUtil.setJsonResponse(request,response);
    }

    @GET
    @Path("/filtered_with_bm/{prvdr}/{stream}/{ccyp}/{fromtime}/{totime}/{filter_type}/{filter_value}/{mode}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRatesWithFilterWithBenchmark(@Context HttpServletRequest request,@PathParam("prvdr") String provider, @PathParam("stream") String stream,
                                                  @PathParam("ccyp") String ccyPair, @PathParam("fromtime") long fromtime, @PathParam("totime") long toTime,
                                                  @PathParam("filter_type") String filterType, @PathParam("filter_value") String filterValue,@PathParam("mode") String mode){
        Object response = null;
        try {
            response = this.getRatesWithBM(provider,stream,ccyPair,fromtime,toTime,filterType,filterValue,mode);
        } catch (Exception e) {
            LOGGER.error("Exception while serializing rates object list.", e);
            if (e instanceof RestServiceRuntimeException) {
                response = ((RestServiceRuntimeException) e).getErrorObject();
            } else {
                ErrorObject errorObject = new ErrorObject();
                errorObject.setType(ErrorObject.ErrorType.error);
                errorObject.setErrorMessage(UNEXPECTED_ERROR + " Message => " + e.getMessage());
                response = errorObject;
            }
        }
        return RestResponseUtil.setJsonResponse(request,response);
    }

    private void postProcessRates(List<RestRatesObject> rates) {
        if(DemoUtility.isDemoMode()) {
            for(RestRatesObject rate : rates) {
                rate.setGuid("*******");
            }
        }
    }

    @GET
    @Path("/rfsstreams/{tradeid}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getRFSStream(@Context HttpServletRequest request,@PathParam("tradeid") String tradeId) {
        Object response = null;
        try {
            List<RFSRate> rates = rfsRatesService.getRates(tradeId);
            response = rates;
        } catch (Exception e) {
            e.printStackTrace();
            ErrorObject errorObject = new ErrorObject();
            errorObject.setErrorMessage(e.getMessage());
            errorObject.setType(ErrorObject.ErrorType.error);
            response = errorObject;
        }
        return RestResponseUtil.setJsonResponse(request, response);
    }

    public List<RestRatesObject> getRates(String provider, String stream, String ccyPair, long fromtime, long toTime, String filterType, String filterValue, String mode) {
        Date startTime = new Date(fromtime);
        Date endTime = new Date(toTime);
        List<RestRatesObject> response = Collections.EMPTY_LIST;
        ProviderStream providerStream = new ProviderStream(provider,stream);
        ProviderStream maskedProviderStream = DemoUtility.getUnMaskedProviderStream(providerStream);
        provider = maskedProviderStream.getProvider();
        stream = maskedProviderStream.getStream();
        List<RestRatesObject> listOfRates = ratesAppDataContext.getRatesByTimeStampFromS3Files(provider, ccyPair, stream, startTime, endTime, mode);
        List<RateRule<RestRatesObject>> rateTierFilter = RatesFilterFactory.getFilters(filterType, filterValue);
        response = RatesFilterFactory.filterRates(rateTierFilter, listOfRates);
        postProcessRates((List<RestRatesObject>) response);
        return response;
    }

    public Map<Long,RestRateWithBmObject> getRatesWithBM(String provider, String stream, String ccyPair, long fromtime, long toTime, String filterType, String filterValue, String mode) {
        ProviderStream providerStream = new ProviderStream(provider,stream);
        ProviderStream maskedProviderStream = DemoUtility.getUnMaskedProviderStream(providerStream);
        provider = maskedProviderStream.getProvider();
        stream = maskedProviderStream.getStream();
        NavigableMap<Long, BenchmarkRate> benchmarkCache = new TreeMap<Long, BenchmarkRate>();
        List<RestRatesObject> rates = this.getRates(provider, stream, ccyPair, fromtime, toTime, filterType, filterValue, mode);
        if (!rates.isEmpty()) {
            Map<Long, RestRateWithBmObject> result = new TreeMap<Long, RestRateWithBmObject>();
            long timeBuffer = DataQueryUtils.ONE_SECOND_IN_MILLI;
            List<BenchmarkRate> benchmarks = this.benchmarkFallbackService.getBenchmarkRate(ccyPair, (fromtime-timeBuffer), (toTime+timeBuffer), BenchmarkFallbackService.FallbackOrder.All);
            if (!benchmarks.isEmpty()) {
                for (BenchmarkRate bm : benchmarks) {
                    benchmarkCache.put(bm.getTimeStamp(), bm);
                }
            }
            populateWithBM(result,benchmarkCache);
            Map<Long,List<RestRatesObject>> tierRates = groupByTier(rates);
            for(Map.Entry<Long,List<RestRatesObject>> entry : tierRates.entrySet()) {
                Long time = entry.getKey();
                RestRateWithBmObject bm = result.get(time);
                if(bm == null) {
                    bm = new RestRateWithBmObject();
                    result.put(time,bm);
                }
                Long bmTime = benchmarkCache.floorKey(time);
                if (bmTime != null) {
                    BenchmarkRate bmRate = benchmarkCache.get(bmTime);
                    bm.setBenchmarkMark(new BigDecimal(bmRate.getMidPriceMedian()));
                }
                List<RestRatesObject> allTiers = entry.getValue();
                bm.setRates(allTiers);
            }
            return result;
        }
        return Collections.EMPTY_MAP;
    }

    private void populateWithBM(Map<Long, RestRateWithBmObject> result, Map<Long, BenchmarkRate> benchmarkCache) {
        Iterator<Long> iterator = benchmarkCache.keySet().iterator();
        while(iterator.hasNext()) {
            Long time = iterator.next();
            BenchmarkRate bmRate = benchmarkCache.get(time);
            RestRateWithBmObject bm = new RestRateWithBmObject();
            bm.setBenchmarkMark(new BigDecimal(bmRate.getMidPriceMedian()));
            result.put(time,bm);
        }
    }

    private Map<Long, List<RestRatesObject>> groupByTier(List<RestRatesObject> rates) {
        Map<Long,List<RestRatesObject>> result = new HashMap<Long,List<RestRatesObject>>();
        for(RestRatesObject rate : rates) {
            List<RestRatesObject> quote = result.get(rate.getTmstmp());
            if(quote == null) {
                quote = new ArrayList<RestRatesObject>();
                result.put(rate.getTmstmp(),quote);
            }
            quote.add(rate);
        }
        return result;
    }

    public void setRatesAppDataContext(RatesAppDataContext ratesAppDataContext) {
        this.ratesAppDataContext = ratesAppDataContext;
    }

    public void setRfsRatesService(RFSRatesService rfsRatesService) {
        this.rfsRatesService = rfsRatesService;
    }

    public void setBenchmarkFallbackService(BenchmarkFallbackService benchmarkFallbackService) {
        this.benchmarkFallbackService = benchmarkFallbackService;
    }
}
