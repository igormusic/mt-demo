package com.example.adapter.model;

public enum MessageStatus {
    REQUEST_ERROR("requestError"),
    REQUEST_SENT("requestSent"),
    RESPONSE_ERROR("responseError"),
    RESPONSE_RECEIVED("responseReceived");

    private final String value;

    MessageStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
