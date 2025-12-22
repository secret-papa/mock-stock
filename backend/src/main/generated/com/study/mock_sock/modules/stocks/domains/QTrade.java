package com.study.mock_sock.modules.stocks.domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTrade is a Querydsl query type for Trade
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrade extends EntityPathBase<Trade> {

    private static final long serialVersionUID = -1050455486L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTrade trade = new QTrade("trade");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> price = createNumber("price", Double.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final QStock stock;

    public final DateTimePath<java.time.LocalDateTime> tradeDate = createDateTime("tradeDate", java.time.LocalDateTime.class);

    public final EnumPath<TradeType> tradeType = createEnum("tradeType", TradeType.class);

    public final com.study.mock_sock.modules.users.domains.QUser user;

    public QTrade(String variable) {
        this(Trade.class, forVariable(variable), INITS);
    }

    public QTrade(Path<? extends Trade> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTrade(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTrade(PathMetadata metadata, PathInits inits) {
        this(Trade.class, metadata, inits);
    }

    public QTrade(Class<? extends Trade> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.stock = inits.isInitialized("stock") ? new QStock(forProperty("stock")) : null;
        this.user = inits.isInitialized("user") ? new com.study.mock_sock.modules.users.domains.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

