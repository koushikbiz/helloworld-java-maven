package com.integral.ds.emscope.rates;

import com.google.common.base.Optional;
import com.integral.ds.commons.model.IRate;
import com.integral.ds.commons.model.VwapCalculationResult;
import com.integral.ds.commons.util.CcyPairPipMultiplyFactor;
import com.integral.ds.dto.ProviderStream;
import com.integral.ds.dto.RatePriceSizeQuote;
import com.integral.ds.dto.RatePriceSizeTier;
import com.integral.ds.emscope.rates.filter.impl.SamplingUtility;
import com.integral.ds.lpa.driver.RateQuoteServiceFactory;
import com.integral.ds.lpa.util.BidOfferCalculator;
import com.integral.ds.lpa.util.BidOfferCalculator2;
import com.integral.ds.util.demo.DemoUtility;

import java.math.BigDecimal;
import java.util.*;

/**
 * Rate price at size service.
 *
 * @author Rahul Bhattacharjee
 */
public class RatePriceAtSizeServiceImpl implements RatePriceAtSizeService {

    private BidOfferCalculator2 CALCULATOR = new BidOfferCalculator2();
    private RateQuoteServiceFactory quoteServiceFactory;

    @Override
    public List<RatePriceSizeQuote> getPriceAtSizeRates(String provider, String stream, String ccyPair, List<Long> volumes, Date fromTime, Date endTime) {
        List<RestRatesObject> rates = quoteServiceFactory.getRateServiceImpl(fromTime,endTime).getRates(provider, stream, ccyPair, fromTime, endTime);
        if(rates == null || rates.isEmpty()) return Collections.EMPTY_LIST;

        List<RatePriceSizeQuote> result = new ArrayList<RatePriceSizeQuote>();

        List<List<RestRatesObject>> groupedRates = groupRates(rates);
        groupedRates = SamplingUtility.sample(groupedRates,fromTime.getTime(),endTime.getTime());

        for(List<RestRatesObject> innerRates : groupedRates) {
            result.add(createRatePriceObject(innerRates, volumes,provider,stream,ccyPair));
        }
        return result;
    }

    private List<List<RestRatesObject>> groupRates(List<RestRatesObject> rates) {
        List<List<RestRatesObject>> result = new ArrayList<List<RestRatesObject>>();
        Map<Long,List<RestRatesObject>> grouped = new TreeMap<Long,List<RestRatesObject>>();
        for(RestRatesObject rate : rates) {
            List<RestRatesObject> innerGroup = grouped.get(rate.getTmstmp());
            if(innerGroup == null) {
                innerGroup = new ArrayList<RestRatesObject>();
                grouped.put(rate.getTmstmp(),innerGroup);
            }
            innerGroup.add(rate);
        }
        Iterator<Long> iterator = grouped.keySet().iterator();

        while(iterator.hasNext()) {
            result.add(grouped.get(iterator.next()));
        }
        return result;
    }

    private RatePriceSizeQuote createRatePriceObject(List<RestRatesObject> rate, List<Long> volumes, String provider, String stream,String ccyPair) {
        RatePriceSizeQuote quote = new RatePriceSizeQuote();
        int tier = 1;

        for(Long volume : volumes) {
            RatePriceSizeTier rateTier = new RatePriceSizeTier();
            String synName = generateName(volume,provider,stream);
            double bid = 0.0d;
            double offer = 0.0d;

            rateTier.setSynName(synName);
            rateTier.setTier(tier++);

            List<IRate> rates = BidOfferCalculator.transformToIRateList((List<RestRatesObject>) rate, provider, stream);

            Optional<VwapCalculationResult> bidResult = CALCULATOR.getVWAPBidAtVolume(rates, volume);
            if(bidResult.isPresent()) bid = bidResult.get().getVwapPrice();

            Optional<VwapCalculationResult> offerResult = CALCULATOR.getVWAPOfferAtVolume(rates,volume);
            if(offerResult.isPresent()) offer = offerResult.get().getVwapPrice();

            rateTier.setBid(new BigDecimal(bid));
            rateTier.setOffer(new BigDecimal(offer));
            rateTier.setTimeStamp(rate.get(0).getTmstmp());
            double spread = getSpread(offer,bid);
            rateTier.setSpread(new BigDecimal(spread));
            rateTier.setVolume(volume);

            Long pipFactor = CcyPairPipMultiplyFactor.getPipConversionFactor(ccyPair);

            if(pipFactor != null) {
                double spreadInPip = pipFactor.longValue() * spread;
                rateTier.setSpreadPip(new BigDecimal(spreadInPip));
            }
            quote.setTier(rateTier);
        }
        return quote;
    }

    private double getSpread(double offer , double bid) {
        if(offer != 0.0d && bid != 0.0d) {
            return (offer - bid);
        }
        return 0;
    }

    private String generateName(Long volume, String provider, String stream) {
        ProviderStream providerStream = DemoUtility.createProviderStream(provider,stream);
        providerStream = DemoUtility.getMaskedProviderStream(providerStream);
        return providerStream.getProvider() + "-" + providerStream.getStream() + "-" + volume;
    }

    public void setQuoteServiceFactory(RateQuoteServiceFactory quoteServiceFactory) {
        this.quoteServiceFactory = quoteServiceFactory;
    }
}
