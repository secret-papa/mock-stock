package com.study.mock_sock.modules.users.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    public Long id;
    private int balance;
    private int stockValuationAmount;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User owner;

    @Builder
    public Account(int balance, int stockValuationAmount, User owner) {
        this.balance = balance;
        this.stockValuationAmount = stockValuationAmount;
    }
}
