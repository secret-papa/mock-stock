package com.study.mock_sock.modules.stocks.domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStockPriceHistory is a Querydsl query type for StockPriceHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStockPriceHistory extends EntityPathBase<StockPriceHistory> {

    private static final long serialVersionUID = 548328735L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStockPriceHistory stockPriceHistory = new QStockPriceHistory("stockPriceHistory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> price = createNumber("price", Double.class);

    public final DateTimePath<java.time.LocalDateTime> recordedAt = createDateTime("recordedAt", java.time.LocalDateTime.class);

    public final QStock stock;

    public QStockPriceHistory(String variable) {
        this(StockPriceHistory.class, forVariable(variable), INITS);
    }

    public QStockPriceHistory(Path<? extends StockPriceHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStockPriceHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStockPriceHistory(PathMetadata metadata, PathInits inits) {
        this(StockPriceHistory.class, metadata, inits);
    }

    public QStockPriceHistory(Class<? extends StockPriceHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.stock = inits.isInitialized("stock") ? new QStock(forProperty("stock")) : null;
    }

}

