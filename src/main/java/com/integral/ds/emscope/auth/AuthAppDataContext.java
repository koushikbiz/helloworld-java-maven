package com.integral.ds.emscope.auth;

/**
 * authentication context.
 *
 * @author Rahul Bhattacharjee
 */
public class AuthAppDataContext {

    private AuthenticationService authenticationService;

	public boolean isLoginValid (String userName, String password,String organization)	{
        return authenticationService.isLoginValid(userName,password,organization);
	}

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
