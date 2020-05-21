package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK1.8
 */
public interface MongoReaderRepository<TEntity, TKey>
        extends MongoBaseRepository<TEntity> {

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    TEntity get(TKey id);

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields 查询字段
     * @return
     */
    TEntity get(TKey id, List<String> includeFields);

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields  查询字段
     * @param readPreference 访问设置
     * @return
     */
    TEntity get(TKey id, List<String> includeFields
        , ReadPreference readPreference);


    /**
     * 根据条件获取实体
     *
     * @param filter 查询条件
     * @return
     */
    TEntity get(Bson filter);

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    TEntity get(Bson filter, List<String> includeFields);

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    TEntity get(Bson filter, List<String> includeFields, Bson sort);

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
    TEntity get(Bson filter, List<String> includeFields, Bson sort, BsonValue hint
        , ReadPreference readPreference);

    /**
     * 根据条件获取实体
     *
     * @param findOptions
     * @return
     */
    TEntity get(FindOptions findOptions);

    //#endregion

    //#region getList

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return
     */
    ArrayList<TEntity> getList(Bson filter);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    ArrayList<TEntity> getList(Bson filter, List<String> includeFields);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    ArrayList<TEntity> getList(Bson filter, List<String> includeFields, Bson sort);

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
    ArrayList<TEntity> getList(Bson filter, List<String> includeFields, Bson sort
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
    ArrayList<TEntity> getList(Bson filter, List<String> includeFields, Bson sort
        , int limit, int skip
        , BsonValue hint
        , ReadPreference readPreference);


    /**
     * 根据条件获取获取列表
     *
     * @param findOptions
     * @return
     */
    ArrayList<TEntity> getList(FindOptions findOptions);

    //#endregion

    /**
     * 数量
     *
     * @param filter 查询条件
     * @return
     */
    long count(Bson filter);

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    long count(Bson filter, int skip, BsonValue hint
        , ReadPreference readPreference);


    /**
     * 数量
     *
     * @param countOptions
     * @return
     */
    long count(CountOptions countOptions);

    /**
     * 是否存在
     *
     * @param filter
     * @return
     */
    boolean exists(Bson filter);

    /**
     * 是否存在
     *
     * @param filter
     * @param hint
     * @param readPreference
     * @return
     */
    boolean exists(Bson filter, BsonValue hint
        , ReadPreference readPreference);

    /**
     * 是否存在
     *
     * @param existsOptions
     * @return
     */
    boolean exists(ExistsOptions existsOptions);
}
