package com.study.mock_sock.modules.stocks.services;

import com.study.mock_sock.modules.stocks.repositories.StockPriceHistoryRepository;
import com.study.mock_sock.modules.stocks.repositories.StockRepository;
import com.study.mock_sock.modules.stocks.services.dto.StockPriceHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockPriceHistoryService {
    private final StockRepository stockRepository;
    private final StockPriceHistoryRepository stockPriceHistoryRepository;

    public List<StockPriceHistoryDto> getStockChart(Long stockId, String range) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime since = calculateSince(range, now);

        long totalCount = stockPriceHistoryRepository.countByStockIdAndRecordedAtAfter(stockId, since);

        if (totalCount == 0) {
            return stockRepository.findById(stockId)
                    .map(stock -> List.of(StockPriceHistoryDto.from(now, stock.getCurrentPrice())))
                    .orElse(List.of());
        }

        if (totalCount < 30) {
            return stockPriceHistoryRepository.findAllByStockIdAndRecordedAtAfterOrderByRecordedAtAsc(stockId, since)
                    .stream()
                    .map(h -> StockPriceHistoryDto.from(h.getRecordedAt(), h.getPrice()))
                    .toList();
        }

        int interval = calculateInterval(totalCount);

        return stockPriceHistoryRepository.findSampledHistory(stockId, since, interval)
                .stream()
                .map(h -> StockPriceHistoryDto.from(h.getRecordedAt(), h.getPrice()))
                .toList();
    }

    private LocalDateTime calculateSince(String range, LocalDateTime now) {
        return switch (range.toLowerCase()) {
            case "1h" -> now.minusHours(1);
            case "1d" -> now.minusDays(1);
            case "1w" -> now.minusWeeks(1);
            case "1m" -> now.minusMonths(1);
            default -> now.minusHours(1); // 기본값 1시간
        };
    }

    /**
     * 목표 데이터 포인트(약 14개)를 맞추기 위한 간격 계산
     */
    private int calculateInterval(long totalCount) {
        // 데이터가 100개면 약 7개마다 1개씩 추출 (100 / 14 = 7.14)
        return (int) Math.max(1, totalCount / 14);
    }

    @Transactional
    public void deleteOldHistory(LocalDateTime dateTime) {
        stockPriceHistoryRepository.deleteOldHistory(dateTime);
    }
}
