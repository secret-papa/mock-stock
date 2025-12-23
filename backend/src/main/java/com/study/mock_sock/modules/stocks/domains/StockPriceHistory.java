package com.study.mock_sock.modules.stocks.domains;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_stock_id_time", columnList = "stock_id, recorded_at"),
})
public class StockPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STOCK_PRICE_HISTORY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private double price;

    private LocalDateTime recordedAt;

    @Builder
    public StockPriceHistory(Stock stock, double price) {
        this.stock = stock;
        this.price = price;
        this.recordedAt = LocalDateTime.now();
    }
}
