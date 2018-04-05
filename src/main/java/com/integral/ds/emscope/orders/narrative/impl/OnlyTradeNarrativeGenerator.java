package com.integral.ds.emscope.orders.narrative.impl;

import com.integral.ds.dto.Order;
import com.integral.ds.dto.TradeInfo;
import com.integral.ds.emscope.orders.narrative.NarrativeGenerator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Narrative generator for trades without order.
 *
 * @author Rahul Bhattacharjee
 */
public class OnlyTradeNarrativeGenerator extends AbstractNarrativeGenerator implements NarrativeGenerator {

    private static final String MESSAGE = "{orgName} Trade ID: {tradeID}~"
            + "The <b>{orgName}</b> has entered a trade <b>{type}</b> to <b>{buySell} {ccyp}</b> @ <b>{rate}</b> for an amount of <b>{amt}</b> <b>{ccy}</b>. "
            + "The trade was entered at {trade_time}.~ Maker organization is {maker_org}";

    @Override
    public String generateNarrativeForOrder(Order order,List<TradeInfo> tradeInfos) {
        TradeInfo tradeInfo = tradeInfos.get(0);

        System.out.println("RAHUL Trade info " + tradeInfo);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        DecimalFormat amountDf = new DecimalFormat("###,###,###,###.###");
        amountDf.setRoundingMode(RoundingMode.DOWN);

        String returnMessage = MESSAGE.replace("{orgName}", normalizeWithBlank(tradeInfo.getTakerOrg()));
        returnMessage = returnMessage.replace("{tradeID}", normalizeWithBlank(tradeInfo.getTradeId()));

        returnMessage = returnMessage.replace("{buySell}",normalizeSide(normalizeWithBlank(tradeInfo.getBuysell())));
        returnMessage = returnMessage.replace("{rate}",normalizeWithBlank(Float.toString(tradeInfo.getRate())));
        returnMessage = returnMessage.replace("{ccyp}",normalizeWithBlank(tradeInfo.getCcyPair()));
        returnMessage = returnMessage.replace("{trade_time}", normalizeWithBlank(sdf.format(tradeInfo.getExectime())));

        if(isBaseCurrencyTrade(tradeInfo)) {
            returnMessage = returnMessage.replace("{amt}", normalizeWithBlank(amountDf.format(new Float(tradeInfo.getBaseamt()))));
        } else {
            returnMessage = returnMessage.replace("{amt}", normalizeWithBlank(amountDf.format(new Float(tradeInfo.getTermamt()))));
        }

        returnMessage = returnMessage.replace("{ccy}", normalizeWithBlank(tradeInfo.getDealt()));
        returnMessage = returnMessage.replace("{type}", normalizeWithBlank(tradeInfo.getType()));
        returnMessage = returnMessage.replace("{maker_org}",tradeInfo.getMakerOrg());
        returnMessage = returnMessage.concat(getStatusString(tradeInfo.getStatus()));
        return returnMessage;
    }

    private boolean isBaseCurrencyTrade(TradeInfo tradeInfo) {
        try {
            return !tradeInfo.getCcyPair().endsWith(tradeInfo.getDealt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private String normalizeWithBlank(String value) {
        return (value == null) ? "" : value;
    }

    private String normalizeSide(String value) {
        return ("B".equalsIgnoreCase(value) || "Buy".equalsIgnoreCase(value)) ? "Buy" : "Sell";
    }

    private String getStatusString(String status) {
        switch (status) {
            case "L": return "The trade was Cancelled.";
            case "X": return "The trade was Cancelled.";
            case "J": return "The trade was Rejected.";
            case "R": return "The trade was Rejected.";
            case "P": return "The trade was Rejected.";
            case "I": return "The trade was Confirmed.";
            case "C": return "The trade was Confirmed.";
            default:  return "The trade type is unrecognized";
        }
    }
}
