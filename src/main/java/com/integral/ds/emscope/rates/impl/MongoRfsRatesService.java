package com.integral.ds.emscope.rates.impl;

import com.integral.ds.emscope.rates.RFSRate;
import com.integral.ds.emscope.rates.RFSRatesException;
import com.integral.ds.emscope.rates.RFSRatesService;
import com.integral.ds.util.PropertyReader;
import com.mongodb.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Mongo DB based rates fetcher.
 *
 * @author Rahul Bhattacharjee
 */
public class MongoRfsRatesService implements RFSRatesService {

    private static final String RFS_MONGO_SCHEMA = "integral";
    private static final int RFS_MONGO_TIMEOUT = 5000;

    private DB database;
    private volatile boolean init = false;

    public void init() {
        try {
            MongoOptions options = new MongoOptions();
            options.slaveOk = true;
            options.connectTimeout= RFS_MONGO_TIMEOUT;
            Mongo mongo = new Mongo(PropertyReader.getPropertyValue(PropertyReader.KEY_EMSCOPE_MONGO_SERVER), options);
            database = mongo.getDB(RFS_MONGO_SCHEMA);
        }catch (Exception e) {
            throw new IllegalArgumentException("Could not connect to the datastore.");
        }
    }

    private void initialize() {
        if(!init) {
            Lock lock = new ReentrantLock();
            lock.lock();
            try {
                if(!init) {
                    init();
                    init = true;
                }
            }finally {
                lock.unlock();
            }
        }
    }

    @Override
    public List<RFSRate> getRates(String transactionId) throws RFSRatesException {
        initialize();
        DBCollection collection = getCollectionFromRequest(transactionId);
        if(collection == null) throw new RFSRatesException("No appropriate Mongo collection found.");
        DBObject orderByQuoteDateTime = new BasicDBObject("QUOTEDATETIME", 1);

        DBObject requestQueryObject = new BasicDBObject();
        requestQueryObject.put("TRANSACTIONID", transactionId);
        DBCursor cursor = collection.find(requestQueryObject).sort(orderByQuoteDateTime);

        List<RFSRate> result = new ArrayList<RFSRate>();
        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            result.addAll(parseRFSRate(object));
        }
        if(cursor != null) {
            cursor.close();
        }
        return result;
    }

    private Date getBusinessDate(String transactionId) {
        DBCollection table = database.getCollection("rfsstream");
        DBObject requestQueryObject = new BasicDBObject();
        requestQueryObject.put("TRANSACTIONID", transactionId);

        DBCursor cursor = table.find(requestQueryObject);
        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            return (Date)object.get("BUSINESSDATE");
        }
        if(cursor != null) {
            cursor.close();
        }
        return null;
    }

    private DBCollection getCollectionFromRequest(String transactionId) {
        Date date = getBusinessDate(transactionId);
        if(date == null) throw new IllegalArgumentException("Collection not found for Transaction Id => " + transactionId);

        DateFormat gmtFormat = new SimpleDateFormat("MM-dd-yyyy");
        gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT+00"));
        String collectionName = "rfsrequest" + "_" + gmtFormat.format(date);
        return database.getCollection(collectionName);
    }

    List<RFSRate> parseRFSRate(DBObject object) {
        String guid = "N/A";
        Date date = null;
        if(object.containsField("GUID")) {
            guid = (String) object.get("GUID");
        }
        if(object.containsField("QUOTEDATETIME")) {
            date = (Date) object.get("QUOTEDATETIME");
        }

        BasicDBList ticks = (BasicDBList) object.get("QUOTEPRICES");
        return getRate(ticks, guid, date.getTime());
    }

    List<RFSRate> getRate(BasicDBList rateList, String guid, long timestamp) {
        List<RFSRate> result = new ArrayList<RFSRate>();
        if(rateList != null && !rateList.isEmpty()) {
            for (int i = 0; i < rateList.size(); i++) {
                DBObject rate = (DBObject) rateList.get(i);
                result.add(getTick(rate, guid, timestamp));
            }
        }
        return result;
    }

    RFSRate getTick(DBObject tick, String guid, long timestamp) {
        double bidRate = 0;
        double offerRate = 0;
        double volume = 0;

        if(tick.containsField("BIDRATE")) {
            bidRate = (double) tick.get("BIDRATE");
        }
        if(tick.containsField("OFFERRATE")) {
            offerRate = (double) tick.get("OFFERRATE");
        }
        if(tick.containsField("AMOUNT")) {
            volume = (double) tick.get("AMOUNT");
        }
        RFSRate rfsRate = new RFSRate();
        rfsRate.setAskPrice(offerRate);
        rfsRate.setBidPrice(bidRate);
        rfsRate.setGuid(guid);
        rfsRate.setTime(timestamp);
        rfsRate.setVolume(volume);
        return rfsRate;
    }
}
