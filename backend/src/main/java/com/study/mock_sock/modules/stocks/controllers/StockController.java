package com.study.mock_sock.modules.stocks.controllers;

import com.study.mock_sock.modules.stocks.services.StockService;
import com.study.mock_sock.modules.stocks.services.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stocks")
public class StockController {
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockDto>> findAllStocks() {
        List<StockDto> stockDtos = stockService.findAll();
        return ResponseEntity.ok(stockDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StockDto>> searchStocks(@RequestParam String keyword) {
        List<StockDto> stockDtos = stockService.searchStocks(keyword);
        return ResponseEntity.ok(stockDtos);
    }
}
