package com.study.mock_sock.modules.stocks.controllers.dto;

public record SellStockRequest(
        String ticker,
        int quantity
) {
}
