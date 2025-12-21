package com.study.mock_sock.modules.stocks.scheduler;

import com.study.mock_sock.modules.stocks.domains.Stock;
import com.study.mock_sock.modules.stocks.repositories.StockRepository;
import com.study.mock_sock.modules.stocks.services.StockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockPriceScheduler {
    private final StockRepository stockRepository;
    private final StockDataService stockDataService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateAllStockPrices() {
        List<Stock> stocks = stockRepository.findAll();

        Flux.fromIterable(stocks)
                .delayElements(Duration.ofMillis(500))
                .flatMap(stock ->
                        stockDataService.getStockPriceAsync(stock.getTicker())
                                .doOnNext(newPrice -> {
                                    if (newPrice > 0) {
                                        stock.updatePrice(newPrice);
                                        System.out.println("[가격 갱신] " + stock.getName() + ": "  + newPrice + stock.getCurrency());
                                    }
                                })
                )
                .blockLast();
    }
}
