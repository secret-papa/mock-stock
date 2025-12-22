package com.study.mock_sock.modules.stocks.repositories;

import com.study.mock_sock.modules.stocks.domains.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findAllByUserIdOrderByTradeDateDesc(Long userId);
}
