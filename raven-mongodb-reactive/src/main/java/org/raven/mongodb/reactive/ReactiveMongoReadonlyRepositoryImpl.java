package org.raven.mongodb.reactive;

import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.raven.commons.data.Entity;
import org.raven.mongodb.FindOptions;
import org.raven.mongodb.MongoOptions;
import org.raven.mongodb.spi.ReactiveIdGenerator;
import org.raven.mongodb.spi.IdGeneratorProvider;
import org.raven.mongodb.spi.Sequence;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
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

    private final ReactiveFindOperationImpl<TEntity, TKey> operation;

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param collectionName collectionName
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     * @param mongoOptions MongoOptions
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }


    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public ReactiveMongoReadonlyRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

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

        operation = new ReactiveFindOperationImpl<>(this,null);
    }

    //#endregion

    //region ext

    @Override
    public <TResult> Mono<Optional<TResult>> findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findOne(findOptions, resultClass);
    }

    @Override
    public <TResult> Mono<List<TResult>> findList(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findList(findOptions, resultClass);
    }

    //#endregion

    //region protected

    public ReactiveFindOperation<TEntity, TKey> findWithClientSession(@Nullable ClientSession clientSession) {
        if (clientSession == null) {
            return operation;
        } else {
            return operation.clone(clientSession);
        }
    }

//    protected Mono<Optional<TEntity>> doFindOne(final FindOptions options) {
//        return this.doFindOne(options, entityInformation.getEntityType());
//    }
//
//    protected <TResult> Mono<Optional<TResult>> doFindOne(final FindOptions options, Class<TResult> resultClass) {
//        return Mono.from(
//                this.doFind(options, resultClass).first()
//        ).map(Optional::of).defaultIfEmpty(Optional.empty());
//    }
//
//    protected Mono<List<TEntity>> doFindList(final FindOptions options) {
//        return this.doFindList(options, entityInformation.getEntityType());
//    }
//
//    protected <TResult> Mono<List<TResult>> doFindList(final FindOptions options, Class<TResult> resultClass) {
//        return Flux.from(
//                doFind(options, resultClass)
//        ).collectList();
//    }

//    protected Mono<Long> doCount(final CountOptions options) {
//
//        if (options.filter() == null) {
//            options.filter(Filters.empty());
//        }
//
//        callGlobalInterceptors(PreFind.class, null, options);
//
//        return Mono.from(
//                super.getCollection(options.readPreference()).countDocuments(options.filter(),
//                        new com.mongodb.client.model.CountOptions()
//                                .hint(options.hint())
//                                .limit(options.limit())
//                                .skip(options.skip())
//                )
//        );
//    }
//
//    protected Mono<Boolean> doExists(final ExistsOptions options) {
//
//        Bson _filter = options.filter();
//        if (_filter == null) {
//            _filter = new BsonDocument();
//        }
//
//        List<String> includeFields = new ArrayList<>(1);
//        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);
//
//        return Mono.from(
//                this.findOne(_filter, includeFields, null, options.hint(), options.readPreference())
//        ).map(Optional::isPresent);
//    }

    @Override
    public FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> findProxy() {
        return operation.proxy();
    }

//    private final FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> proxy =
//
//            new FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>>() {
//                @Override
//                public EntityInformation<TEntity, TKey> getEntityInformation() {
//                    return ReactiveMongoReadonlyRepositoryImpl.this.entityInformation;
//                }
//
//                @Override
//                public Mono<Optional<TEntity>> doFindOne(FindOptions options) {
//                    return ReactiveMongoReadonlyRepositoryImpl.this.doFindOne(options);
//                }
//
//                @Override
//                public Mono<List<TEntity>> doFindList(FindOptions options) {
//                    return ReactiveMongoReadonlyRepositoryImpl.this.doFindList(options);
//                }
//
//                @Override
//                public Mono<Long> doCount(CountOptions options) {
//                    return ReactiveMongoReadonlyRepositoryImpl.this.doCount(options);
//                }
//
//                @Override
//                public Mono<Boolean> doExists(ExistsOptions options) {
//                    return ReactiveMongoReadonlyRepositoryImpl.this.doExists(options);
//                }
//            };

    //endregion

}
