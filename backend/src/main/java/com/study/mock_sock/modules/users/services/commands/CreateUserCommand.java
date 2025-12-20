package com.study.mock_sock.modules.users.services.commands;

public class CreateUserCommand {
    public String email;
    public String password;
    public String alias;

    public CreateUserCommand(String email, String password, String alias) {
        this.email = email;
        this.password = password;
        this.alias = alias;
    }
}
