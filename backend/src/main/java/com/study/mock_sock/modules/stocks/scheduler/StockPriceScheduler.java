package com.study.mock_sock.modules.stocks.scheduler;

import com.study.mock_sock.modules.stocks.domains.Stock;
import com.study.mock_sock.modules.stocks.domains.StockPriceHistory;
import com.study.mock_sock.modules.stocks.repositories.StockPriceHistoryRepository;
import com.study.mock_sock.modules.stocks.repositories.StockRepository;
import com.study.mock_sock.modules.stocks.services.StockDataService;
import com.study.mock_sock.modules.stocks.services.StockPriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockPriceScheduler {
    private final StockDataService stockDataService;
    private final StockPriceHistoryService stockPriceHistoryService;
    private final StockRepository stockRepository;
    private final StockPriceHistoryRepository stockPriceHistoryRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateAllStockPrices() {
        List<Stock> stocks = stockRepository.findAll();

        Flux.fromIterable(stocks)
                .delayElements(Duration.ofMillis(500))
                .flatMap(stock ->
                        stockDataService.getStockPriceAsync(stock.getTicker())
                                .publishOn(Schedulers.boundedElastic())
                                .doOnNext(newPrice -> {
                                    if (newPrice > 0) {
                                        stock.updatePrice(newPrice);
                                        stockPriceHistoryRepository.save(
                                                StockPriceHistory.builder()
                                                        .stock(stock)
                                                        .price(newPrice)
                                                        .build()
                                        );
                                        System.out.println("[가격 갱신] " + stock.getName() + ": "  + newPrice + stock.getCurrency());
                                    }
                                })
                )
                .blockLast();
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupOldData() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        stockPriceHistoryService.deleteOldHistory(oneMonthAgo);
    }
}
