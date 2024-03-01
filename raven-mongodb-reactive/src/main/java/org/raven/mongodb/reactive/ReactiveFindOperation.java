package org.raven.mongodb.reactive;

import org.raven.mongodb.FindOptions;
import org.raven.mongodb.operation.FindOperation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ReactiveFindOperation<TEntity, TKey>
        extends FindOperation<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> {

    /**
     * 根据条件获取实体
     *
     * @param findOptions FindOptions
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
