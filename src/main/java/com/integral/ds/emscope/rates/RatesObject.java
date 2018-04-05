package com.integral.ds.emscope.rates;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.integral.ds.representation.Representation;

/**
 * Created with IntelliJ IDEA.
 * User: vchawla
 * Date: 10/10/13
 * Time: 7:19 AM
 * To change this template use File | Settings | File Templates.
 */

@XmlRootElement
public class RatesObject implements Comparable<RatesObject> {

	private Long tmstmp;
    private Long rtEffectiveTime;
    private Long valueDate;
    private BigDecimal bidPrice;
    private BigDecimal bidSize;
    private BigDecimal askPrice;
    private BigDecimal askSize;
    private String provider;
    private String stream;
    private String ccyPair;
    private String status;
    private String guid;
    private String quoteId;
    private String server;
    private String adapter;
    private Integer tier;
    /**
     * Ideally it should be enum QuoteType defined in ProviderStreamMetaData
     * but then emscope would depend on Commons jar. As of now to avoid this
     * it is kept as String and RatesObject receiver will translate it to 
     * enum.
     * 
     * ToDo: Change it to that enum after resolving dependency. 
     */
    private String quoteType;

    @Representation(names = {"COMPLETE", "SUMMARY"})
	public java.lang.String getAdptr() {
		return adapter;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public BigDecimal getAsk_price() {
		return askPrice;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public BigDecimal getAsk_size() {
		return askSize;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public BigDecimal getBid_price() {
		return bidPrice;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public BigDecimal getBid_size() {
		return bidSize;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public String getCcyp() {
		return ccyPair;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
    public Long getEffTime() {
		return rtEffectiveTime;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public String getGuid() {
		return guid;
	}
    
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public Integer getLvl() {
		return tier;
	}
    
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public String getPrvdr() {
		return provider;
	}
    
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public String getQuoteId() {
		return quoteId;
	}
    
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public String getSrvr() {
		return server;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public String getStatus() {
		return status;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
	public String getStrm() {
		return stream;
	}
    
    @Representation(names = {"COMPLETE", "SUMMARY"})
    public Long getTmstmp() {
		return tmstmp;
    }

    @Representation(names = {"COMPLETE", "SUMMARY"})
	public Long getValueDate() {
		return valueDate;
	}

    //End Of Getters.
    //Start of Setters.
	public void setTmstmp(Long tmstmp) {
		this.tmstmp = tmstmp;
	}

	public void setRtEffectiveTime(Long rtEffectiveTime) {
		this.rtEffectiveTime = rtEffectiveTime;
	}

	public void setValueDate(Long valueDate) {
		this.valueDate = valueDate;
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
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public void setCcyPair(String ccyPair) {
		this.ccyPair = ccyPair;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
	public void setTier(Integer tier) {
		this.tier = tier;
	}

	@Override
	public int compareTo(RatesObject o) {
		return this.tmstmp.compareTo(o.getTmstmp());
	}

    @Override
    public String toString() {
        return "RatesObject{" +
                "tmstmp=" + tmstmp +
                ", rtEffectiveTime=" + rtEffectiveTime +
                ", valueDate=" + valueDate +
                ", bidPrice=" + bidPrice +
                ", bidSize=" + bidSize +
                ", askPrice=" + askPrice +
                ", askSize=" + askSize +
                ", provider='" + provider + '\'' +
                ", stream='" + stream + '\'' +
                ", ccyPair='" + ccyPair + '\'' +
                ", status='" + status + '\'' +
                ", guid='" + guid + '\'' +
                ", quoteId='" + quoteId + '\'' +
                ", server='" + server + '\'' +
                ", adapter='" + adapter + '\'' +
                ", tier=" + tier +
                '}';
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }
}
