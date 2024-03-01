package org.raven.mongodb;

import org.raven.commons.data.Entity;
import org.raven.mongodb.operation.ModifyOperation;

import java.util.Map;

public interface SyncModifyOperation<TEntity extends Entity<TKey>, TKey>
        extends ModifyOperation<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> {
}
