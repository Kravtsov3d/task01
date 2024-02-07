package com.task10.authentication.model;

public class SigninRequest {

    private String email;
    private String password;

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

    @Override
    public String toString() {
        return "SigninRequest{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
