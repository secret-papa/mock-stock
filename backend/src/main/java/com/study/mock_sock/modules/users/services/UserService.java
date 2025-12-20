package com.study.mock_sock.modules.users.services;

import com.study.mock_sock.common.auth.JwtProvider;
import com.study.mock_sock.modules.users.domains.Account;
import com.study.mock_sock.modules.users.domains.User;
import com.study.mock_sock.modules.users.repositories.UserRepository;
import com.study.mock_sock.modules.users.services.commands.CreateUserCommand;
import com.study.mock_sock.modules.users.services.exceptions.DuplicateAliasException;
import com.study.mock_sock.modules.users.services.exceptions.DuplicateEmailException;
import com.study.mock_sock.modules.users.services.exceptions.NotFoundUserException;
import com.study.mock_sock.modules.users.services.exceptions.NotMatchedPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signup(CreateUserCommand command) {
        if(userRepository.findByEmail(command.email).isPresent()) {
            throw new DuplicateEmailException(command.email);
        }

        if (userRepository.findByAlias(command.alias).isPresent()) {
            throw new DuplicateAliasException(command.alias);
        }

        String encodedPassword = passwordEncoder.encode(command.password);

        User user = User.builder()
                .email(command.email)
                .password(encodedPassword)
                .alias(command.alias)
                .build();

        Account account = Account.builder()
                .balance(100_000_000)
                .build();

        user.linkAccount(account);

        return userRepository.save(user).getId();
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotMatchedPasswordException();
        }

        return jwtProvider.createToken(user.getId(), user.getEmail(), user.getAlias());
    }

    public User getMyInfo(Long userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
    }
}
