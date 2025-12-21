package com.study.mock_sock.modules.stocks.services;

import com.study.mock_sock.modules.stocks.domains.Stock;
import com.study.mock_sock.modules.stocks.repositories.StockRepository;
import com.study.mock_sock.modules.stocks.services.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockDataService stockDataService;

    @Transactional
    public void fetchAndSaveRealTimeStock(String ticker) {
        Stock stock = stockDataService.fetchRealTimeStock(ticker);
        stockRepository.save(stock);
    }

    public List<StockDto> findAll() {
        List<Stock> stocks = stockRepository.findAll();

        return stocks.stream()
                .map(StockDto::from)
                .toList();
    }

    @Transactional
    public List<StockDto> searchStocks(String keyword) {
        List<Stock> stocks = stockRepository.searchByKeyword(keyword);

        if (stocks.isEmpty() && isTickerPattern(keyword)) {
            try {
                Stock newStock = stockDataService.fetchRealTimeStock(keyword);
                stockRepository.save(newStock);
                return List.of(StockDto.from(newStock));
            } catch (Exception e) {
                return List.of();
            }
        }

        return stocks.stream()
                .map(StockDto::from)
                .toList();
    }

    private boolean isTickerPattern(String keyword) {
        return keyword.matches("^[A-Z0-9.-]+$");
    }
}
