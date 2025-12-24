package com.study.mock_sock.modules.stocks.controllers;

import com.study.mock_sock.modules.stocks.controllers.dto.BuyStockRequest;
import com.study.mock_sock.modules.stocks.controllers.dto.SellStockRequest;
import com.study.mock_sock.modules.stocks.services.TradeService;
import com.study.mock_sock.modules.stocks.services.dto.HoldingStockDto;
import com.study.mock_sock.modules.stocks.services.dto.TradeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @GetMapping("/trades")
    public ResponseEntity<Slice<TradeDto>> getTrades(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Slice<TradeDto> trades = tradeService.listTrade(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(trades);
    }

    @PostMapping("/trades/buy")
    public ResponseEntity<?> buyStock(@RequestBody BuyStockRequest request, @AuthenticationPrincipal Long userId) {
        tradeService.buyStock(request.ticker(), request.quantity(), userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/trades/sell")
    public ResponseEntity<?> sellStock(@RequestBody SellStockRequest request, @AuthenticationPrincipal Long userId) {
        tradeService.sellStock(request.ticker(), request.quantity(), userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/portfolio")
    public ResponseEntity<List<HoldingStockDto>> listHoldingStock(@AuthenticationPrincipal Long userId) {
        List<HoldingStockDto> holdingStocks = tradeService.listHoldingStock(userId);

        return ResponseEntity.status(HttpStatus.OK).body(holdingStocks);
    }
}
