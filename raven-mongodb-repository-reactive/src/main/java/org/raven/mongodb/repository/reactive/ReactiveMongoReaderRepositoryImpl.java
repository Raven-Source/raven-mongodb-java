package org.raven.mongodb.repository.reactive;

import com.mongodb.ReadPreference;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.*;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 只读数据仓储
 *
 * @param <TEntity>
 * @param <TKey>
 * @author yi.liang
 * @since JDK11
 */
public class ReactiveMongoReaderRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends AbstractReactiveMongoBaseRepository<TEntity, TKey>
        implements ReactiveMongoReaderRepository<TEntity, TKey> {

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession        mongoSession
     * @param collectionName      collectionName
     * @param idGeneratorProvider idGeneratorProvider
     */
    public ReactiveMongoReaderRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(mongoSession, collectionName, sequence, idGeneratorProvider);
    }

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public ReactiveMongoReaderRepositoryImpl(final ReactiveMongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoOptions mongoOptions
     */
    public ReactiveMongoReaderRepositoryImpl(final MongoOptions mongoOptions, final String collectionName) {
        super(mongoOptions, collectionName);
    }

    //#endregion

    //#region get

    /**
     * 根据id获取实体
     *
     * @param id id
     * @return
     */
    @Override
    public Mono<TEntity> get(final TKey id) {
        return this.get(id, null);
    }

    /**
     * 根据id获取实体
     *
     * @param id
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public Mono<TEntity> get(final TKey id, final List<String> includeFields) {
        return this.get(id, includeFields, null);
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
    public Mono<TEntity> get(final TKey id, final List<String> includeFields
            , final ReadPreference readPreference) {

        Bson filter = Filters.eq(BsonConstant.PRIMARY_KEY_NAME, id);

        Bson projection = null;
        if (includeFields != null) {
            projection = super.includeFields(includeFields);
        }
        FindPublisher<TEntity> result = super.getCollection(readPreference).find(filter, entityInformation.getEntityType());
        result = super.findOptions(result, projection, null, 1, 0, null);

        return Mono.from(result.first());
    }

    /**
     * 根据条件获取实体
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public Mono<TEntity> get(final Bson filter) {
        return this.get(filter, null);
    }

    /**
     * 根据条件获取实体
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public Mono<TEntity> get(final Bson filter, final List<String> includeFields) {
        return this.get(filter, includeFields, null);
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
    public Mono<TEntity> get(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.get(filter, includeFields, sort, null, null);

    }

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
    @Override
    public Mono<TEntity> get(final Bson filter, final List<String> includeFields, final Bson sort, final Bson hint
            , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        Bson projection = null;
        if (includeFields != null) {
            projection = super.includeFields(includeFields);
        }

        FindPublisher<TEntity> result = super.getCollection(readPreference).find(_filter, entityInformation.getEntityType());
        result = super.findOptions(result, projection, sort, 1, 0, hint);

        return Mono.from(result.first());
    }

    /**
     * 根据条件获取实体
     *
     * @param findOptions
     * @return
     */
    @Override
    public Mono<TEntity> get(final FindOptions findOptions) {
        return this.get(findOptions.filter(), findOptions.includeFields(), findOptions.sort(), findOptions.hint(), findOptions.readPreference());
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
    public Flux<TEntity> getList(final Bson filter) {
        return this.getList(filter, null);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param filter        查询条件
     * @param includeFields 查询字段
     * @return
     */
    @Override
    public Flux<TEntity> getList(final Bson filter, final List<String> includeFields) {
        return this.getList(filter, includeFields, null);
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
    public Flux<TEntity> getList(final Bson filter, final List<String> includeFields, final Bson sort) {
        return this.getList(filter, includeFields, sort, 0, 0);
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
    public Flux<TEntity> getList(final Bson filter, final List<String> includeFields, final Bson sort
            , final int limit, final int skip) {
        return this.getList(filter, includeFields, sort, limit, skip, null, null);
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
    public Flux<TEntity> getList(final Bson filter, final List<String> includeFields, final Bson sort
            , final int limit, final int skip
            , final Bson hint
            , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        Bson projection = null;
        if (includeFields != null) {
            projection = super.includeFields(includeFields);
        }

        FindPublisher<TEntity> result = super.getCollection(readPreference).find(_filter, entityInformation.getEntityType());
        result = super.findOptions(result, projection, sort, limit, skip, hint);

        return Flux.from(result);
    }

    /**
     * 根据条件获取获取列表
     *
     * @param findOptions
     * @return
     */
    @Override
    public Flux<TEntity> getList(final FindOptions findOptions) {
        return this.getList(findOptions.filter(),
                findOptions.includeFields(),
                findOptions.sort(),
                findOptions.limit(),
                findOptions.skip(),
                findOptions.hint(),
                findOptions.readPreference()
        );
    }

    //#endregion

    /**
     * 数量
     *
     * @param filter 查询条件
     * @return
     */
    @Override
    public Mono<Long> count(final Bson filter) {
        return this.count(filter, (Bson) null, (ReadPreference) null);
    }

    /**
     * 数量
     *
     * @param filter         查询条件
     * @param hint           hint索引
     * @param readPreference 访问设置
     * @return
     */
    @Override
    public Mono<Long> count(final Bson filter, final Bson hint
            , final ReadPreference readPreference) {

        return this.count(filter, 0, 0, hint, readPreference);
    }

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
    @Override
    public Mono<Long> count(final Bson filter, int limit, int skip, final Bson hint
            , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        return Mono.from(
                super.getCollection(readPreference).countDocuments(_filter,
                        new com.mongodb.client.model.CountOptions()
                                .hint(hint)
                                .limit(limit)
                                .skip(skip)
                )
        );
    }

    /**
     * 数量
     *
     * @param countOptions
     * @return
     */
    @Override
    public Mono<Long> count(final CountOptions countOptions) {
        return count(
                countOptions.filter(),
                countOptions.limit(),
                countOptions.skip(),
                countOptions.hint(),
                countOptions.readPreference()
        );
    }

    /**
     * 是否存在
     *
     * @param filter
     * @return
     */
    @Override
    public Mono<Boolean> exists(final Bson filter) {
        return this.exists(filter, null, null);
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
    public Mono<Boolean> exists(final Bson filter, final Bson hint
            , final ReadPreference readPreference) {

        Bson _filter = filter;
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return Mono.from(
                this.get(_filter, includeFields, null, hint, readPreference)
        ).map(Objects::nonNull);
    }

    /**
     * 是否存在
     *
     * @param existsOptions
     * @return
     */
    @Override
    public Mono<Boolean> exists(final ExistsOptions existsOptions) {
        return this.exists(existsOptions.filter(), existsOptions.hint(), existsOptions.readPreference());
    }

}
