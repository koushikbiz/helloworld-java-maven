package com.integral.ds.cfx.metadata.impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.integral.ds.cfx.metadata.Metadata;
import com.integral.ds.s3.S3Utils;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Test S3 metadata service.
 * @author Rahul Bhattacharjee
 */
public class TestS3MetadataImpl {

    private final static String accessKey = S3Utils.getAccesskey();
    private final static String secretKey = S3Utils.getSecretkey();


    private String bucket = "integral-rates";
    private String key = "temporary/cfx.config";

    //@Test
    public void testS3MetadataService() {

        ClientConfiguration config = new ClientConfiguration();
        config.setSocketTimeout(0);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        Metadata metadata = new S3MetadataImpl(bucket,key,credentials);
        metadata.load();

        String val = metadata.get("key1");

        metadata.set("key2","val2");
        metadata.store();


        metadata = new S3MetadataImpl(bucket,key,credentials);
        metadata.load();

        val = metadata.get("key1");
        val = metadata.get("key2");
    }
}
