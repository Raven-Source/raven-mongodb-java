package org.raven.mongodb.repository.async;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.async.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.*;
import org.raven.mongodb.repository.contants.BsonConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 异步只读数据仓储
 *
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK1.8
 */
public class MongoReaderRepositoryAsyncImpl<TEntity extends Entity<TKey>, TKey>
    extends AbstractMongoBaseRepositoryAsync<TEntity, TKey>
    implements MongoReaderRepositoryAsync<TEntity, TKey> {

    //#region 构造函数

    /**
     * 构造函数
     *
     * @param uri            数据库连接uri
     * @param dbName         数据库名称
     * @param collectionName 集合名称
     * @param writeConcern   WriteConcern
     * @param readPreference ReadPreference
     * @param sequence       Mongo自增长ID数据序列对象
     */
    public MongoReaderRepositoryAsyncImpl(final String uri, final String dbName, final String collectionName, final WriteConcern writeConcern, final ReadPreference readPreference, final MongoSequence sequence) {
        super(uri, dbName, collectionName, writeConcern, readPreference, sequence);
    }

    /**
     * 构造函数
     *
     * @param uri    数据库连接uri
     * @param dbName 数据库名称
     */
    public MongoReaderRepositoryAsyncImpl(final String uri, final String dbName) {
        super(uri, dbName);
    }

    /**
     * 构造函数
     *
     * @param options
     * @see MongoRepositoryOptions
     */
    public MongoReaderRepositoryAsyncImpl(final MongoRepositoryOptions options) {
        super(options);
    }

    //#endregion

    //#region get

    /**
     * 根据id获取实体
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final TKey id) {
        return this.getAsync(id, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final TKey id, final List<String> includeFields) {
        return this.getAsync(id, includeFields, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields  查询字段
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final TKey id, final List<String> includeFields
        , final ReadPreference readPreference) {

        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);

        Bson projection = null;
        if (includeFields != null) {
            projection = super.includeFields(includeFields);
        }

        FindIterable<TEntity> findIterable = super.getCollection(readPreference).find(filter, entityClazz);
        findIterable = super.findOptions(findIterable, projection, null, 1, 0, null);

        CompletableFuture<TEntity> future = new CompletableFuture<>();
        findIterable.first((entity, throwable) -> {
            if (throwable == null) {
                future.complete(entity);
            } else {
                future.completeExceptionally(throwable);
            }
        });
        return future;

    }

    /**
     * 根据条件获取实体
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final Bson filter) {
        return this.getAsync(filter, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final Bson filter, final List<String> includeFields) {
        return this.getAsync(filter, includeFields, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.getAsync(filter, includeFields, sort, null, null);

    }

    /**
     * @param filter
     * @param includeFields
     * @param sort
     * @param hint
     * @param readPreference
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final Bson filter, final List<String> includeFields, final Bson sort, final BsonValue hint
        , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        Bson projection = null;
        if (includeFields != null) {
            projection = super.includeFields(includeFields);
        }

        FindIterable<TEntity> result = super.getCollection(readPreference).find(_filter, entityClazz);
        result = super.findOptions(result, projection, sort, 1, 0, hint);

        CompletableFuture<TEntity> future = new CompletableFuture<>();
        result.first((entity, throwable) -> {

            if (throwable == null) {
                future.complete(entity);
            } else {
                future.completeExceptionally(throwable);
            }
        });
        return future;
    }

    /**
     * 根据条件获取实体
     *
     * @param findOptions
     * @return
     */
    @Override
    public CompletableFuture<TEntity> getAsync(final FindOptions findOptions) {
        return this.getAsync(findOptions.getFilter(), findOptions.getIncludeFields(), findOptions.getSort(), findOptions.getHint(), findOptions.getReadPreference());
    }

    //#endregion

    //#region getList

    /**
     * 根据条件获取获取列表
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public CompletableFuture<ArrayList<TEntity>> getListAsync(final Bson filter) {
        return this.getListAsync(filter, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public CompletableFuture<ArrayList<TEntity>> getListAsync(final Bson filter, final List<String> includeFields) {
        return this.getListAsync(filter, includeFields, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @param sort          排序
     * @return
     */
    @Override
    public CompletableFuture<ArrayList<TEntity>> getListAsync(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.getListAsync(filter, includeFields, sort, 0, 0);
    }

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
    @Override
    public CompletableFuture<ArrayList<TEntity>> getListAsync(final Bson filter, final List<String> includeFields, final Bson sort
        , int limit, int skip) {
        return this.getListAsync(filter, includeFields, sort, limit, skip, null, null);
    }

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
    @Override
    public CompletableFuture<ArrayList<TEntity>> getListAsync(final Bson filter, final List<String> includeFields, final Bson sort
        , int limit, int skip
        , BsonValue hint
        , ReadPreference readPreference) {
        CompletableFuture<ArrayList<TEntity>> future = new CompletableFuture<>();
        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        Bson projection = null;
        if (includeFields != null) {
            projection = super.includeFields(includeFields);
        }

        FindIterable<TEntity> findIterable = super.getCollection(readPreference).find(_filter, entityClazz);
        findIterable = super.findOptions(findIterable, projection, sort, limit, skip, hint);

        ArrayList<TEntity> list = new ArrayList<>();
        findIterable.forEach(entity -> {
            list.add(entity);
        }, (_nil, throwable) -> {
            if (throwable == null) {
                future.complete(list);
            } else {
                future.completeExceptionally(throwable);
            }
        });

//        for (TEntity entity : result) {
//            list.add(entity);
//        }

        return future;
    }

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions
     * @return
     */
    @Override
    public CompletableFuture<ArrayList<TEntity>> getListAsync(FindOptions findOptions) {
        return this.getListAsync(findOptions.getFilter(), findOptions.getIncludeFields(), findOptions.getSort(), findOptions.getLimit(), findOptions.getSkip(), findOptions.getHint(), findOptions.getReadPreference());
    }

    //#endregion

    /**
     * 数量
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public CompletableFuture<Long> countAsync(final Bson filter) {
        return this.countAsync(filter, 0, null, null);
    }

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param skip
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public CompletableFuture<Long> countAsync(final Bson filter, final int skip, final BsonValue hint
        , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        CountOptions option = new CountOptions();
        option.skip(skip);

        CompletableFuture<Long> future = new CompletableFuture<>();

        super.getCollection(readPreference).count(_filter, option, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }
        });

        return future;
    }

    /**
     * 数量
     *
     * @param countOptions
     * @return
     */
    @Override
    public CompletableFuture<Long> countAsync(final CountOptions countOptions) {

        Bson _filter = countOptions.getFilter();
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        CompletableFuture<Long> future = new CompletableFuture<>();

        super.getCollection(countOptions.getReadPreference()).count(_filter, countOptions, (result, throwable) -> {

            if (throwable == null) {
                future.complete(result);
            } else {
                future.completeExceptionally(throwable);
            }
        });
        //return super.getCollection(countOptions.getReadPreference()).countAsync(_filter, countOptions);

        return future;
    }

    /**
     * 是否存在
     *
     * @param filter
     * @return
     */
    @Override
    public CompletableFuture<Boolean> existsAsync(final Bson filter) {
        return this.existsAsync(filter, null, null);
    }

    /**
     * 是否存在
     *
     * @param filter
     * @param hint
     * @param readPreference
     * @return
     */
    @Override
    public CompletableFuture<Boolean> existsAsync(final Bson filter, final BsonValue hint
        , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return this.getAsync(_filter, includeFields, null, hint, readPreference).thenApply(entity -> entity != null);
    }

    /**
     * 是否存在
     *
     * @param existsOptions
     * @return
     */
    @Override
    public CompletableFuture<Boolean> existsAsync(final ExistsOptions existsOptions) {
        return this.existsAsync(existsOptions.getFilter(), existsOptions.getHint(), existsOptions.getReadPreference());
    }
}
