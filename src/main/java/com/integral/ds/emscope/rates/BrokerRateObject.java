package com.integral.ds.emscope.rates;

import java.math.BigDecimal;
import java.util.List;

/**
 * Bean for broker rates.
 *
 * @author Rahul Bhattacharjee
 */
public class BrokerRateObject {

    private String broker;
    private String brokerStream;
    private String ccyPair;
    private long timeStamp;
    private List<LPStream> lpStreams;
    private String aggregationStrategy; // replace with enum when we have full list.
    private String quoteType;
    private String updateType;
    private String guid;
    private BigDecimal bidPrice;
    private BigDecimal bidSize;
    private String bidLp;
    private BigDecimal askPrice;
    private BigDecimal askSize;
    private String askLp;

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getBrokerStream() {
        return brokerStream;
    }

    public void setBrokerStream(String brokerStream) {
        this.brokerStream = brokerStream;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<LPStream> getLpStreams() {
        return lpStreams;
    }

    public void setLpStreams(List<LPStream> lpStreams) {
        this.lpStreams = lpStreams;
    }

    public String getAggregationStrategy() {
        return aggregationStrategy;
    }

    public void setAggregationStrategy(String aggregationStrategy) {
        this.aggregationStrategy = aggregationStrategy;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    public BigDecimal getBidSize() {
        return bidSize;
    }

    public void setBidSize(BigDecimal bidSize) {
        this.bidSize = bidSize;
    }

    public String getBidLp() {
        return bidLp;
    }

    public void setBidLp(String bidLp) {
        this.bidLp = bidLp;
    }

    public BigDecimal getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    public BigDecimal getAskSize() {
        return askSize;
    }

    public void setAskSize(BigDecimal askSize) {
        this.askSize = askSize;
    }

    public String getAskLp() {
        return askLp;
    }

    public void setAskLp(String askLp) {
        this.askLp = askLp;
    }
}
