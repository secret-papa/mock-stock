package com.study.mock_sock.modules.stocks.controllers;

import com.study.mock_sock.modules.stocks.services.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exchange")
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping
    public ResponseEntity<Double> getExchangeRate() {
        double exchangeRate = exchangeService.getCurrentExchangeRate();
        return ResponseEntity.status(HttpStatus.OK).body(exchangeRate);
    }
}
