package com.integral.ds.emscope.auth.impl;

import com.integral.ds.dto.UserInfo;
import com.integral.ds.emscope.auth.AuthenticationService;
import com.integral.ds.emscope.auth.cache.UserInfoCache;
/**
 * Service for auth of EMScope application.
 *
 * @author Rahul Bhattacharjee
 */
public class DatabaseAuthenticationService implements AuthenticationService {

    private UserInfoCache cache;

    @Override
    public void init() {
    }

    @Override
    public boolean isLoginValid(String userName, String password,String organization) {
        if(userName == null || password == null) {
            return false;
        }
        UserInfo userInfo = cache.getUserInfo(userName);
        if(userInfo != null) {
            return password.equals(userInfo.getPassword());
        }
        return false;
    }

    public void setCache(UserInfoCache cache) {
        this.cache = cache;
    }
}
