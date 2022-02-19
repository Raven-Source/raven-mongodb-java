package org.raven.mongodb.repository.reactive;

import com.mongodb.client.model.Filters;
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
 */
public class ReactiveMongoReadonlyRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends AbstractAsyncMongoBaseRepository<TEntity, TKey>
        implements ReactiveMongoReadonlyRepository<TEntity, TKey> {

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession        ReactiveMongoSession
     * @param collectionName      collectionName
     * @param sequence            Sequence
     * @param idGeneratorProvider IdGeneratorProvider
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(mongoSession, collectionName, sequence, idGeneratorProvider);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession) {
        super(mongoSession);
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param collectionName collectionName
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName) {
        super(mongoSession, collectionName);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     * @param mongoOptions MongoOptions
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
        super(mongoSession, mongoOptions);
    }


    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        super(mongoSession, mongoOptions, collectionName);
    }

    //#endregion

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

    protected Mono<Boolean> doExists(final ExistsOptions options) {

        Bson _filter = options.filter();
        if (_filter == null) {
            _filter = new BsonDocument();
        }

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return Mono.from(
                this.get(_filter, includeFields, null, options.hint(), options.readPreference())
        ).map(Optional::isPresent);
    }

    @Override
    public FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> findProxy() {
        return proxy;
    }

    private final FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> proxy =

            new FindProxy<>() {
                @Override
                protected EntityInformation<TEntity, TKey> getEntityInformation() {
                    return ReactiveMongoReadonlyRepositoryImpl.this.entityInformation;
                }

                @Override
                protected Mono<Optional<TEntity>> doFindOne(FindOptions options) {
                    return ReactiveMongoReadonlyRepositoryImpl.this.doFindOne(options);
                }

                @Override
                protected Mono<List<TEntity>> doFindList(FindOptions options) {
                    return ReactiveMongoReadonlyRepositoryImpl.this.doFindList(options);
                }

                @Override
                protected Mono<Long> doCount(CountOptions options) {
                    return ReactiveMongoReadonlyRepositoryImpl.this.doCount(options);
                }

                @Override
                protected Mono<Boolean> doExists(ExistsOptions options) {
                    return ReactiveMongoReadonlyRepositoryImpl.this.doExists(options);
                }
            };

    //endregion

}
