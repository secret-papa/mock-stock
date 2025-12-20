package com.study.mock_sock.modules.users.controllers;

import com.study.mock_sock.common.dto.ErrorResponse;
import com.study.mock_sock.modules.users.controllers.dto.SignUpRequest;
import com.study.mock_sock.modules.users.services.UserService;
import com.study.mock_sock.modules.users.services.commands.CreateUserCommand;
import com.study.mock_sock.modules.users.services.exceptions.DuplicateAliasException;
import com.study.mock_sock.modules.users.services.exceptions.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            CreateUserCommand command = new CreateUserCommand(request.email, request.password, request.alias);
            Long userId = userService.signup(command);

            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        } catch (DuplicateEmailException | DuplicateAliasException e) {
            ErrorResponse error = new ErrorResponse("DUPLICATE_USER_INFO", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
