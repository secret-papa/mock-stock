package com.study.mock_sock.modules.users.controllers;

import com.study.mock_sock.common.dto.ErrorResponse;
import com.study.mock_sock.modules.users.controllers.dto.GetMyInfoResponse;
import com.study.mock_sock.modules.users.controllers.dto.LoginRequest;
import com.study.mock_sock.modules.users.controllers.dto.LoginResponse;
import com.study.mock_sock.modules.users.controllers.dto.SignUpRequest;
import com.study.mock_sock.modules.users.domains.User;
import com.study.mock_sock.modules.users.services.UserService;
import com.study.mock_sock.modules.users.services.commands.CreateUserCommand;
import com.study.mock_sock.modules.users.services.exceptions.DuplicateAliasException;
import com.study.mock_sock.modules.users.services.exceptions.DuplicateEmailException;
import com.study.mock_sock.modules.users.services.exceptions.NotFoundUserException;
import com.study.mock_sock.modules.users.services.exceptions.NotMatchedPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            CreateUserCommand command = new CreateUserCommand(request.email(), request.password(), request.alias());
            Long userId = userService.signup(command);

            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        } catch (DuplicateEmailException | DuplicateAliasException e) {
            ErrorResponse error = new ErrorResponse("DUPLICATE_USER_INFO", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.login(request.email(), request.password());
            LoginResponse response = new LoginResponse(token);

        return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundUserException e) {
            ErrorResponse error = new ErrorResponse("USER_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (NotMatchedPasswordException e) {
            ErrorResponse error = new ErrorResponse("NOT_MATCHED_PASSWORD", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Long userId) {
        try {
            User user = userService.getMyInfo(userId);

            GetMyInfoResponse response = GetMyInfoResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .alias(user.getAlias())
                    .balance(user.getAccount().getBalance())
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundUserException e) {
            ErrorResponse error = new ErrorResponse("USER_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
