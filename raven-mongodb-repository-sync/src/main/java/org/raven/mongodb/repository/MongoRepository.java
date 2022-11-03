package org.raven.mongodb.repository;

import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.operation.ModifyOperation;

import java.util.Map;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface MongoRepository<TEntity extends Entity<TKey>, TKey>
        extends MongoReadonlyRepository<TEntity, TKey>
        , ModifyOperation<TEntity, TKey, TKey, Map<Integer, TKey>, Long, TEntity, Long> {

}
