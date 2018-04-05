package com.integral.ds.emscope.orders.narrative.impl;

import com.google.common.base.Optional;
import com.integral.ds.dto.Order;
import com.integral.ds.dto.TradeInfo;
import com.integral.ds.emscope.orders.OrderStatus;
import com.integral.ds.emscope.orders.narrative.NarrativeGenerator;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Abstract class for narrative generation.
 */
public abstract class AbstractNarrativeGenerator implements NarrativeGenerator {

    private static final Logger LOGGER = Logger.getLogger(AbstractNarrativeGenerator.class);

    private static final String STATUS_A = " It is still an <b>Active</b> order";
    private static final String STATUS_F = " It was completely filled @ <b>{rate}</b>.";
    private static final String STATUS_P = " It was partially filled @ <b>{fillRate}</b> for an amount of <b>{filledAmt}</b>. ";
    private static final String STATUS_X = " It was cancelled. The partially filled amount was <b>{filledAmt}</b> @ <b>{fillRate}</b>.";
    private static final String STATUS_Z = " Last Event at {lastEvent}. ";
    private static final String STATUS_D = " It got declined. Last Event at <b>{lastEvent}</b>";

    public String formatOrderRate(String orderRate) {
        DecimalFormat df = new DecimalFormat("#.#######");
        try {
            Double doubleRate = Double.parseDouble(orderRate);
            return getFormattedDouble(doubleRate);
        } catch (Exception exception) {
            LOGGER.error("Ignoring exception while formatting order rate.",
                    exception);
        }
        try {
            int index = orderRate.indexOf("from");
            int startIndex = 0;
            if (index == -1) {
                startIndex = 10;
            } else {
                startIndex = index + 5;
            }
            String someString = df.format(Double.parseDouble(orderRate
                    .substring(startIndex)));
            String startingString = orderRate.substring(0, startIndex);
            return startingString + someString;
        } catch (Exception exception) {
            LOGGER.error("Ignoring exception while formatting order rate.",
                    exception);
        }
        return "";
    }

    private String getFormattedDouble(double value) throws Exception {
        DecimalFormat decimalFormat = new DecimalFormat("#.#######");
        try {
            return decimalFormat.format(value);
        } catch (Exception exception) {
            LOGGER.error("Ignoring exception while formatting double " + value,
                    exception);
        }
        return "";
    }

    public String appendOrderStatusToMessage(OrderStatus orderStatus,String returnMessage, Order order, List<TradeInfo> tradeInfoList) {
        switch (orderStatus) {
            case A:
                returnMessage += STATUS_A;
                break;
            case F:
                returnMessage += STATUS_F;
                break;
            case P:
                returnMessage += STATUS_P;
                break;
            case X: {
                if(isNonEmptyOrder(order,tradeInfoList)) {
                    returnMessage += STATUS_X;
                } else {
                    returnMessage += " It was cancelled.";
                }
                break;
            }
            case Z:
                returnMessage += STATUS_Z;
                break;
            case D:
                returnMessage += STATUS_D;
                break;
            default:
        }
        return returnMessage;
    }

    private boolean isNonEmptyOrder(Order order, List<TradeInfo> tradeInfoList) {
        double filledAmount =  order.getFilledAmt().doubleValue();
        double rate = 0;
        if(isCancelled(tradeInfoList)) {
            return false;
        }
        try {
            rate = Double.parseDouble(order.getFillRate());
        }catch (Exception e){}
        return !(filledAmount == 0 && rate == 0);
    }

    private boolean isCancelled(List<TradeInfo> tradeInfoList) {
        for(TradeInfo tradeInfo : tradeInfoList) {
            if(!("X".equalsIgnoreCase(tradeInfo.getStatus()))) {
                return false;
            }
        }
        return true;
    }

    protected Optional<Long> getOrderDuration(Order order) {
        long lastEventTime = order.getLastEvent();
        long createdTimeOfOrder = order.getCreated();
        if(lastEventTime == 0) {
            return Optional.absent();
        } else {
            long diff = (lastEventTime-createdTimeOfOrder);
            return diff <= 0 ? Optional.<Long>absent() : Optional.of(diff);
        }
    }

    protected boolean isZeroFloat(String orderRate) {
        float val = 0.0F;
        try {
            val = Float.parseFloat(orderRate);
        }catch (Exception e) {
        }
        return val == 0.0F ? true : false;
    }
}
