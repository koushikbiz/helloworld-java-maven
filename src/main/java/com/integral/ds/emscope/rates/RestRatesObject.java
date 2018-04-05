package com.integral.ds.emscope.rates;

import com.integral.ds.dto.QuoteType;
import com.integral.ds.representation.Representation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Rates objects containing only the required fields.
 *
 * @author Rahul Bhattacharjee
 */
public class RestRatesObject implements Comparable<RestRatesObject> {

    private Long tmstmp;
    private java.math.BigDecimal bidPrice;
    private java.math.BigDecimal bidSize;
    private java.math.BigDecimal askPrice;
    private java.math.BigDecimal askSize;
    private String status;
    private String guid;
    private Integer tier;
    private String ccyPair;
    private String metaInfo;
    private QuoteType quoteType;
    private Map<String,String> additionalInformation = null;
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
    public java.math.BigDecimal getAsk_price() {
        return askPrice;
    }

    @Representation(names = {"COMPLETE", "SUMMARY"})
    public java.math.BigDecimal getAsk_size() {
        return askSize;
    }

    @Representation(names = {"COMPLETE", "SUMMARY"})
    public java.math.BigDecimal getBid_price() {
        return bidPrice;
    }

    @Representation(names = {"COMPLETE", "SUMMARY"})
    public java.math.BigDecimal getBid_size() {
        return bidSize;
    }

    @Representation(names = {"COMPLETE", "SUMMARY"})
    public java.lang.String getGuid() {
        return guid;
    }


    @Representation(names = {"COMPLETE", "SUMMARY"})
    public java.lang.Integer getLvl() {
        return tier;
    }

    @Representation(names = {"COMPLETE", "SUMMARY"})
    public java.lang.String getStatus() {
        return status;
    }

    @Representation(names = {"COMPLETE", "SUMMARY"})
    public Long getTmstmp() {
        return tmstmp;

    }

    public String getMetaInfo()
    {
    	return metaInfo;
    }
    
    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    //End Of Getters.
    //Start of Setters.
    public void setTmstmp(Long tmstmp) {
        this.tmstmp = tmstmp;
    }

    public void setMetaInfo(String metaInfo)
    {
    	this.metaInfo = metaInfo;
    }
    
    public void setBidPrice(java.math.BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }
    public void setBidSize(java.math.BigDecimal bidSize) {
        this.bidSize = bidSize;
    }
    public void setAskPrice(java.math.BigDecimal askPrice) {
        this.askPrice = askPrice;
    }
    public void setAskSize(java.math.BigDecimal askSize) {
        this.askSize = askSize;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public QuoteType getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(QuoteType quoteType) {
        this.quoteType = quoteType;
    }

    public Map<String, String> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, String> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public int compareTo(RestRatesObject o) {
    	if(this.tmstmp == o.tmstmp)
    		{
    		return this.tier.compareTo(o.tier);
    		}
        return this.tmstmp.compareTo(o.getTmstmp());
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.y HH:mm:ss.SSS");
        return "RestRatesObject{" +
                "tmstmp=" + dateFormat.format(new Date(tmstmp)) +
                ", bidPrice=" + bidPrice +
                ", bidSize=" + bidSize +
                ", askPrice=" + askPrice +
                ", askSize=" + askSize +
                ", status='" + status + '\'' +
                ", guid='" + guid + '\'' +
                ", tier=" + tier +
                ", ccyPair='" + ccyPair + '\'' +
                ", metaInfo='" + metaInfo + '\'' +
                ", quoteType=" + quoteType +
                ", additionalInformation=" + additionalInformation +
                '}';
    }
}
