package com.study.mock_sock.modules.users.controllers.dto;

import lombok.Builder;

public record GetMyInfoResponse(
        Long id,
        String email,
        String alias,
        int balance
) {
    @Builder
    public GetMyInfoResponse(Long id, String email, String alias, int balance) {
        this.id = id;
        this.email = email;
        this.alias = alias;
        this.balance = balance;
    }
}
