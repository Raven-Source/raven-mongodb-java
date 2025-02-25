package org.raven.mongodb;

import org.raven.commons.data.Entity;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.operation.ReadOperation;

import java.util.List;

public interface SyncReadOperation<TEntity extends Entity<TKey>, TKey>
        extends ReadOperation<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> {

    /**
     * 根据条件获取实体
     *
     * @param findOptions FindOptions
     * @param resultClass TResult Class
     * @param <TResult>   TResult
     * @return TResult
     */
    <TResult> TResult findOne(final FindOptions findOptions, final Class<TResult> resultClass);

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions FindOptions
     * @param resultClass TResult Class
     * @param <TResult>   TResult
     * @return TResult
     */
    <TResult> List<TResult> findMany(final FindOptions findOptions, final Class<TResult> resultClass);

}
