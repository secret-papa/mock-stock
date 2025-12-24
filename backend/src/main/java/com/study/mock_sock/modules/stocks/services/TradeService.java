package com.study.mock_sock.modules.stocks.services;

import com.study.mock_sock.modules.stocks.domains.HoldingStock;
import com.study.mock_sock.modules.stocks.domains.Stock;
import com.study.mock_sock.modules.stocks.domains.Trade;
import com.study.mock_sock.modules.stocks.domains.TradeType;
import com.study.mock_sock.modules.stocks.repositories.HoldingStockRepository;
import com.study.mock_sock.modules.stocks.repositories.StockRepository;
import com.study.mock_sock.modules.stocks.repositories.TradeRepository;
import com.study.mock_sock.modules.stocks.services.dto.HoldingStockDto;
import com.study.mock_sock.modules.stocks.services.dto.TradeDto;
import com.study.mock_sock.modules.users.domains.User;
import com.study.mock_sock.modules.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeService {
    private final ExchangeService exchangeService;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final TradeRepository tradeRepository;
    private final HoldingStockRepository holdingStockRepository;

    public Slice<TradeDto> listTrade(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("tradeDate").descending());

        Slice<Trade> tradeSlice = tradeRepository.findAllByUserId(userId, pageable);

        return tradeSlice.map(TradeDto::from);
    }

    @Transactional
    public void buyStock(String ticker, int quantity, Long userId) {
        Stock stock = stockRepository.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        int price_KRW = exchangeService.convertToKRW(stock.getCurrency(), stock.getCurrentPrice());
        int totalCost = price_KRW * quantity;

        if (user.getAccount().getBalance() < totalCost) {
            throw new RuntimeException("잔액이 부족하여 매수할 수 없습니다.");
        }

        user.getAccount().withdraw(totalCost);

        Trade trade = Trade.builder()
                .user(user)
                .stock(stock)
                .tradeType(TradeType.BUY)
                .quantity(quantity)
                .price(stock.getCurrentPrice())
                .build();

        tradeRepository.save(trade);

        HoldingStock holdingStock = holdingStockRepository.findByUserAndStock(user, stock)
                .orElse(
                        HoldingStock.builder()
                                .user(user)
                                .stock(stock)
                                .build()
                );

        holdingStock.buy(stock.getCurrentPrice(), quantity);
        holdingStockRepository.save(holdingStock);
    }

    @Transactional
    public void sellStock(String ticker, int quantity, Long userId) {
        Stock stock = stockRepository.findByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 주식입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        HoldingStock holdingStock = holdingStockRepository.findByUserAndStock(user, stock)
                .orElseThrow(()-> new RuntimeException("소유한 주식이 없습니다."));

        int price_KRW = exchangeService.convertToKRW(stock.getCurrency(), stock.getCurrentPrice());
        int totalCost = price_KRW * quantity;

        user.getAccount().deposit(totalCost);
        holdingStock.sell(quantity);

        if (holdingStock.getQuantity() == 0) {
            holdingStockRepository.delete(holdingStock);
        }

        Trade trade = Trade.builder()
                .user(user)
                .stock(stock)
                .tradeType(TradeType.SELL)
                .quantity(quantity)
                .price(stock.getCurrentPrice())
                .build();

        tradeRepository.save(trade);
    }

    public List<HoldingStockDto> listHoldingStock(Long userId) {
        List<HoldingStock> holdingStocks = holdingStockRepository.findAllByUserId(userId);
        return holdingStocks.stream()
                .map(holdingStock -> HoldingStockDto.builder()
                        .stockId(holdingStock.getStock().getId())
                        .name(holdingStock.getStock().getName())
                        .ticker(holdingStock.getStock().getTicker())
                        .quantity(holdingStock.getQuantity())
                        .currency(holdingStock.getStock().getCurrency())
                        .avgPrice(holdingStock.getAvgPrice())
                        .currentPrice(holdingStock.getStock().getCurrentPrice())
                        .profit(holdingStock.calculateProfit(holdingStock.getStock().getCurrentPrice()))
                        .returnRate(holdingStock.calculateReturnRate(holdingStock.getStock().getCurrentPrice())).build())
                .toList();
    }
}
