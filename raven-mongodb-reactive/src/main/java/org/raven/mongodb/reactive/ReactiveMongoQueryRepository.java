package org.raven.mongodb.reactive;

import org.raven.commons.data.Entity;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.operation.ReadOperation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;


/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface ReactiveMongoQueryRepository<TEntity extends Entity<TKey>, TKey>
        extends ReactiveMongoBaseRepository<TEntity>,
        ReadOperation<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> {

    /**
     * 根据条件获取实体
     *
     * @param findOptions 查询条件
     * @param resultClass TResult Class
     * @param <TResult>   TResult
     * @return TResult
     */
    <TResult> Mono<Optional<TResult>> findOne(final FindOptions findOptions, final Class<TResult> resultClass);

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions FindOptions
     * @param resultClass TResult Class
     * @param <TResult>   TResult
     * @return TResult
     */
    <TResult> Mono<List<TResult>> findList(final FindOptions findOptions, final Class<TResult> resultClass);

}
