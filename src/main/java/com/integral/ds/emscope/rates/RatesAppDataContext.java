package com.integral.ds.emscope.rates;

import com.integral.ds.cassandra.RateUpdateInfoDao;
import com.integral.ds.dto.ProviderStream;
import com.integral.ds.emscope.RestServiceRuntimeException;
import com.integral.ds.model.ErrorObject;
import com.integral.ds.util.DataQueryUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.integral.ds.util.demo.DemoUtility;
import org.apache.log4j.Logger;

public class RatesAppDataContext {

    private static final Logger LOGGER = Logger.getLogger(RatesAppDataContext.class);

    private static final String RATES_NOT_AVAILABLE = "Tick Data not available for the requested time frame.";
    private static final String RATES_NOT_AVAILABLE_IN_DATASTORE_BATCH = "Stream Data not available for the time frame in the data store (S3).";
    private static final String RATES_NOT_AVAILABLE_IN_DATASTORE_REALTIME = "Stream Data not available for the time frame in the data store (RTRR).";

    private RatesService ratesService;
    private RatesService realtimeRatesService;
    private RateUpdateInfoDao updateInfoDao;

    public List<RestRatesObject> getRatesByTimeStampFromS3Files(String provider, String ccyPair, String stream,Date startTime, Date endTime,String b_r) {
        Date lookBackStartDate = new Date(startTime.getTime());
        List<RestRatesObject> listOfRates = null;
        List<RestRatesObject> finalRateList = new ArrayList<>();

        if(b_r.equalsIgnoreCase("All")) {
            try {
                listOfRates = this.getRatesByTimeStampFromS3Files(provider,ccyPair,stream,startTime,endTime,"Realtime");
            } catch (Exception e) {
                listOfRates = this.getRatesByTimeStampFromS3Files(provider,ccyPair,stream,startTime,endTime,"Batch");
            }
            //if(listOfRates.isEmpty()) {
            //}
        } else if(b_r.equalsIgnoreCase("Realtime")) {
            try {
                listOfRates = realtimeRatesService.getRates(provider, stream, ccyPair, lookBackStartDate, endTime);
            } catch (Exception e) {
                LOGGER.error("Exception while fetching rates from realtime dao.",e);
                throw new RestServiceRuntimeException(getErrorObjectForRealtime(RATES_NOT_AVAILABLE_IN_DATASTORE_REALTIME, provider, stream, ccyPair, startTime, endTime, ErrorObject.ErrorType.error), "No rates for this period in datastore.");
            }
        } else {
            try {
                listOfRates = ratesService.getRates(provider, stream, ccyPair, lookBackStartDate, endTime);
            }catch (Exception e) {
                LOGGER.error("Exception while fetching rates from S3 dao.",e);
                throw new RestServiceRuntimeException(getErrorObject(RATES_NOT_AVAILABLE_IN_DATASTORE_BATCH, provider, stream, ccyPair, startTime, endTime, ErrorObject.ErrorType.error), "No rates for this period in datastore.");
            }
        }

        if(listOfRates.isEmpty()) {
            ErrorObject errorObject = null;
            if("Batch".equalsIgnoreCase(b_r)) {
                errorObject = getErrorObject(RATES_NOT_AVAILABLE,provider, stream, ccyPair, startTime, endTime, ErrorObject.ErrorType.warning);
            } else {
                errorObject = getErrorObjectForRealtime(RATES_NOT_AVAILABLE,provider, stream, ccyPair, startTime, endTime, ErrorObject.ErrorType.warning);
            }
            throw new RestServiceRuntimeException(errorObject, "No rates for this period.");
        }

        Map<Integer, List<RestRatesObject>> ratesGroupedByTier = new HashMap<Integer, List<RestRatesObject>>();
        for (RestRatesObject rateItem : listOfRates) {
            List<RestRatesObject> rateListForTier = ratesGroupedByTier.get(rateItem.getLvl());
            if (rateListForTier == null) {
                rateListForTier = new ArrayList<RestRatesObject>();
                ratesGroupedByTier.put(rateItem.getLvl(), rateListForTier);
            }
            rateListForTier.add(rateItem);
        }

        Set<Integer> tierSrt = new TreeSet<Integer>(ratesGroupedByTier.keySet());

        for (Integer tier : tierSrt) {
            List<RestRatesObject> ratesForTier = ratesGroupedByTier.get(tier);
            Collections.sort(ratesForTier, Collections.reverseOrder());
            for (RestRatesObject rate : ratesForTier) {

                Date timeStamp = new Date(rate.getTmstmp().longValue());
                if (timeStamp.compareTo(startTime) > 0) {
                    finalRateList.add(rate);
                } else {
                    rate.setTmstmp(startTime.getTime());
                    finalRateList.add(rate);
                    break;
                }
            }
        }
        Collections.sort(finalRateList);
        return finalRateList;
    }

    private ErrorObject getErrorObjectForRealtime(String message, String provider, String stream, String ccyPair, Date startTime, Date endTime, ErrorObject.ErrorType type) {
        Object [] params = new Object[] {provider,stream};
        List<String> lastUpdated = updateInfoDao.getRows(params);
        StringBuilder stringBuilder = new StringBuilder();

        ProviderStream providerStream = new ProviderStream(provider, stream);
        if(DemoUtility.isDemoMode()) {
            providerStream = DemoUtility.getMaskedProviderStream(providerStream);
        }

        if(lastUpdated.isEmpty()) {
            stringBuilder.append("Ticks from this stream (" + providerStream.getProvider() + "/" + providerStream.getStream() + ") is not available in the data store.");
        } else {
            stringBuilder.append("Ticks from this stream (" + providerStream.getProvider() + "/" + providerStream.getStream() + ") was last updated on " + lastUpdated.get(0));
        }

        ErrorObject errorObject = new ErrorObject();
        errorObject.setErrorMessage(stringBuilder.toString());
        errorObject.setType(type);
        return errorObject;
    }

    private ErrorObject getErrorObject(String message, String provider, String stream, String ccyPair, Date startTime, Date endTime, ErrorObject.ErrorType type) {
        String date = DataQueryUtils.getNearestAvailableRates(provider, stream, ccyPair, startTime);
        ProviderStream providerStream = new ProviderStream(provider, stream);
        if(DemoUtility.isDemoMode()) {
            providerStream = DemoUtility.getMaskedProviderStream(providerStream);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message);
        stringBuilder.append("Stream : " + providerStream.getStream());
        stringBuilder.append(" ,Provider : " + providerStream.getProvider() + " ,Start time : " + startTime);
        stringBuilder.append(" ,End time : " + endTime + " ,currency pair : " + ccyPair + ".");
        if (date != null) {
            stringBuilder.append("Last available rate at " + date);
        }
        ErrorObject errorObject = new ErrorObject();
        errorObject.setErrorMessage(stringBuilder.toString());
        errorObject.setType(type);
        return errorObject;
    }

    public void setRatesService(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    public void setRealtimeRatesService(RatesService realtimeRatesService) {
        this.realtimeRatesService = realtimeRatesService;
    }

    public void setUpdateInfoDao(RateUpdateInfoDao updateInfoDao) {
        this.updateInfoDao = updateInfoDao;
    }
}
