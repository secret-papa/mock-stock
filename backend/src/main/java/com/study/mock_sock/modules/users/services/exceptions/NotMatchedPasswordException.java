package com.study.mock_sock.modules.users.services.exceptions;

public class NotMatchedPasswordException extends RuntimeException {
    public NotMatchedPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
