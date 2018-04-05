package com.integral.ds.emscope.rates;

/**
 * @author Rahul Bhattacharjee
 */
public class LPStream {

    private String provider;
    private String stream;

    public LPStream(String provider, String stream) {
        this.provider = provider;
        this.stream = stream;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
