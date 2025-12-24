package com.study.mock_sock.modules.stocks.controllers;

import com.study.mock_sock.modules.stocks.services.StockPriceHistoryService;
import com.study.mock_sock.modules.stocks.services.StockService;
import com.study.mock_sock.modules.stocks.services.dto.StockDto;
import com.study.mock_sock.modules.stocks.services.dto.StockPriceHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;
    private final StockPriceHistoryService stockPriceHistoryService;

    @GetMapping
    public ResponseEntity<Slice<StockDto>> findAllStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String exchange) {
        Slice<StockDto> stockDtos = stockService.findAll(page, size, exchange);
        return ResponseEntity.ok(stockDtos);
    }

    @GetMapping("/exchanges")
    public ResponseEntity<List<String>> getExchanges() {
        List<String> exchanges = stockService.getExchanges();
        return ResponseEntity.ok(exchanges);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StockDto>> searchStocks(@RequestParam String keyword) {
        List<StockDto> stockDtos = stockService.searchStocks(keyword);
        return ResponseEntity.ok(stockDtos);
    }

    @GetMapping("/{stockId}/chart")
    public ResponseEntity<List<StockPriceHistoryDto>> getChart(
            @PathVariable Long stockId,
            @RequestParam(defaultValue = "1h") String range) {

        List<StockPriceHistoryDto> chartData = stockPriceHistoryService.getStockChart(stockId, range);
        return ResponseEntity.ok(chartData);
    }
}
