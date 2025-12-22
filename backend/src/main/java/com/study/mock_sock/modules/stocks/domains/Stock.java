package com.study.mock_sock.modules.stocks.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ticker;

    private String name;

    private String currency;

    private double currentPrice;

    private String exchange;

    @Builder
    public Stock(String ticker, String name, String currency, double currentPrice, String exchange) {
        this.ticker = ticker;
        this.name = name;
        this.currency = currency;
        this.currentPrice = currentPrice;
        this.exchange = exchange;
    }

    public void updatePrice(double newPrice) {
        this.currentPrice = newPrice;
    }
}
