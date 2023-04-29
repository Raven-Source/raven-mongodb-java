package org.raven.mongodb.repository;

import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.operation.ModifyOperation;

import java.util.Map;

public interface SyncModifyOperation<TEntity extends Entity<TKey>, TKey>
        extends ModifyOperation<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> {
}
