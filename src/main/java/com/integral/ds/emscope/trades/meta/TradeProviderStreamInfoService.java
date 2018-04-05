package com.integral.ds.emscope.trades.meta;

import com.integral.ds.dao.InternalTradeEPADao;
import com.integral.ds.dto.InternalTradeEPAInfo;
import com.integral.ds.dto.TradeStreamDetails;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service to get trades provider , stream , tier information from trade epa internal table.
 *
 * @author Rahul Bhattacharjee
 */
public class TradeProviderStreamInfoService {

    private InternalTradeEPADao tradeEPADao;

    public Map<String,TradeStreamDetails> getStreamDetailsForTrades(List<String> tradeIds) {
        if(tradeIds == null || tradeIds.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        List<InternalTradeEPAInfo> tradeEPAInfos = tradeEPADao.getTradeInfos(tradeIds);
        Map<String,TradeStreamDetails> result = new HashMap<String,TradeStreamDetails>();
        for(String tradeId : tradeIds) {
            result.put(tradeId,new TradeStreamDetails());
        }
        for(InternalTradeEPAInfo tradeEPAInfo : tradeEPAInfos) {
            setTradeEpaInfoToResult(tradeEPAInfo,result);
        }
        return result;
    }

    private void setTradeEpaInfoToResult(InternalTradeEPAInfo tradeEPAInfo, Map<String, TradeStreamDetails> result) {
        TradeStreamDetails streamDetails = result.get(tradeEPAInfo.getTradeid());
        streamDetails.setAvailable(true);
        streamDetails.setProvider(tradeEPAInfo.getProvider());
        streamDetails.setStream(tradeEPAInfo.getStream());
        streamDetails.setTier(tradeEPAInfo.getTier());
        streamDetails.setCcyPair(tradeEPAInfo.getCcyPair());
        streamDetails.setAvailable(tradeEPAInfo.isValid());
    }

    public void setTradeEPADao(InternalTradeEPADao tradeEPADao) {
        this.tradeEPADao = tradeEPADao;
    }
}
