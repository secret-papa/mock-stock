package com.study.mock_sock.modules.stocks.repositories;

import com.study.mock_sock.modules.stocks.domains.Stock;

import java.util.List;

public interface StockRepositoryCustom {
    List<Stock> searchByKeyword(String keyword);
}
