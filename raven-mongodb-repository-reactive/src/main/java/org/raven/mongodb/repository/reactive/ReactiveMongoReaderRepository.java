package org.raven.mongodb.repository.reactive;

import com.mongodb.Function;
import com.mongodb.ReadPreference;
import org.bson.conversions.Bson;
import org.raven.mongodb.repository.CountOptions;
import org.raven.mongodb.repository.ExistsOptions;
import org.raven.mongodb.repository.FindOptions;
import org.raven.mongodb.repository.query.FieldNest;
import org.raven.mongodb.repository.query.FilterBuilder;
import org.raven.mongodb.repository.query.HintBuilder;
import org.raven.mongodb.repository.query.SortBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 */
public interface ReactiveMongoReaderRepository<TEntity, TKey>
        extends ReactiveMongoBaseRepository<TEntity> {

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    Mono<TEntity> get(TKey id);

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields 查询字段
     * @return
     */
    Mono<TEntity> get(TKey id, List<String> includeFields);

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields  查询字段
     * @param readPreference 访问设置
     * @return
     */
    Mono<TEntity> get(TKey id, List<String> includeFields
            , ReadPreference readPreference);


    /**
     * 根据条件获取实体
     *
     * @param filter 查询条件
     * @return
     */
    Mono<TEntity> get(Bson filter);

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    Mono<TEntity> get(Bson filter, List<String> includeFields);

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    Mono<TEntity> get(Bson filter, List<String> includeFields, Bson sort);

    /**
     * 根据条件获取实体
     *
     * @param filter         查询条件
     * @param includeFields  查询字段
     * @param sort           排序
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    Mono<TEntity> get(Bson filter, List<String> includeFields, Bson sort, Bson hint
            , ReadPreference readPreference);


    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Mono<TEntity> get(Function<FilterBuilder<TEntity>, Bson> filterBuilder);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Mono<TEntity> get(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                Function<FieldNest, List<String>> fieldNestList);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Mono<TEntity> get(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                Function<FieldNest, List<String>> fieldNestList,
                Function<SortBuilder<TEntity>, Bson> sortBuilder);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Mono<TEntity> get(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                Function<FieldNest, List<String>> fieldNestList,
                Function<SortBuilder<TEntity>, Bson> sortBuilder,
                Function<HintBuilder<TEntity>, Bson> hintBuilder);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Mono<TEntity> get(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                Function<FieldNest, List<String>> fieldNestList,
                Function<SortBuilder<TEntity>, Bson> sortBuilder,
                Function<HintBuilder<TEntity>, Bson> hintBuilder,
                ReadPreference readPreference);

    /**
     * 根据条件获取实体
     *
     * @param findOptions
     * @return
     */
    Mono<TEntity> get(FindOptions findOptions);

    //#endregion

    //#region getList

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return
     */
    Flux<TEntity> getList(Bson filter);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    Flux<TEntity> getList(Bson filter, List<String> includeFields);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    Flux<TEntity> getList(Bson filter, List<String> includeFields, Bson sort);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @param limit
     * @param skip
     * @return
     */
    Flux<TEntity> getList(Bson filter, List<String> includeFields, Bson sort
            , int limit, int skip);

    /**
     * 根据条件获取获取列表
     *
     * @param filter         查询条件
     * @param includeFields  查询字段
     * @param sort           排序
     * @param limit
     * @param skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    Flux<TEntity> getList(Bson filter, List<String> includeFields, Bson sort
            , int limit, int skip
            , Bson hint
            , ReadPreference readPreference);


    /**
     * 根据条件获取获取列表
     *
     * @return
     */
    Flux<TEntity> getList(Function<FilterBuilder<TEntity>, Bson> filterBuilder);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Flux<TEntity> getList(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                          Function<FieldNest, List<String>> fieldNestList);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Flux<TEntity> getList(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                          Function<FieldNest, List<String>> fieldNestList,
                          Function<SortBuilder<TEntity>, Bson> sortBuilder);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Flux<TEntity> getList(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                          Function<FieldNest, List<String>> fieldNestList,
                          Function<SortBuilder<TEntity>, Bson> sortBuilder,
                          int limit, int skip);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Flux<TEntity> getList(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                          Function<FieldNest, List<String>> fieldNestList,
                          Function<SortBuilder<TEntity>, Bson> sortBuilder,
                          int limit, int skip,
                          Function<HintBuilder<TEntity>, Bson> hintBuilder);

    /**
     * 根据条件获取实体
     *
     * @param filterBuilder 查询条件
     * @return
     */
    Flux<TEntity> getList(Function<FilterBuilder<TEntity>, Bson> filterBuilder,
                          Function<FieldNest, List<String>> fieldNestList,
                          Function<SortBuilder<TEntity>, Bson> sortBuilder,
                          int limit, int skip,
                          Function<HintBuilder<TEntity>, Bson> hintBuilder,
                          ReadPreference readPreference);


    /**
     * 根据条件获取获取列表
     *
     * @param findOptions
     * @return
     */
    Flux<TEntity> getList(FindOptions findOptions);

    //#endregion

    /**
     * 数量
     *
     * @param filter 查询条件
     * @return
     */
    Mono<Long> count(Bson filter);

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    Mono<Long> count(Bson filter, Bson hint
            , ReadPreference readPreference);


    /**
     * 数量
     *
     * @param filter         查询条件
     * @param limit          limit
     * @param skip           skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    Mono<Long> count(Bson filter, int limit, int skip, Bson hint
            , ReadPreference readPreference);


    /**
     * 数量
     *
     * @param countOptions
     * @return
     */
    Mono<Long> count(CountOptions countOptions);

    /**
     * 是否存在
     *
     * @param filter
     * @return
     */
    Mono<Boolean> exists(Bson filter);

    /**
     * 是否存在
     *
     * @param filter
     * @param hint
     * @param readPreference
     * @return
     */
    Mono<Boolean> exists(Bson filter, Bson hint
            , ReadPreference readPreference);

    /**
     * 是否存在
     *
     * @param existsOptions
     * @return
     */
    Mono<Boolean> exists(ExistsOptions existsOptions);
}
