package com.integral.ds.emscope.trades.external;

import com.google.common.base.Optional;
import com.integral.ds.s3.RecordTransformer;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * External trade csv file parser.
 *
 * @author Rahul Bhattacharjee
 */
public class ExternalTradeParser {

    private static final RecordTransformer<Optional<ExternalTrade>> TRANSFORMER = new ExternalTradeTransformer();

    public List<ExternalTrade> parse(BufferedReader reader) {
        List<ExternalTrade> trades = new ArrayList<ExternalTrade>();
        String line = "";

        try {
            while (line != null) {
                line = reader.readLine();
                if(StringUtils.isBlank(line) || line.startsWith("#")) continue;

                Optional<ExternalTrade> trade = TRANSFORMER.transform(line);
                if(trade.isPresent()) trades.add(trade.get());
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_LIST;
        }
        return trades;
    }

    private static class ExternalTradeTransformer implements RecordTransformer<Optional<ExternalTrade>> {
        @Override
        public Optional<ExternalTrade> transform(String record) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
            ExternalTrade trade = new ExternalTrade();

            try {
                String[] splits = record.split(",");
                trade.setTradeId(splits[0]);
                String dateString = splits[1];
                String timeString = splits[2];
                String dataTime = dateString + " " + timeString;
                Date date = formatter.parse(dataTime);
                trade.setExecTime(date.getTime());
                trade.setSide(ExternalTrade.Side.valueOf(splits[3]));
                trade.setCcyPair(splits[4]);
                trade.setVolume(Long.parseLong(splits[5]));
                trade.setPrice(Float.parseFloat(splits[6]));
            }catch (Exception e) {
                e.printStackTrace();
                return Optional.absent();
            }
            return Optional.of(trade);
        }
    }
}
