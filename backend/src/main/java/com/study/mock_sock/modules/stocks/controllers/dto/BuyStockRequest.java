package com.study.mock_sock.modules.stocks.controllers.dto;

public record BuyStockRequest(
        String ticker,
        int quantity
) {

}
