package org.raven.mongodb.reactive;

import org.raven.mongodb.FindOptions;
import org.raven.mongodb.operation.FindOperation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;


/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface ReactiveMongoReadonlyRepository<TEntity, TKey>
        extends ReactiveMongoBaseRepository<TEntity>,
        FindOperation<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> {

//    /**
//     * 数量
//     *
//     * @param filter 查询条件
//     * @return count
//     */
//    Mono<Long> count(Bson filter);
//
//    /**
//     * 数量
//     *
//     * @param filter         查询条件
//     * @param hint           hint索引
//     * @param readPreference 访问设置
//     * @return count
//     */
//    Mono<Long> count(Bson filter, Bson hint
//            , ReadPreference readPreference);
//
//
//    /**
//     * 数量
//     *
//     * @param filter         查询条件
//     * @param limit          limit
//     * @param skip           skip
//     * @param hint           hint索引
//     * @param readPreference 访问设置
//     * @return count
//     */
//    Mono<Long> count(Bson filter, int limit, int skip, Bson hint
//            , ReadPreference readPreference);
//
//
//    /**
//     * 数量
//     *
//     * @param countOptions CountOptions
//     * @return count
//     */
//    Mono<Long> count(CountOptions countOptions);
//
//    /**
//     * 是否存在
//     *
//     * @param filter conditions
//     * @return exists
//     */
//    Mono<Boolean> exists(Bson filter);
//
//    /**
//     * 是否存在
//     *
//     * @param filter         conditions
//     * @param hint           hint
//     * @param readPreference ReadPreference
//     * @return exists
//     */
//    Mono<Boolean> exists(Bson filter, Bson hint
//            , ReadPreference readPreference);
//
//    /**
//     * 是否存在
//     *
//     * @param existsOptions ExistsOptions
//     * @return exists
//     */
//    Mono<Boolean> exists(ExistsOptions existsOptions);

    /**
     * 根据条件获取实体
     *
     * @param findOptions 查询条件
     * @param resultClass TResult Class
     * @param <TResult> TResult
     * @return TResult
     */
    <TResult> Mono<Optional<TResult>> findOne(final FindOptions findOptions, final Class<TResult> resultClass);

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions FindOptions
     * @param resultClass TResult Class
     * @param <TResult> TResult
     * @return TResult
     */
    <TResult> Mono<List<TResult>> findList(final FindOptions findOptions, final Class<TResult> resultClass);

}
