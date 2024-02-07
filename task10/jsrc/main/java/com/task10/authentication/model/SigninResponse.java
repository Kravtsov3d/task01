package com.task10.authentication.model;

public class SigninResponse {

    private final String accessToken;

    public SigninResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
