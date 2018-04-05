package com.integral.ds.emscope.rates.filter;

import com.integral.ds.emscope.rates.RateRule;
import com.integral.ds.emscope.rates.RestRatesObject;
import com.integral.ds.emscope.rates.filter.impl.NoopFilter;
import com.integral.ds.emscope.rates.filter.impl.SampleFilter;
import com.integral.ds.emscope.rates.filter.impl.TierFilter;
import com.integral.ds.s3.Filter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Filter factory and filter utility class.
 *
 * @author Rahul Bhattacharjee
 */
public class RatesFilterFactory {

    private static final Logger LOGGER = Logger.getLogger(RatesFilterFactory.class);

    public enum FilterType {TIER_FILTER,SAMPLE_FILTER,NOOP_FILTER}

    public static List<RateRule<RestRatesObject>> getFilters(String filterTypes ,String filterParams) {
        List<RateRule<RestRatesObject>> result = new ArrayList<RateRule<RestRatesObject>>();
        String [] filters = filterTypes.split("\\|");
        String [] filterParameters = filterParams.split("\\|",filters.length);

        if(filters.length != filterParameters.length) {
            throw new IllegalArgumentException("Filter parameter mismatch. Filter => " + filterTypes + " , Filter parameters => " + filterParams);
        }

        for(int i = 0; i < filters.length; i++) {
            String filter = filters[i];
            RateRule<RestRatesObject> parsedFilter = getFilter(filter);
            parsedFilter.init(getFilterParam(filterParameters[i]));
            result.add(parsedFilter);
        }
        return result;
    }

    private static List<String> getFilterParam(String parameters) {
        if(StringUtils.isNotBlank(parameters)) {
            String [] params = parameters.split(",");
            return new ArrayList(Arrays.asList(params));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    private static RateRule<RestRatesObject> getFilter(String filterType) {
        try {
            FilterType type = FilterType.valueOf(filterType.toUpperCase());
            switch (type) {
                case TIER_FILTER:return new TierFilter();
                case SAMPLE_FILTER: return new SampleFilter();
                case NOOP_FILTER: return new NoopFilter();
                default: return new NoopFilter();
            }
        } catch (Exception e) {
            return new NoopFilter();
        }
    }

    public static List<RestRatesObject> filterRates(List<RateRule<RestRatesObject>> rules, final List<RestRatesObject> listOfRates) {
        List<RestRatesObject> input = listOfRates;
        for(RateRule<RestRatesObject> rule : rules) {
            String msg = "Running rule " + rule + " , on input set " + input.size();
            if(input.isEmpty()) {
                LOGGER.info(msg + " , output size 0.");
                return input;
            } else {
                input = rule.filter(input);
            }
            LOGGER.info(msg + " , output size " + input.size());
        }
        return input;
    }
}
