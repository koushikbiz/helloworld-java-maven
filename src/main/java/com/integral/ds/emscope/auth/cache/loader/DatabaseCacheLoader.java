package com.integral.ds.emscope.auth.cache.loader;

import com.google.common.cache.CacheLoader;
import com.integral.ds.dao.AuthDAO;
import com.integral.ds.dto.UserInfo;
import org.apache.log4j.Logger;

/**
 * Fetches user identity using auth dao.
 *
 * @author Rahul Bhattacharjee
 */
public class DatabaseCacheLoader extends CacheLoader<String,UserInfo> {

    private static final Logger LOGGER = Logger.getLogger(DatabaseCacheLoader.class);

    private AuthDAO authDAO;

    @Override
    public UserInfo load(String userName) throws Exception {
        LOGGER.debug("Trying to fetch user details for " + userName);
        return authDAO.getUserInfoForUser(userName);
    }

    public void setAuthDAO(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }
}
