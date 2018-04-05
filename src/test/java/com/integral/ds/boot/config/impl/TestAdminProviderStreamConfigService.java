package com.integral.ds.boot.config.impl;

import com.integral.ds.dao.NoopProviderStreamConfigFeatcherImpl;
import com.integral.ds.dao.ProviderStreamConfigFetcher;
import com.integral.ds.dto.CompleteProviderStream;
import com.integral.ds.dto.ProviderStream;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhattacharjeer on 3/21/2017.
 */
public class TestAdminProviderStreamConfigService {

    //@Test
    public void testAdminServiceBrokerServiceImpl() {
        List<ProviderStream> providerStreams = new ArrayList<ProviderStream>();
        ProviderStream providerStream1 = new ProviderStream("AXICORP","HCTECH_BNPPB");
        ProviderStream providerStream2 = new ProviderStream("AXICORP","HFTHacy_RBSPB");

        providerStreams.add(providerStream1);
        providerStreams.add(providerStream2);

        ProviderStreamConfigFetcher fetcher = new NoopProviderStreamConfigFeatcherImpl(providerStreams);
        AdminProviderStreamConfigServiceImpl awsService = new AdminProviderStreamConfigServiceImpl();
        awsService.setStreamReaderDao(fetcher);
        awsService.init();

        List<CompleteProviderStream> list = awsService.getCompleteProviderStreamConfiguration();
        System.out.println(list.size());
    }
}
