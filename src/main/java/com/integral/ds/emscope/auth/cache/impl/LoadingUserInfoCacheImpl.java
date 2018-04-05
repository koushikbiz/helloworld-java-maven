package com.integral.ds.emscope.auth.cache.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.integral.ds.dto.UserInfo;
import com.integral.ds.emscope.auth.cache.UserInfoCache;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutionException;

/**
 * Loading user info cache implementation.
 *
 * @author Rahul Bhattacharjee
 */
public class LoadingUserInfoCacheImpl implements UserInfoCache {

    private static final Logger LOGGER = Logger.getLogger(LoadingUserInfoCacheImpl.class);

    private CacheLoader loader;
    private LoadingCache<String,UserInfo> USER_IDENTITY_CACHE = null;

    @Override
    public void initCache() {
        USER_IDENTITY_CACHE = CacheBuilder.newBuilder().maximumSize(100)
                              .build(loader);
    }

    @Override
    public UserInfo getUserInfo(String userName) {
        try {
            return USER_IDENTITY_CACHE.get(userName);
        } catch (ExecutionException e) {
            LOGGER.error("Exception while loading cache for username " + userName,e);
        }
        return null;
    }

    @Override
    public void removeUserInfo(String userName) {
        throw new UnsupportedOperationException("Remove to be applied to the cache through strategy.");
    }

    @Override
    public void putUserInfo(String userName, UserInfo userInfo) {
        throw new UnsupportedOperationException("Loading of user is delegated to the CacheLoader implementation.");
    }

    public void setLoader(CacheLoader loader) {
        this.loader = loader;
    }
}
