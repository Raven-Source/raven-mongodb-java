package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import org.bson.conversions.Bson;
import org.raven.mongodb.repository.operation.FindOperation;

import java.util.List;

/**
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public interface MongoReaderRepository<TEntity, TKey>
        extends MongoBaseRepository<TEntity>, FindOperation<TEntity, TKey, TEntity, List<TEntity>> {

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
