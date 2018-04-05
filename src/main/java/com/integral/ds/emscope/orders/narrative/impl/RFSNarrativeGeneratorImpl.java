package com.integral.ds.emscope.orders.narrative.impl;

import com.integral.ds.dto.Order;
import com.integral.ds.dto.Trade;
import com.integral.ds.dto.TradeInfo;
import com.integral.ds.emscope.orders.OrderStatus;
import com.integral.ds.emscope.orders.narrative.NarrativeGenerator;
import com.integral.ds.tool.Util;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * RFS trades narrative generator.
 *
 * @author Rahul Bhattacharjee
 */
public class RFSNarrativeGeneratorImpl extends AbstractNarrativeGenerator implements NarrativeGenerator {

    private static final Logger LOGGER = Logger.getLogger(RFSNarrativeGeneratorImpl.class);

    private static final BigDecimal ORDER_DURATION_CUTOFF = new BigDecimal(2500);
    private static final String ORDER_DURATION_MESSAGE = " The total order duration was <b>{orderduration}</b>.~";
    private static final String ORDER_EXECUTION_STRATEGY = "~ Order execution strategy: <b>{executionstrategy}</b>. ~";
    private static final String MESSAGE = "{orgName} {cpty_a} Order ID: {orderID}~"
            + "The <b>{orgName}:{dealer}</b> placed a <b>{orderType} ({subType})</b> order "
            + "to {provider} <b>{buySell} {ccyp}</b> {rate_str} for an amount of <b>{orderAmt}</b> <b>{ccy}</b>. Spot rate was <b>{spot_rate}</b>.~"
            + "Time in Force: <b>{timeInForce}</b> The order was placed at <b>{created}</b>, GMT.";

    @Override
    public String generateNarrativeForOrder(Order order,List<TradeInfo> tradeInfos) {
        String returnMessage = "";
        TradeInfo tradeInfo = tradeInfos.get(0);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            DecimalFormat amountDf = new DecimalFormat("###,###,###,###.###");

            returnMessage = MESSAGE.replace("{orgName}", order.getOrg());
            returnMessage = returnMessage.replace("{orderID}",order.getOrderID());

            if(StringUtils.isNotBlank(order.getBrokerForCptyA())) {
                String msg = "( a customer of " + order.getBrokerForCptyA() + " )";
                returnMessage = returnMessage.replace("{cpty_a}",msg);
            }else {
                returnMessage = returnMessage.replace("{cpty_a}","");
            }

            returnMessage = returnMessage
                    .replace("{dealer}", order.getDealer());
            returnMessage = returnMessage.replace("{orderType}",
                    order.getOrderType());
            returnMessage = returnMessage.replace("{subType}", tradeInfo.getType());
            returnMessage = returnMessage.replace("{buySell}",
                    order.getBuySell());
            returnMessage = returnMessage.replace("{ccyp}", order.getCcyPair());
            returnMessage = returnMessage.replace("{spot_rate}",Float.toString(tradeInfo.getSpotrate()));
            returnMessage = returnMessage.replace("{provider}",order.getProvider());

            if (order.getMarketRange() != null && Double.valueOf( order.getMarketRange()) > 0.0) {
                String rateMessage = String.format("Range %s from %s",formatOrderRate(Double.valueOf( order.getMarketRange())+""), formatOrderRate(order.getOrderRate()));
                returnMessage = returnMessage.replace("{rate_str}", rateMessage);
            } else {
                String orderRate = formatOrderRate(order.getOrderRate());
                String orderRateString = null;
                if(isZeroFloat(orderRate)) {
                    orderRateString = "";
                } else {
                    orderRateString = " @ <b>" + orderRate + "</b>";
                }
                returnMessage = returnMessage.replace("{rate_str}",orderRateString);
            }

            BigDecimal totalOrderDuration = order.getTotalOrderDuration();
            if (totalOrderDuration != null) {
                if (totalOrderDuration.compareTo(ORDER_DURATION_CUTOFF) <= 0) {
                    returnMessage += ORDER_DURATION_MESSAGE.replace("{orderduration}", Util.getReadableTimeRange(totalOrderDuration.longValue()));
                } else {
                    returnMessage += ORDER_DURATION_MESSAGE.replace("{orderduration}", Util.getReadableTimeRange(totalOrderDuration.longValue()));
                }
            } else {
                returnMessage += "~";
            }
            returnMessage = returnMessage.replace("{orderAmt}",
                    amountDf.format(order.getOrderAmt()));
            returnMessage = returnMessage.replace("{timeInForce}",
                    order.getTimeInForce());
            returnMessage = returnMessage.replace("{created}",
                    sdf.format(new Date(order.getCreated())));
            returnMessage = returnMessage.replace("{ccy}", order.getDealt());

            returnMessage = appendOrderStatusToMessage(OrderStatus.valueOf(order.getOrderStatus()), returnMessage , order,tradeInfos);

            returnMessage = returnMessage.replace("{rate}", formatOrderRate(Float.toString(tradeInfo.getRate())));

            if (order.getFillRate() != null)
                returnMessage = returnMessage.replace("{fillRate}",
                        order.getFillRate() + "");

            if (order.getPpnl() != null)
                returnMessage = returnMessage.replace("{ppnl}", order.getPpnl()
                        .floatValue() + "");

            if (order.getFilledAmt() != null)
                returnMessage = returnMessage.replace("{filledAmt}",
                        amountDf.format(order.getFilledAmt().longValue()) + "");

            if (order.getLastEvent() != 0l)
                returnMessage = returnMessage.replace("{lastEvent}",
                        sdf.format(new Date(order.getLastEvent())));

            returnMessage = returnMessage.replace("{orderRate}", order.getOrderRate());

            String executionStrategy = order.getExecutionStrategy();
            if (executionStrategy != null && !StringUtils.EMPTY.equals(executionStrategy)) {
                returnMessage += ORDER_EXECUTION_STRATEGY.replace("{executionstrategy}", executionStrategy);
            }
            LOGGER.debug("Returning narrative message for order " + order
                    + " , message " + returnMessage);
        } catch (Exception exception) {
            LOGGER.error("Exception while generating narrative for order "
                    + order, exception);
        }
        return returnMessage;
    }
}
