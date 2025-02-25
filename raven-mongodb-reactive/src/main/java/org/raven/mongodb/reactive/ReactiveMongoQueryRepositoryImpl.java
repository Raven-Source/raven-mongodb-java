package org.raven.mongodb.reactive;

import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.raven.commons.data.Entity;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.MongoOptions;
import org.raven.mongodb.operation.FindExecutor;
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
public class ReactiveMongoQueryRepositoryImpl<TEntity extends Entity<TKey>, TKey>
        extends AbstractAsyncMongoBaseRepository<TEntity, TKey>
        implements ReactiveMongoQueryRepository<TEntity, TKey> {

    private final ReactiveReadOperationImpl<TEntity, TKey> operation;

    //#region constructor

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     */
    public ReactiveMongoQueryRepositoryImpl(final ReactiveMongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param collectionName collectionName
     */
    public ReactiveMongoQueryRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    /**
     * constructor
     *
     * @param mongoSession ReactiveMongoSession
     * @param mongoOptions MongoOptions
     */
    public ReactiveMongoQueryRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }


    /**
     * constructor
     *
     * @param mongoSession   ReactiveMongoSession
     * @param mongoOptions   MongoOptions
     * @param collectionName collectionName
     */
    public ReactiveMongoQueryRepositoryImpl(final ReactiveMongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
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
    public ReactiveMongoQueryRepositoryImpl(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(mongoSession, collectionName, sequence, idGeneratorProvider);

        operation = new ReactiveReadOperationImpl<>(this,null);
    }

    //#endregion

    //region ext

    @Override
    public <TResult> Mono<Optional<TResult>> findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findOne(findOptions, resultClass);
    }

    @Override
    public <TResult> Mono<List<TResult>> findMany(FindOptions findOptions, Class<TResult> resultClass) {
        return operation.findMany(findOptions, resultClass);
    }

    //#endregion

    //region protected

    @Override
    public ReactiveReadOperation<TEntity, TKey> findWithClientSession(@Nullable ClientSession clientSession) {
        if (clientSession == null) {
            return operation;
        } else {
            return operation.clone(clientSession);
        }
    }

    //endregion

    @Override
    public FindExecutor<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> findExecutor() {
        return operation.findExecutor();
    }

}
