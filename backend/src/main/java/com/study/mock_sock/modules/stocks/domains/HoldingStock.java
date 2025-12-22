package com.study.mock_sock.modules.stocks.domains;

import com.study.mock_sock.modules.users.domains.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoldingStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOLDING_STOCK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private int quantity;
    private double avgPrice;

    @Builder
    public HoldingStock(User user, Stock stock) {
        this.user = user;
        this.stock = stock;
        this.avgPrice = 0;
    }

    public void buy(double price, int quantity) {
        double totalCost = (this.avgPrice * this.quantity) + (price * quantity);
        this.quantity += quantity;
        this.avgPrice = totalCost / this.quantity;
    }

    public void sell(int quantity) {
        if (this.quantity < quantity) {
            throw new RuntimeException("보유 수량이 부족합니다.");
        }

        this.quantity -= quantity;

        if (this.quantity == 0) {
            this.avgPrice = 0.0;
        }
    }

    public double calculateProfit(double currentPrice) {
        return (currentPrice - this.avgPrice) * this.quantity;
    }

    public double calculateReturnRate(double currentPrice) {
        if (this.avgPrice == 0) return 0.0;
        return ((currentPrice / this.avgPrice) - 1.0) * 100.0;
    }
}
