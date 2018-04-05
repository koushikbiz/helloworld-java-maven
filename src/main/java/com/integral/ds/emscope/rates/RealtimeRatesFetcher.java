package com.integral.ds.emscope.rates;

import com.integral.ds.cassandra.RealtimeRecordTransformer;
import com.integral.ds.dto.RealTimeRates;
import com.integral.ds.s3.RecordTransformer;

import java.util.Date;
import java.util.List;

/**
 * Created by bhattacharjeer on 5/24/2017.
 */
public interface RealtimeRatesFetcher {
    RecordTransformer<List<RestRatesObject>> TRANSFORMER = new RealtimeRecordTransformer();
    List<RealTimeRates> getRates(String provider, String stream, String ccyPair, Date fromTime, Date endTime);
}
