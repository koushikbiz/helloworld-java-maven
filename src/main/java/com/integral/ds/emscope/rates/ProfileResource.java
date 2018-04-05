package com.integral.ds.emscope.rates;

import com.integral.ds.emscope.profile.ProfileRecord;
import com.integral.ds.emscope.rates.filter.impl.SampleFilter;
import com.integral.ds.emscope.rates.filter.impl.SamplingUtility;
import com.integral.ds.s3.S3ProfileReader;
import com.integral.ds.util.PropertyReader;
import com.integral.ds.util.RestResponseUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Profile service. This is return profile from a specified time.
 *
 * @author Rahul Bhattacharjee
 */
@Path("/profile")
@Component
public class ProfileResource {

    private static final Logger LOGGER = Logger.getLogger(ProfileResource.class);

    private static final String UNEXPECTED_ERROR = "Unexpected error in EMScope.";
    private static final String UNSUPPORTED_CCYPAIR_WARNING = "Unsupported currency pair for profile service.";
    private static final S3ProfileReader S_3_PROFILE_READER = new S3ProfileReader();
    private static Set<String> SUPPORTED_CCYPAIRS;

    static {
        loadSupportedCcyPairs();
    }

    private static void loadSupportedCcyPairs() {
        Set<String> ccyPairsSet = new HashSet<String>();
        String ccyPairs = PropertyReader.getPropertyValue(PropertyReader.KEY_PROFILE_SUPPORTED_CCYPAIR);
        if (StringUtils.isNotBlank(ccyPairs)) {
            String[] splits = ccyPairs.split(",");
            for (String split : splits) {
                ccyPairsSet.add(split);
            }
        }
        SUPPORTED_CCYPAIRS = Collections.unmodifiableSet(ccyPairsSet);
    }

    @GET
    @Path("{provider}/{stream}/{ccyp}/{fromtime}/{totime}")
    @Produces(MediaType.TEXT_PLAIN)
    public String findRange(@Context HttpServletRequest request, @PathParam("provider") String provider, @PathParam("stream") String stream,
                            @PathParam("ccyp") String ccyPair, @PathParam("fromtime") long fromtime, @PathParam("totime") long toTime) {

        String responseMessage = null;
        try {
            if (validate(ccyPair)) {
                Date startTime = new Date(fromtime);
                Date endTime = new Date(toTime);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Fetching profile information for " + provider + " stream " + stream + " ccy pair " + ccyPair);
                }

                List<ProfileRecord> response = S_3_PROFILE_READER.getProfileRecords(provider, stream, ccyPair, startTime, endTime);
                response = sample(response,fromtime,toTime);
                return RestResponseUtil.setJsonResponse(request, response);
            } else {
                responseMessage = UNSUPPORTED_CCYPAIR_WARNING;
            }
        } catch (Exception e) {
            LOGGER.error(UNEXPECTED_ERROR, e);
            responseMessage = UNEXPECTED_ERROR;
        }
        return responseMessage;
    }

    private List<ProfileRecord> sample(List<ProfileRecord> response, long fromTime, long toTime) {
        return SamplingUtility.sample(response,fromTime,toTime);
    }

    private boolean validate(String ccyPair) {
        if (ccyPair != null) {
            return SUPPORTED_CCYPAIRS.contains(ccyPair.trim().toUpperCase());
        }
        return false;
    }
}
