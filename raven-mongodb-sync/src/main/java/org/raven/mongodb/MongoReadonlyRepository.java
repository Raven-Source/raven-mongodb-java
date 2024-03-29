package org.raven.mongodb;

import com.mongodb.client.ClientSession;

/**
 * MongoReaderRepository
 *
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public interface MongoReadonlyRepository<TEntity, TKey>
        extends MongoBaseRepository<TEntity>,
        SyncFindOperation<TEntity, TKey> {

    SyncFindOperation<TEntity, TKey> findWithClientSession(ClientSession clientSession);

//    /**
//     * 数量
//     *
//     * @param filter 查询条件
//     * @return count
//     */
//    long count(Bson filter);
//
//    /**
//     * 数量
//     *
//     * @param filter         查询条件
//     * @param hint           hint索引
//     * @param readPreference 访问设置
//     * @return count
//     */
//    long count(Bson filter, Bson hint
//            , ReadPreference readPreference);
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
//    long count(Bson filter, int limit, int skip, Bson hint
//            , ReadPreference readPreference);
//
//
//    /**
//     * 数量
//     *
//     * @param countOptions CountOptions
//     * @return count
//     */
//    long count(CountOptions countOptions);
//
//    /**
//     * 是否存在
//     *
//     * @param filter filter Bson
//     * @return exists
//     */
//    boolean exists(Bson filter);
//
//    /**
//     * 是否存在
//     *
//     * @param filter         filter Bson
//     * @param hint           hint Bson
//     * @param readPreference ReadPreference
//     * @return exists
//     */
//    boolean exists(Bson filter, Bson hint
//            , ReadPreference readPreference);
//
//    /**
//     * 是否存在
//     *
//     * @param existsOptions ExistsOptions
//     * @return exists
//     */
//    boolean exists(ExistsOptions existsOptions);

//    /**
//     * 根据条件获取实体
//     *
//     * @param findOptions FindOptions
//     * @param resultClass TResult Class
//     * @param <TResult>   TResult
//     * @return TResult
//     */
//    <TResult> TResult findOne(final FindOptions findOptions, final Class<TResult> resultClass);
//
//    /**
//     * 根据条件获取获取列表
//     *
//     * @param findOptions FindOptions
//     * @param resultClass TResult Class
//     * @param <TResult>   TResult
//     * @return TResult
//     */
//    <TResult> List<TResult> findList(final FindOptions findOptions, final Class<TResult> resultClass);

}
