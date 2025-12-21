package com.study.mock_sock.modules.stocks.services.dto;

import com.study.mock_sock.modules.stocks.domains.Stock;

public record StockDto(
        Long id,
        String ticker,
        String name,
        String currency,
        int currentPrice,
        String exchange
) {
    public static StockDto from(Stock stock) {
        return new StockDto(
                stock.getId(),
                stock.getTicker(),
                stock.getName(),
                stock.getCurrency(),
                stock.getCurrentPrice(),
                stock.getExchange()
        );
    }
}
