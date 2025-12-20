package com.study.mock_sock.modules.users.services.exceptions;

public class DuplicateAliasException extends RuntimeException {
    public DuplicateAliasException(String alias) {
        super("이미 사용 중인 닉네임입니다: " + alias);
    }
}
