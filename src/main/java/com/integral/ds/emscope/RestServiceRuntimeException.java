package com.integral.ds.emscope;

import com.integral.ds.model.ErrorObject;

/**
 * Exception for all the REST services.
 *
 * @author Rahul Bhattacharjee
 */
public class RestServiceRuntimeException extends RuntimeException {

    private ErrorObject errorObject;

    public RestServiceRuntimeException(ErrorObject errorObject , String message) {
        super(message);
        this.errorObject = errorObject;
    }

    public ErrorObject getErrorObject() {
        return errorObject;
    }
}
