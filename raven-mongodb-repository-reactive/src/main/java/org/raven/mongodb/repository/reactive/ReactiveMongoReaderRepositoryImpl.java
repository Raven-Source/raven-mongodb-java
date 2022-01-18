package org.raven.mongodb.repository.reactive;

import com.mongodb.ReadPreference;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.*;
import org.raven.mongodb.repository.annotations.PreFind;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 只读数据仓储
 *
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
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
     * @param mongoSession        ReactiveMongoSession
     * @param collectionName      collectionName
     * @param sequence            Sequence
     * @param idGeneratorProvider IdGeneratorProvider
     */
    public ReactiveMongoReaderRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(mongoSession, collectionName, sequence, idGeneratorProvider);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     */
    public ReactiveMongoReaderRepositoryImpl(final ReactiveMongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param collectionName collectionName
     */
    public ReactiveMongoReaderRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName) {
        super(mongoSession, collectionName);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     * @param mongoOptions MongoOptions
     */
    public ReactiveMongoReaderRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
        super(mongoSession, mongoOptions);
    }


    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public ReactiveMongoReaderRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        super(mongoSession, mongoOptions, collectionName);
    }

    //#endregion

    /**
     * 数量
     *
     * @param filter 查询条件
     * @return count
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
     * @return count
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
     * @return count
     */
    @Override
    public Mono<Long> count(final Bson filter, int limit, int skip, final Bson hint
            , final ReadPreference readPreference) {

        CountOptions options = (CountOptions) new CountOptions()
                .limit(limit)
                .skip(skip)
                .filter(filter)
                .hint(hint)
                .readPreference(readPreference);

        return this.count(options);
    }

    /**
     * 数量
     *
     * @param countOptions CountOptions
     * @return count
     */
    @Override
    public Mono<Long> count(final CountOptions countOptions) {
        return this.doCount(countOptions);
    }

    /**
     * 是否存在
     *
     * @param filter conditions
     * @return exists
     */
    @Override
    public Mono<Boolean> exists(final Bson filter) {
        return this.exists(filter, null, null);
    }

    /**
     * 是否存在
     *
     * @param filter         conditions
     * @param hint           hint
     * @param readPreference {{@link ReadPreference}}
     * @return exists
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
        ).map(Optional::isPresent);
    }

    /**
     * 是否存在
     *
     * @param existsOptions ExistsOptions
     * @return exists
     */
    @Override
    public Mono<Boolean> exists(final ExistsOptions existsOptions) {
        return this.exists(existsOptions.filter(), existsOptions.hint(), existsOptions.readPreference());
    }


    //region protected

    protected Mono<Optional<TEntity>> doFindOne(final FindOptions options) {
        return Mono.from(
                this.doFind(options).first()
        ).map(Optional::of).defaultIfEmpty(Optional.empty());
    }

    protected Mono<List<TEntity>> doFindList(final FindOptions options) {
        return Flux.from(
                doFind(options)
        ).collectList();
    }

    protected FindPublisher<TEntity> doFind(final FindOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        Bson projection = null;
        if (options.includeFields() != null) {
            projection = BsonUtils.includeFields(options.includeFields());
        }

        callGlobalInterceptors(PreFind.class, null, options);

        FindPublisher<TEntity> result = super.getCollection(options.readPreference()).find(options.filter(), entityInformation.getEntityType());
        result = super.findOptions(result, projection, options.sort(), options.limit(), options.skip(), options.hint());

        return result;
    }

    protected Mono<Long> doCount(final CountOptions options) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        callGlobalInterceptors(PreFind.class, null, options);

        return Mono.from(
                super.getCollection(options.readPreference()).countDocuments(options.filter(),
                        new com.mongodb.client.model.CountOptions()
                                .hint(options.hint())
                                .limit(options.limit())
                                .skip(options.skip())
                )
        );
    }

    @Override
    public FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>> findProxy() {
        return proxy;
    }

    private final FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>> proxy =

            new FindProxy<>() {
                @Override
                protected EntityInformation<TEntity, TKey> getEntityInformation() {
                    return ReactiveMongoReaderRepositoryImpl.this.entityInformation;
                }

                @Override
                protected Mono<Optional<TEntity>> doFindOne(FindOptions options) {
                    return ReactiveMongoReaderRepositoryImpl.this.doFindOne(options);
                }

                @Override
                protected Mono<List<TEntity>> doFindList(FindOptions options) {
                    return ReactiveMongoReaderRepositoryImpl.this.doFindList(options);
                }
            };

    //endregion

}
