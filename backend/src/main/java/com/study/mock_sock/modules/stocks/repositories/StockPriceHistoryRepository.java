package com.study.mock_sock.modules.stocks.repositories;

import com.study.mock_sock.modules.stocks.domains.StockPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface StockPriceHistoryRepository extends JpaRepository<StockPriceHistory, Long> {
    List<StockPriceHistory> findAllByStockIdAndRecordedAtAfterOrderByRecordedAtAsc(
            Long stockId,
            LocalDateTime recordedAt
    );

    @Query("SELECT COUNT(s) from StockPriceHistory s WHERE s.stock.id = :stockId AND s.recordedAt >= :since")
    long countByStockIdAndRecordedAtAfter(@Param("stockId") Long stockId, @Param("since") LocalDateTime since);

    @Query(value = "SELECT stock_price_history_id, stock_id, price, recorded_at FROM (" +
            "  SELECT *, ROW_NUMBER() OVER (ORDER BY recorded_at ASC) as rn " +
            "  FROM stock_price_history " +
            "  WHERE stock_id = :stockId AND recorded_at >= :since" +
            ") AS numbered " +
            "WHERE numbered.rn % :samplingInterval = 0 " +
            "ORDER BY recorded_at ASC", nativeQuery = true)
    List<StockPriceHistory> findSampledHistory(
            @Param("stockId") Long stockId,
            @Param("since") LocalDateTime since,
            @Param("samplingInterval") int samplingInterval
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM stock_price_history WHERE recorded_at < :dateTime LIMIT 5000", nativeQuery = true)
    int deleteOldHistory(@Param("dateTime") LocalDateTime dateTime);
}
