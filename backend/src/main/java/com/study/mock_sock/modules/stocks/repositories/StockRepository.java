package com.study.mock_sock.modules.stocks.repositories;

import com.study.mock_sock.modules.stocks.domains.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {
    Optional<Stock> findByTicker(String ticker);
    Slice<Stock> findAllBy(Pageable pageable);
    Slice<Stock> findByExchange(String exchange, Pageable pageable);

    @Query("SELECT DISTINCT s.exchange FROM Stock s WHERE s.exchange IS NOT NULL ORDER BY s.exchange")
    List<String> findDistinctExchanges();
}
