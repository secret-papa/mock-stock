package com.study.mock_sock.modules.stocks.domains;

import com.study.mock_sock.modules.users.domains.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRADE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    private int quantity;
    private double price;
    private LocalDateTime tradeDate;

    @Builder
    public Trade(User user, Stock stock, TradeType tradeType, int quantity, double price) {
        this.user = user;
        this.stock = stock;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.tradeDate = LocalDateTime.now();
    }
}
