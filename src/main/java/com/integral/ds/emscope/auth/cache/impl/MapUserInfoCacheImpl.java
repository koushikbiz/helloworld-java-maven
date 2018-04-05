package com.integral.ds.emscope.auth.cache.impl;

import com.integral.ds.dto.UserInfo;
import com.integral.ds.emscope.auth.cache.UserInfoCache;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap based user info cache. Hard ref.
 *
 * @author Rahul Bhattacharjee
 */
public class MapUserInfoCacheImpl implements UserInfoCache {

    private Map<String,UserInfo> CACHE = null;

    @Override
    public void initCache() {
        CACHE = new HashMap<String,UserInfo>();
    }

    @Override
    public UserInfo getUserInfo(String userName) {
        return CACHE.get(userName);
    }

    @Override
    public void removeUserInfo(String userName) {
        CACHE.remove(userName);
    }

    @Override
    public void putUserInfo(String userName, UserInfo userInfo) {
        CACHE.put(userName,userInfo);
    }
}
