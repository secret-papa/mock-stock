package com.study.mock_sock.modules.stocks.services.dto;

import com.study.mock_sock.modules.stocks.domains.HoldingStock;
import lombok.Builder;

@Builder
public record HoldingStockDto(
        Long stockId,
        String name,
        String ticker,
        int quantity,
        String currency,
        double avgPrice,
        double currentPrice,
        double profit,
        double returnRate
) {
    public static HoldingStockDto from(HoldingStock holdingStock) {
        return new HoldingStockDto(
                holdingStock.getStock().getId(),
                holdingStock.getStock().getName(),
                holdingStock.getStock().getTicker(),
                holdingStock.getQuantity(),
                holdingStock.getStock().getCurrency(),
                holdingStock.getAvgPrice(),
                holdingStock.getStock().getCurrentPrice(),
                holdingStock.calculateProfit(holdingStock.getStock().getCurrentPrice()),
                holdingStock.calculateReturnRate(holdingStock.getStock().getCurrentPrice())
        );
    }
}
