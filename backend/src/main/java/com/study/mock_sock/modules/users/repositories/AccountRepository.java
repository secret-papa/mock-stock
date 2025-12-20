package com.study.mock_sock.modules.users.repositories;

import com.study.mock_sock.modules.users.domains.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
