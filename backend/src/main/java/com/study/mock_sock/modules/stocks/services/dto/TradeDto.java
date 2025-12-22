package com.study.mock_sock.modules.stocks.services.dto;

import com.study.mock_sock.modules.stocks.domains.Trade;
import com.study.mock_sock.modules.stocks.domains.TradeType;

import java.time.LocalDateTime;

public record TradeDto(
    String name,
    String ticker,
    int quantity,
    String currency,
    double price,
    TradeType tradeType,
    LocalDateTime tradeDate
) {
    public static TradeDto from(Trade trade) {
        return new TradeDto(
                trade.getStock().getName(),
                trade.getStock().getTicker(),
                trade.getQuantity(),
                trade.getStock().getCurrency(),
                trade.getPrice(),
                trade.getTradeType(),
                trade.getTradeDate()
                );
    }
}
