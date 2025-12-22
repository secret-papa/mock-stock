package com.study.mock_sock.modules.stocks.repositories;

import com.study.mock_sock.modules.stocks.domains.HoldingStock;
import com.study.mock_sock.modules.stocks.domains.Stock;
import com.study.mock_sock.modules.users.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingStockRepository extends JpaRepository<HoldingStock, Long> {
    Optional<HoldingStock> findByUserAndStock(User user, Stock stock);
    List<HoldingStock> findAllByUserId(Long userId);
}
