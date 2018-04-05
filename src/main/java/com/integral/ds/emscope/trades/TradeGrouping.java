package com.integral.ds.emscope.trades;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.integral.ds.dto.Order;
import org.apache.log4j.Logger;

import com.integral.ds.commons.util.Pair;

public class TradeGrouping implements Comparable<TradeGrouping> {
	
	protected static final Logger LOG = Logger.getLogger(TradeGrouping.class);
	private static final String CONFIRMED_TRADE = "C";
	private static final String REJECTED_TRADE = "R";
	private static final String FAILEd_TRADE = "F";
	private static final String CANCELLED_TRADE = "X";
	private DecimalFormat df = new DecimalFormat("#.#####");
	private DecimalFormat dfWith2PtPrecision = new DecimalFormat("#.##");
	private String providerName;
	
	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	private int confirmedTradeCount;
	private double comfirmedAmount;
	
	private int rejectedTradesCount;
	private double rejectedTradesAmount;
	
	private int failedTradesCount;
	private double failedTradesAmount;
	
	private int cancelledTradeCount;
	private double cancelledTradeAmount;
	
	private double totalAmount;
	private double matchingAmount;
	private double filledRatio;

	private long totalResponseTime;
	private int numberOfTrades;
	private long averageResponseTime;

	private String priceImprovement;

	private Map<Pair<Integer, String>, List<Pair<BigDecimal, BigDecimal>>>  matchedAmountMap = new HashMap<>();

	public String getWeightedAvgRate() {
		if (comfirmedAmount == 0)
			return "NA";
		return df.format(totalAmount/comfirmedAmount);
	}

	public int getConfirmedTradeCount() {
		return confirmedTradeCount;
	}

	public String getComfirmedAmount() {
		return df.format(comfirmedAmount);
	}

	public int getRejectedTradesCount() {
		return rejectedTradesCount;
	}

	public String getRejectedTradesAmount() {
		return df.format(rejectedTradesAmount);
	}

	public int getFailedTradesCount() {
		return failedTradesCount;
	}

	public String getFailedTradesAmount() {
		return df.format(failedTradesAmount);
	}

	public int getCancelledTradeCount() {
		return cancelledTradeCount;
	}

	public String getCancelledTradeAmount() {
		return df.format(cancelledTradeAmount);
	}
	
	public String getMatchingAmount() {
		return df.format(matchingAmount);
	}
	
	public String getFilledRatio() {
		if(matchingAmount==0) {
			return "NA"; 
		}
		return dfWith2PtPrecision.format(filledRatio);
	}

	public void processTrade(Map tradeMap, boolean isTermCcy){
		String status = (String)tradeMap.get("TRADESTATUS");
		BigDecimal amount = (BigDecimal)tradeMap.get(isTermCcy ? "TERMAMT":"BASEAMT");
		BigDecimal matchingAmountTrade = (BigDecimal)tradeMap.get("MATCHINGAMOUNT");
		BigDecimal rate = (BigDecimal) tradeMap.get("RATE");
		Integer sweepnumber = (Integer) tradeMap.get("SWEEPNUMBER");

		long responseTime = ((BigDecimal)tradeMap.get("RESPONSETIME")).longValue();
		if(responseTime != 0) {
			totalResponseTime += responseTime;
			numberOfTrades++;
		}

		BigDecimal matchedRate = BigDecimal.valueOf(Double.valueOf((String)tradeMap.get("MATCHEDRATE")));
		String stream = (String)tradeMap.get("STREAM");

		if (status.equals("C") ) {
			confirmedTradeCount ++;
			comfirmedAmount= comfirmedAmount + amount.doubleValue();
			double value = (amount.doubleValue() * rate.doubleValue());
			totalAmount = totalAmount + value;
		} else if (status.equals(REJECTED_TRADE)) {
			rejectedTradesCount ++;
			rejectedTradesAmount = rejectedTradesAmount + amount.doubleValue();
		} else if (status.equals(FAILEd_TRADE))	{
			failedTradesCount ++;
			failedTradesAmount = failedTradesAmount + amount.doubleValue();
		} else if (status.equals(CANCELLED_TRADE)) {
			cancelledTradeAmount ++;
			cancelledTradeAmount = cancelledTradeAmount + amount.doubleValue();
		}
		
		Pair<Integer, String> sweepNumStream = new Pair(sweepnumber,stream);
		List<Pair<BigDecimal, BigDecimal>> matchAmtSet = matchedAmountMap.get(sweepNumStream);
		if (matchAmtSet == null) {
			matchAmtSet = new ArrayList<Pair<BigDecimal,BigDecimal>>();
			matchedAmountMap.put(sweepNumStream, matchAmtSet);
		}
		matchAmtSet.add(new Pair(matchingAmountTrade,matchedRate));
	}
	
	@Override
	public int compareTo(TradeGrouping o) {
		return new Double(o.getComfirmedAmount()).compareTo(new Double(this.getComfirmedAmount()));
	}
	
	public void computeMatchingAmount() {
		for (Pair<Integer, String> sweepNumStream : matchedAmountMap.keySet()) {
			List<Pair<BigDecimal, BigDecimal>> amntRatePair = matchedAmountMap.get(sweepNumStream);
			double tempMatchingAmt = 0;
			for (Pair<BigDecimal, BigDecimal> pair : amntRatePair) {
				if(pair.getSecond().equals(BigDecimal.valueOf(-1.0))) {
					matchingAmount -= tempMatchingAmt;
					matchingAmount += pair.getFirst().doubleValue();
					break;
				}
				tempMatchingAmt += pair.getFirst().doubleValue();
				matchingAmount += pair.getFirst().doubleValue();
			}
		}
		filledRatio = comfirmedAmount/matchingAmount;
	}

	public void computeAverageResponseTime() {
		if(numberOfTrades != 0) {
			averageResponseTime = totalResponseTime/numberOfTrades;
		}
	}

	public void calculatePriceImprovement(Order order) {
		float fillRateInFloat = Float.parseFloat(order.getFillRate());
		String waRate = getWeightedAvgRate();

		if("NA".equalsIgnoreCase(waRate)) {
			priceImprovement = waRate;
		} else {
			float waRateInFloat = Float.parseFloat(waRate);
			if("B".equalsIgnoreCase(order.getBuySell()) || "BUY".equalsIgnoreCase(order.getBuySell())) {
				priceImprovement = df.format(fillRateInFloat - waRateInFloat);
			} else {
				priceImprovement = df.format(waRateInFloat - fillRateInFloat);
			}
		}
	}

	public String getPriceImprovement() {
		return priceImprovement;
	}

	public long getAverageResponseTime() {
		return averageResponseTime;
	}
}
