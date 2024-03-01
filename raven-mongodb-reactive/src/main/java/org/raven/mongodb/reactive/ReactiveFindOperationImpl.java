package org.raven.mongodb.reactive;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.ClientSession;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.CountOptions;
import org.raven.mongodb.EntityInformation;
import org.raven.mongodb.ExistsOptions;
import org.raven.mongodb.FindOptions;
import org.raven.mongodb.contants.BsonConstant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReactiveFindOperationImpl<TEntity extends Entity<TKey>, TKey> implements ReactiveFindOperation<TEntity, TKey> {

    private final AbstractAsyncMongoBaseRepository<TEntity, TKey> baseRepository;

    protected final @Nullable ClientSession clientSession;

    public ReactiveFindOperationImpl(AbstractAsyncMongoBaseRepository<TEntity, TKey> baseRepository,
                                     @Nullable ClientSession clientSession) {
        this.baseRepository = baseRepository;
        this.clientSession = clientSession;
    }

    protected ReactiveFindOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
        return new ReactiveFindOperationImpl<>(this.baseRepository, clientSession);
    }

    @Override
    public FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> findProxy() {
        return proxy;
    }

    @Override
    public <TResult> Mono<Optional<TResult>> findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindOne(findOptions, resultClass);
    }

    @Override
    public <TResult> Mono<List<TResult>> findList(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindList(findOptions, resultClass);
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

    protected Mono<List<TEntity>> doFindList(final FindOptions options) {
        return this.doFindList(options, baseRepository.getEntityInformation().getEntityType());
    }

    protected <TResult> Mono<List<TResult>> doFindList(final FindOptions options, Class<TResult> resultClass) {
        return Flux.from(
                baseRepository.doFind(clientSession, options, resultClass)
        ).collectList();
    }

    protected Mono<Long> doCount(final CountOptions options) {
        return baseRepository.doCount(clientSession, options);
    }

    protected Mono<Boolean> doExists(final ExistsOptions options) {

        Bson _filter = options.filter();
        if (_filter == null) {
            _filter = Filters.empty();
        }

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return Mono.from(
                this.findOne(_filter, includeFields, null, options.hint(), options.readPreference())
        ).map(Optional::isPresent);
    }

    //endregion

    public FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> proxy() {
        return proxy;
    }

    private FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>> proxy =
            new FindProxy<TEntity, TKey, Mono<Optional<TEntity>>, Mono<List<TEntity>>, Mono<Long>, Mono<Boolean>>() {
                @Override
                public EntityInformation<TEntity, TKey> getEntityInformation() {
                    return ReactiveFindOperationImpl.this.baseRepository.getEntityInformation();
                }

                @Override
                public Mono<Optional<TEntity>> doFindOne(FindOptions options) {
                    return ReactiveFindOperationImpl.this.doFindOne(options);
                }

                @Override
                public Mono<List<TEntity>> doFindList(FindOptions options) {
                    return ReactiveFindOperationImpl.this.doFindList(options);
                }

                @Override
                public Mono<Long> doCount(CountOptions options) {
                    return ReactiveFindOperationImpl.this.doCount(options);
                }

                @Override
                public Mono<Boolean> doExists(ExistsOptions options) {
                    return ReactiveFindOperationImpl.this.doExists(options);
                }
            };

}
