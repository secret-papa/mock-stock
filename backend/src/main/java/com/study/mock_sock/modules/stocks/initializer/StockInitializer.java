package com.study.mock_sock.modules.stocks.initializer;

import com.study.mock_sock.modules.stocks.services.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockInitializer implements CommandLineRunner {
    private final StockService stockService;
    private final ResourceLoader resourceLoader;

    @Override
    public void run(String... args) throws Exception {
        Resource resource = (Resource) resourceLoader.getResource("classpath:tickers.txt");
        List<String> tickers = Files.readAllLines(Paths.get(resource.getURI()));
        int savedTickerSize = 0;

        for (String ticker : tickers) {
            try {
                stockService.fetchAndSaveRealTimeStock(ticker);
                savedTickerSize++;
                Thread.sleep(200); // API 과부하 방지를 위한 약간의 지연
            } catch (Exception e) {
                System.out.println("실패한 티커: " + ticker + " - " + e.getMessage());
            }
        }

        System.out.println("총 " + savedTickerSize + "개 종목 자동 초기화 완료!");
    }
}
