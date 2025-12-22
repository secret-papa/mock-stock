package com.study.mock_sock.modules.stocks.services;

import com.study.mock_sock.modules.stocks.domains.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StockDataService {
    private final WebClient webClient;

    public Mono<Double> getStockPriceAsync(String ticker) {
        return webClient.get()
                .uri("/{ticker}?interval=1m&range=1m", ticker)
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonString -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(jsonString);
                        JsonNode result = root.path("chart").path("result").get(0);

                        if (result == null || result.isMissingNode()) {
                            throw new RuntimeException("Yahoo API 응답에 데이터가 없습니다.");
                        }

                        return result.path("meta").path("regularMarketPrice").asDouble();
                    } catch (Exception e) {
                        throw new RuntimeException(ticker + " 파싱 실패: " + e.getMessage());
                    }
                });
    }

    public Stock fetchRealTimeStock(String ticker) {
        String jsonString = webClient.get()
                .uri("/{ticker}?interval=1m&range=1m", ticker)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonString);
            JsonNode result = root.path("chart").path("result").get(0);

            if (result == null || result.isMissingNode()) {
                throw new RuntimeException("Yahoo API 응답에 데이터가 없습니다.");
            }

            JsonNode meta = result.path("meta");

            if (
                    meta.path("currency").isMissingNode() ||
                    !Objects.equals(meta.path("currency").asText(), "KRW") &&
                    !Objects.equals(meta.path("currency").asText(), "USD")) {
                System.out.println(meta.path("currency").asText() + ticker);
                throw new RuntimeException("지원하지 않는 통화입니다.");
            }

            return Stock.builder()
                    .ticker(ticker)
                    .name(meta.path("longName").asText(meta.path("sortName").asText("Unkwon")))
                    .currency(meta.path("currency").asText())
                    .exchange(meta.path("exchangeName").asText())
                    .currentPrice((int) meta.path("regularMarketPrice").asDouble())
                    .build();

        } catch (Exception e) {
           throw new RuntimeException("JSON 파싱 중 에러 발생: " + e.getMessage());
        }
    }
}
