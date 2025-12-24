package com.study.mock_sock.modules.stocks.services;

import com.study.mock_sock.modules.stocks.domains.Stock;
import com.study.mock_sock.modules.stocks.repositories.StockRepository;
import com.study.mock_sock.modules.stocks.services.dto.StockDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

    public Slice<StockDto> findAll(int page, int size, String exchange) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Slice<Stock> stocks;

        if (exchange == null || exchange.isBlank()) {
            stocks = stockRepository.findAllBy(pageRequest);
        } else {
            stocks = stockRepository.findByExchange(exchange, pageRequest);
        }

        List<StockDto> stockDtos = stocks.getContent().stream()
                .map(StockDto::from)
                .toList();

        return new SliceImpl<>(stockDtos, pageRequest, stocks.hasNext());
    }

    public List<String> getExchanges() {
        return stockRepository.findDistinctExchanges();
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
