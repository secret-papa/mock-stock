package com.study.mock_sock.modules.users.controllers.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetMyInfoResponse {
    public Long id;
    public String email;
    public String alias;
    public int balance;
    public int stockValuationAmount;
}
