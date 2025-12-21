package com.study.mock_sock.modules.stocks.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.mock_sock.modules.stocks.domains.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.mock_sock.modules.stocks.domains.QStock.stock;

@Repository
@RequiredArgsConstructor
public class StockRepositoryCustomImpl implements StockRepositoryCustom {
    private final JPAQueryFactory queryFactory; // Querydsl 전용 팩토리

    @Override
    public List<Stock> searchByKeyword(String keyword) {
        return queryFactory
                .selectFrom(stock)
                .where(
                        stock.name.containsIgnoreCase(keyword)
                        .or(stock.ticker.contains(keyword)))
                .fetch();
    }
}
