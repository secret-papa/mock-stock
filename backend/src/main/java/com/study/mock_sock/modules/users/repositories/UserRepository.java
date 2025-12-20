package com.study.mock_sock.modules.users.repositories;

import com.study.mock_sock.modules.users.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAlias(String alias);
}
