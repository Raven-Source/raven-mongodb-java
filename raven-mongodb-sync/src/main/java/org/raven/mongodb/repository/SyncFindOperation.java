package org.raven.mongodb.repository;

import org.raven.mongodb.repository.operation.FindOperation;

import java.util.List;

public interface SyncFindOperation<TEntity, TKey> extends FindOperation<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> {

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
    <TResult> List<TResult> findList(final FindOptions findOptions, final Class<TResult> resultClass);

}
