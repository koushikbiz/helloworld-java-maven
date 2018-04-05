package com.integral.ds.emscope.trades;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class TradeNarrative {

	
	private static final String message = "{counterParty} Filled {totalAmount} in {tradeCount} Trades at an Average rate of {avgRate}";
	private static final String headerMessage = "The Top {count} Provoders who filled the Trade are";
	public String getNarrative(List<TradeGrouping> orderGroupingList)	{
		
		
		
		
		Collections.sort(orderGroupingList);
		DecimalFormat df = new DecimalFormat("#.#####");
		String narrative = "" + headerMessage;
		int count = 0;
		for (TradeGrouping orderGroup: orderGroupingList)	{
			
			String cptyMessage = message.replace("{counterParty}", orderGroup.getProviderName());
			cptyMessage = cptyMessage.replace("{totalAmount}", orderGroup.getComfirmedAmount());
			cptyMessage = cptyMessage.replace("{tradeCount}", orderGroup.getConfirmedTradeCount() + "");
			cptyMessage = cptyMessage.replace("{avgRate}", orderGroup.getWeightedAvgRate());
			
			narrative = narrative + "~" + cptyMessage;
			if (count >= 5)
				break;
			
			count++;
		}
		
		return narrative.replace("{count}", count + "");
		
	}
}
