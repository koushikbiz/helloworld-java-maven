package com.integral.ds.emscope.auth.cache;

import com.integral.ds.dto.UserInfo;

/**
 * User Info cache.
 *
 * @author Rahul Bhattacharjee
 */
public interface UserInfoCache {

    public void initCache();

    public UserInfo getUserInfo(String userName);

    public void removeUserInfo(String userName);

    public void putUserInfo(String userName,UserInfo userInfo);

}
