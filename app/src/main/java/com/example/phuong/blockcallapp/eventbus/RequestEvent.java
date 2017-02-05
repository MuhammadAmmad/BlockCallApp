package com.example.phuong.blockcallapp.eventbus;

/**
 * Created by phuong on 05/02/2017.
 */

public class RequestEvent {
    private String request;

    public RequestEvent(String request) {
        this.request = request;
    }

    public RequestEvent() {
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
