package com.task10.authentication.model;

public class SigninRequest {

    private String email;
    private String password;
    private String clientId;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "SigninRequest{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", clientId='" + clientId + '\'' +
            '}';
    }
}
