package com.integral.ds.emscope.orders;

/**
 * @author Rahul Bhattacharjee
 */
public enum OrderStatus {

    A("Active Order"),F("Completely filled order"),P("Partially filled order"),X("Cancelled order"),Z("Expired order"),D("declined order");

    private String message;

    OrderStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
