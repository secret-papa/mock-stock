package com.study.mock_sock.modules.stocks.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExchangeService {
    private final StockDataService stockDataService;
    @Getter
    private double currentExchangeRate = 1350; // 기본값

    @Scheduled(fixedDelay = 3600000) // 1시간마다 환율 업데이트
    public void updateExchangeRate() {
        stockDataService.getStockPriceAsync("USDKRW=X")
                .subscribe(rate -> {
                    if (rate > 0) {
                        this.currentExchangeRate = rate;
                        System.out.println("현재 환율 업데이트 완료: " + this.currentExchangeRate);
                    }
                });
    }

    public int convertToKRW(String currency, double price) {
        if ("USD".equals(currency)) {
            return (int) (price * currentExchangeRate);
        }

        return (int) price;
    }
}
