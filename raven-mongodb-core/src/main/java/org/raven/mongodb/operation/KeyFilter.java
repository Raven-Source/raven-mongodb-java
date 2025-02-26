package org.raven.mongodb.operation;

import org.bson.conversions.Bson;

public interface KeyFilter<TKey> {
    Bson filterById(TKey id);
}
