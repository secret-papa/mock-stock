package com.study.mock_sock.modules.stocks.repositories;

import com.study.mock_sock.modules.stocks.domains.Trade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    Slice<Trade> findAllByUserId(Long userId, Pageable pageable);
}
