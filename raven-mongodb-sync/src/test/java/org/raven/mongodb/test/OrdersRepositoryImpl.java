package org.raven.mongodb.test;

import org.raven.mongodb.MongoRepositoryImpl;
import org.raven.mongodb.test.model.Orders;

public class OrdersRepositoryImpl extends MongoRepositoryImpl<Orders, Long> {

    public OrdersRepositoryImpl() {
        super(MongoSessionInstance.mongoSession);
    }
}
