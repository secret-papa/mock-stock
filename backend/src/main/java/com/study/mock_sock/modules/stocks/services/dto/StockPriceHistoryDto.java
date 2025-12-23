package com.study.mock_sock.modules.stocks.services.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record StockPriceHistoryDto(
        String time,
        double price
) {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static StockPriceHistoryDto from(LocalDateTime recordedAt, double price) {
        return new StockPriceHistoryDto(recordedAt.format(formatter), price);
    }
}
