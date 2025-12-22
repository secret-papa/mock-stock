package com.study.mock_sock.modules.stocks.domains;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHoldingStock is a Querydsl query type for HoldingStock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHoldingStock extends EntityPathBase<HoldingStock> {

    private static final long serialVersionUID = -724739211L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHoldingStock holdingStock = new QHoldingStock("holdingStock");

    public final NumberPath<Double> avgPrice = createNumber("avgPrice", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final QStock stock;

    public final com.study.mock_sock.modules.users.domains.QUser user;

    public QHoldingStock(String variable) {
        this(HoldingStock.class, forVariable(variable), INITS);
    }

    public QHoldingStock(Path<? extends HoldingStock> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHoldingStock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHoldingStock(PathMetadata metadata, PathInits inits) {
        this(HoldingStock.class, metadata, inits);
    }

    public QHoldingStock(Class<? extends HoldingStock> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.stock = inits.isInitialized("stock") ? new QStock(forProperty("stock")) : null;
        this.user = inits.isInitialized("user") ? new com.study.mock_sock.modules.users.domains.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

