package org.raven.mongodb;

import org.raven.mongodb.model.Orders;

public class OrdersRepositoryImpl extends MongoRepositoryImpl<Orders, Long> {

    public OrdersRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);
    }
}
