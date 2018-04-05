package com.integral.ds.emscope.rates.multi;

import com.integral.ds.emscope.rates.RestRatesObject;
import com.integral.ds.s3.Filter;

import java.util.Date;

/**
 * @author Rahul Bhattacharjee
 */
public class RatesFetchInput {

    private String provider;
    private String stream;
    private String ccyPair;
    private Date start;
    private Date end;
    private int tier;
    private Filter<RestRatesObject> filter;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Filter<RestRatesObject> getFilter() {
        return filter;
    }

    public void setFilter(Filter<RestRatesObject> filter) {
        this.filter = filter;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public String getStreamUniqueName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getProvider());
        stringBuilder.append("-");
        stringBuilder.append(getStream());
        stringBuilder.append("-");
        stringBuilder.append(getTier());
        return stringBuilder.toString();
    }
}
