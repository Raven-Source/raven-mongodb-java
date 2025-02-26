package org.raven.mongodb.reactive;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.reactivestreams.client.ClientSession;
import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.criteria.CountOptions;
import org.raven.mongodb.criteria.ExistsOptions;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.operation.FindExecutor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ReactiveReadOperationImpl<TEntity extends Entity<TKey>, TKey> implements ReactiveReadOperation<TEntity, TKey> {

    private final AbstractAsyncMongoBaseRepository<TEntity, TKey> baseRepository;

    protected final @Nullable ClientSession clientSession;

    public ReactiveReadOperationImpl(AbstractAsyncMongoBaseRepository<TEntity, TKey> baseRepository,
                                     @Nullable ClientSession clientSession) {
        this.baseRepository = baseRepository;
        this.clientSession = clientSession;
    }

    protected ReactiveReadOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
        return new ReactiveReadOperationImpl<>(this.baseRepository, clientSession);
    }

    @Override
    public <TResult> Mono<Optional<TResult>> findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindOne(findOptions, resultClass);
    }

    @Override
    public <TResult> Mono<List<TResult>> findMany(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindMany(findOptions, resultClass);
    }

    //region protected

    protected Mono<Optional<TEntity>> doFindOne(final FindOptions options) {
        return this.doFindOne(options, baseRepository.getEntityInformation().getEntityType());
    }

    protected <TResult> Mono<Optional<TResult>> doFindOne(final FindOptions options, Class<TResult> resultClass) {
        return Mono.from(
                baseRepository.doFind(clientSession, options, resultClass).first()
        ).map(Optional::of).defaultIfEmpty(Optional.empty());
    }

    protected Mono<List<TEntity>> doFindMany(final FindOptions options) {
        return this.doFindMany(options, baseRepository.getEntityInformation().getEntityType());
    }

    protected <TResult> Mono<List<TResult>> doFindMany(final FindOptions options, Class<TResult> resultClass) {
        return Flux.from(
                baseRepository.doFind(clientSession, options, resultClass)
        ).collectList();
    }

    protected Mono<Long> doCount(final CountOptions options) {
        return baseRepository.doCount(clientSession, options);
    }

    protected Mono<Boolean> doExists(@NonNull final ExistsOptions options) {

        Bson _filter = options.filter();
        if (_filter == null) {
            _filter = Filters.empty();
        }

        Bson projection = Projections.include(BsonConstant.PRIMARY_KEY_NAME);

        return Mono.from(
                this.findOne(_filter, projection, null, options.hint(), options.readPreference())
        ).map(Optional::isPresent);
    }

    @Override
    public Bson filterById(TKey id) {
        return baseRepository.filterById(id);
    }

    //endregion

    @Override
    public FindExecutor<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> findExecutor() {
        return findExecutor;
    }

    private final FindExecutor<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> findExecutor =
            new FindExecutor<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>>() {
                @Override
                public Class<TEntity> getEntityType() {
                    return ReactiveReadOperationImpl.this.baseRepository.getEntityInformation().getEntityType();
                }

                @Override
                public Mono<Optional<TEntity>> doFindOne(FindOptions options) {
                    return ReactiveReadOperationImpl.this.doFindOne(options);
                }

                @Override
                public Mono<List<TEntity>> doFindMany(FindOptions options) {
                    return ReactiveReadOperationImpl.this.doFindMany(options);
                }

                @Override
                public Mono<Long> doCount(CountOptions options) {
                    return ReactiveReadOperationImpl.this.doCount(options);
                }

                @Override
                public Mono<Boolean> doExists(ExistsOptions options) {
                    return ReactiveReadOperationImpl.this.doExists(options);
                }
            };

}
