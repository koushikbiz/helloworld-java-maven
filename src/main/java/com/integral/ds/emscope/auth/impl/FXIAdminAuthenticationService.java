package com.integral.ds.emscope.auth.impl;

import com.integral.ds.dto.UserInfo;
import com.integral.ds.emscope.auth.AuthenticationService;
import com.integral.ds.emscope.auth.cache.UserInfoCache;
import com.integral.ds.serializer.Serializer;
import com.integral.ds.util.PropertyReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 * FXI admin login service based authentication service.
 *
 * @author Rahul Bhattacharjee
 */
public class FXIAdminAuthenticationService implements AuthenticationService {

    private static final Logger LOGGER = Logger.getLogger(FXIAdminAuthenticationService.class);

    private static final String ENCODING = "application/json";

    private static String LOGIN_REST_URL = null;
    private static String LOGOUT_REST_URL = null;

    private Serializer serializer;
    private AuthenticationService fallBackService;
    private UserInfoCache cache;

    @Override
    public void init() {
        String serverUrl = PropertyReader.getPropertyValue(PropertyReader.KEY_AUTH_SERVER_URL);
        LOGIN_REST_URL = serverUrl + "/fxi/admin/auth/login";
        LOGOUT_REST_URL = serverUrl + "/fxi/admin/auth/logout";
    }

    @Override
    public boolean isLoginValid(String userName, String password, String organization) {
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return false;
        }
        boolean isSuccessful = isValidLoginUsingRemoteCall(userName,password,organization);
        if(isSuccessful) {
           return isSuccessful;
        } else {
           if(fallBackService != null) {
              if(LOGGER.isInfoEnabled()) {
                 LOGGER.info("Using fallback authentication service for authentication user " + userName);
              }
              return fallBackService.isLoginValid(userName,password,organization);
           }
        }
        return false;
    }

    private boolean isValidLoginUsingRemoteCall(String userName,String password,String organization) {
        UserInfo userInfo = cache.getUserInfo(userName);

        if(userInfo != null) {
            if(StringUtils.isNotBlank(organization)){
                return organization.equals(userInfo.getOrganization()) && password.equals(userInfo.getPassword());
            } else {
                return false;
            }
        }

        LoginInfo user = new LoginInfo(userName,password,organization);
        boolean isSuccessful = attemptToLogin(user);
        if(isSuccessful) {
            putInCache(user);
            return true;
        }
        return false;
    }

    private boolean attemptToLogin(LoginInfo user) {
        boolean isSuccessful = false;
        try {
            try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
                isSuccessful = login(client,user);
                logout(client);
            }
        }catch (Exception e) {
            LOGGER.warn("Exception while logging in user using fxi admin , falling back to database.",e);
        }
        return isSuccessful;
    }

    private void putInCache(LoginInfo user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getUser());
        userInfo.setPassword(user.getPass());
        userInfo.setOrganization(user.getOrg());
        cache.putUserInfo(user.getUser(),userInfo);
    }

    private boolean login(HttpClient client, LoginInfo user) throws Exception {
        String postContent = serializer.serialize(user);
        StringEntity stringEntryForPost = new StringEntity(postContent);
        stringEntryForPost.setContentType(ENCODING);

        HttpPost loginPostReq = new HttpPost(LOGIN_REST_URL);
        if (stringEntryForPost != null) {
            loginPostReq.setEntity(stringEntryForPost);
        }
        HttpResponse response = client.execute(loginPostReq);
        return response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK ? true : false;
    }

    private void logout(HttpClient client) {
        HttpPost logoutPostReq = new HttpPost(LOGOUT_REST_URL);
        try {
            client.execute(logoutPostReq);
        } catch (Exception e) {
            LOGGER.warn("Logout failed.Ignoring this rest call.",e);
        }
    }

    private static class LoginInfo {
        private String user;
        private String pass;
        private String org;

        private LoginInfo(String user, String pass, String org) {
            this.user = user;
            this.pass = pass;
            this.org = org;
        }

        private String getUser() {
            return user;
        }

        private LoginInfo setUser(String user) {
            this.user = user;
            return this;
        }

        private String getPass() {
            return pass;
        }

        private LoginInfo setPass(String password) {
            this.pass = password;
            return this;
        }

        private String getOrg() {
            return org;
        }

        private LoginInfo setOrg(String org) {
            this.org = org;
            return this;
        }
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public void setFallBackService(AuthenticationService fallBackService) {
        this.fallBackService = fallBackService;
    }

    public void setCache(UserInfoCache cache) {
        this.cache = cache;
    }
}
