package org.raven.mongodb;

import org.raven.commons.data.Entity;
import org.raven.mongodb.operation.WriteOperation;

import java.util.Map;

public interface SyncWriteOperation<TEntity extends Entity<TKey>, TKey>
        extends WriteOperation<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> {
}
