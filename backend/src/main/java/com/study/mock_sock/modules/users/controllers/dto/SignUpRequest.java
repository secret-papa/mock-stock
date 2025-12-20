package com.study.mock_sock.modules.users.controllers.dto;

public class SignUpRequest {
    public String email;
    public String password;
    public String alias;

    public SignUpRequest(String email, String password, String alias) {
        this.email = email;
        this.password = password;
        this.alias = alias;
    }
}
