package com.integral.ds.emscope.rates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Quotes , collection of rates arrived in the same milli second.
 *
 * @author Rahul Bhattacharjee
 */
public class Quote {

    private List<RestRatesObject> rates = new ArrayList<RestRatesObject>();
    private Date sampleTime;

    public void addRates(Collection rate) {
        rates.addAll(rate);
    }

    public void addRate(RestRatesObject rate) {
        rates.add(rate);
    }

    public List<RestRatesObject> getRates(){
        return rates;
    }

    public boolean isEmpty() {
        return this.rates.isEmpty();
    }

    public Date getQuoteTime() {
        if(!isEmpty()) {
            // return the time of the last rate.
            return new Date(rates.get((rates.size()-1)).getTmstmp());
        }
        return null;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date date) {
        this.sampleTime = date;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("=== Quote sample time " + sampleTime + "=== \n");
        int count = 0;
        for(RestRatesObject rate : rates) {
            stringBuilder.append("Rate " + (count++) + " => " + rate);
        }
        return stringBuilder.toString();
    }
}
