package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public interface MongoReaderRepository<TEntity, TKey>
        extends MongoBaseRepository<TEntity> {

    /**
     * 根据id获取实体
     *
     * @param id TKey
     * @return
     */
    TEntity get(TKey id);

    /**
     * 根据id获取实体
     *
     * @param id            TKey
     * @param includeFields 查询字段
     * @return
     */
    TEntity get(TKey id, List<String> includeFields);

    /**
     * 根据id获取实体
     *
     * @param id             TKey
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
    TEntity get(Bson filter, List<String> includeFields, Bson sort, Bson hint
            , ReadPreference readPreference);

    /**
     * 根据条件获取实体
     *
     * @param findOptions FindOptions
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
    List<TEntity> getList(Bson filter);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    List<TEntity> getList(Bson filter, List<String> includeFields);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    List<TEntity> getList(Bson filter, List<String> includeFields, Bson sort);

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @param limit         limit
     * @param skip          skip
     * @return
     */
    List<TEntity> getList(Bson filter, List<String> includeFields, Bson sort
            , int limit, int skip);

    /**
     * 根据条件获取获取列表
     *
     * @param filter         查询条件
     * @param includeFields  查询字段
     * @param sort           排序
     * @param limit          limit
     * @param skip           skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    List<TEntity> getList(Bson filter, List<String> includeFields, Bson sort
            , int limit, int skip
            , Bson hint
            , ReadPreference readPreference);


    /**
     * 根据条件获取获取列表
     *
     * @param findOptions FindOptions
     * @return List
     */
    List<TEntity> getList(FindOptions findOptions);

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
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    long count(Bson filter, Bson hint
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
    long count(Bson filter, int limit, int skip, Bson hint
            , ReadPreference readPreference);


    /**
     * 数量
     *
     * @param countOptions CountOptions
     * @return
     */
    long count(CountOptions countOptions);

    /**
     * 是否存在
     *
     * @param filter filter Bson
     * @return
     */
    boolean exists(Bson filter);

    /**
     * 是否存在
     *
     * @param filter         filter Bson
     * @param hint           hint Bson
     * @param readPreference ReadPreference
     * @return
     */
    boolean exists(Bson filter, Bson hint
            , ReadPreference readPreference);

    /**
     * 是否存在
     *
     * @param existsOptions ExistsOptions
     * @return
     */
    boolean exists(ExistsOptions existsOptions);
}
