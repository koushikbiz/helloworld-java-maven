package com.integral.ds.emscope.auth;

/**
 * Basic auth service interface , verifies the user credentials with user passed data.
 *
 * @author Rahul Bhattacharjee
 */
public interface AuthenticationService {

    public static final String USERNAME_KEY = "UserName";
    public static final String PASSWORD_KEY = "Password";
    public static final String ORGANIZATION_KEY = "Organization";

    /**
     * Service initialize code , if any.
     */
    public void init();

    /**
     * Verifies if the passes credentials are valid.
     *
     * @param userName
     * @param password
     * @return
     */
    public boolean isLoginValid(String userName, String password,String organization);
}
