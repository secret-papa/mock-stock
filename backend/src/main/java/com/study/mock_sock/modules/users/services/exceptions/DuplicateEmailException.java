package com.study.mock_sock.modules.users.services.exceptions;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("이미 사용 중인 이메일입니다: " + email);
    }
}
