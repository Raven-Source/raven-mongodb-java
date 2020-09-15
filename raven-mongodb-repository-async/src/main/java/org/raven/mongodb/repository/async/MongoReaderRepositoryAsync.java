package org.raven.mongodb.repository.async;

import com.mongodb.ReadPreference;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.raven.mongodb.repository.CountOptions;
import org.raven.mongodb.repository.ExistsOptions;
import org.raven.mongodb.repository.FindOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public interface MongoReaderRepositoryAsync<TEntity, TKey>
        extends MongoBaseRepositoryAsync<TEntity> {


    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    CompletableFuture<TEntity> getAsync(TKey id);

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields 查询字段
     * @return
     */
    CompletableFuture<TEntity> getAsync(TKey id, List<String> includeFields);

//    /**
//     * 根据id获取实体
//     *
//     * @param id
//     * @param includeFields 查询字段
//     * @param sort          排序
//     * @return
//     */
//    CompletableFuture<TEntity> getAsync(TKey id, List<String> includeFields, Bson sort);

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields  查询字段
     * @param readPreference 访问设置
     * @return
     */
    CompletableFuture<TEntity> getAsync(TKey id, List<String> includeFields
        , ReadPreference readPreference);


    /**
     * 根据条件获取实体
     *
     * @param filter 查询条件
     * @return
     */
    CompletableFuture<TEntity> getAsync(Bson filter);

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    CompletableFuture<TEntity> getAsync(Bson filter, List<String> includeFields);

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    CompletableFuture<TEntity> getAsync(Bson filter, List<String> includeFields, Bson sort);

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
    CompletableFuture<TEntity> getAsync(Bson filter, List<String> includeFields, Bson sort, BsonValue hint
        , ReadPreference readPreference);

    /**
     * 根据条件获取实体
     *
     * @param findOptions
     * @return
     */
    CompletableFuture<TEntity> getAsync(FindOptions findOptions);

    //#endregion

    //#region getList

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return
     */
    CompletableFuture<List<TEntity>> getListAsync(Bson filter);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    CompletableFuture<List<TEntity>> getListAsync(Bson filter, List<String> includeFields);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    CompletableFuture<List<TEntity>> getListAsync(Bson filter, List<String> includeFields, Bson sort);

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
    CompletableFuture<List<TEntity>> getListAsync(Bson filter, List<String> includeFields, Bson sort
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
    CompletableFuture<List<TEntity>> getListAsync(Bson filter, List<String> includeFields, Bson sort
        , int limit, int skip
        , BsonValue hint
        , ReadPreference readPreference);


    /**
     * 根据条件获取获取列表
     *
     * @param findOptions
     * @return
     */
    CompletableFuture<List<TEntity>> getListAsync(FindOptions findOptions);

    //#endregion

    /**
     * 数量
     *
     * @param filter 查询条件
     * @return
     */
    CompletableFuture<Long> countAsync(Bson filter);

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    CompletableFuture<Long> countAsync(Bson filter, int skip, BsonValue hint
        , ReadPreference readPreference);


    /**
     * 数量
     *
     * @param countOptions
     * @return
     */
    CompletableFuture<Long> countAsync(CountOptions countOptions);

    /**
     * 是否存在
     *
     * @param filter
     * @return
     */
    CompletableFuture<Boolean> existsAsync(Bson filter);

    /**
     * 是否存在
     *
     * @param filter
     * @param hint
     * @param readPreference
     * @return
     */
    CompletableFuture<Boolean> existsAsync(Bson filter, BsonValue hint
        , ReadPreference readPreference);

    /**
     * 是否存在
     *
     * @param existsOptions
     * @return
     */
    CompletableFuture<Boolean> existsAsync(ExistsOptions existsOptions);
}
