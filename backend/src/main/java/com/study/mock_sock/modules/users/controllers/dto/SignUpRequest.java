package com.study.mock_sock.modules.users.controllers.dto;

public record SignUpRequest(
       String email,
       String password,
       String alias
) { }
