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

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User owner;

    @Builder
    public Account(int balance) {
        this.balance = balance;
    }

    public void withdraw(int amount) {
        if (this.balance < amount) {
            throw new RuntimeException("잔액이 부족하여 주식을 살 수 없습니다.");
        }
        this.balance -= amount;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }
}
