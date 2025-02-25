package org.raven.mongodb;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.criteria.CountOptions;
import org.raven.mongodb.criteria.ExistsOptions;
import org.raven.mongodb.criteria.FindOptions;
import org.raven.mongodb.operation.FindExecutor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SyncReadOperationImpl<TEntity extends Entity<TKey>, TKey> implements SyncReadOperation<TEntity, TKey> {

    private final AbstractMongoBaseRepository<TEntity, TKey> baseRepository;

//    private final DoFindProxy doFindProxy;
//    private final Function2<ClientSession, CountOptions, Long> doCountProxy;
//    private final FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> proxy;

//    protected final EntityInformation<TEntity, TKey> entityInformation;

    protected final @Nullable ClientSession clientSession;

    public SyncReadOperationImpl(AbstractMongoBaseRepository<TEntity, TKey> baseRepository,
                                 @Nullable ClientSession clientSession) {
        this.baseRepository = baseRepository;
        this.clientSession = clientSession;
    }

    protected SyncReadOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
        return new SyncReadOperationImpl<>(this.baseRepository, clientSession);
    }

    @Override
    public <TResult> TResult findOne(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindOne(findOptions, resultClass);
    }

    @Override
    public <TResult> List<TResult> findMany(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindMany(findOptions, resultClass);
    }

    //region protected

    protected TEntity doFindOne(final FindOptions options) {
        return this.doFindOne(options, baseRepository.getEntityInformation().getEntityType());
    }

    protected <TResult> TResult doFindOne(final FindOptions options, Class<TResult> resultClass) {
        return baseRepository.doFind(clientSession, options, resultClass).first();
    }

    protected List<TEntity> doFindMany(final FindOptions options) {
        return this.doFindMany(options, baseRepository.getEntityInformation().getEntityType());
    }

    protected <TResult> List<TResult> doFindMany(final FindOptions options, Class<TResult> resultClass) {
        FindIterable<TResult> result = baseRepository.doFind(clientSession, options, resultClass);

        ArrayList<TResult> list = new ArrayList<>();
        for (TResult entity : result) {
            list.add(entity);
        }

        return list;
    }

    protected long doCount(final CountOptions options) {
        return baseRepository.doCount(clientSession, options);
    }

    protected boolean doExists(@NonNull final ExistsOptions options) {

        Bson _filter = options.filter();
        if (_filter == null) {
            _filter = Filters.empty();
        }

        Bson projection = Projections.include(BsonConstant.PRIMARY_KEY_NAME);

        return this.findOne(_filter, projection, null, options.hint(), options.readPreference()) != null;
    }

    //endregion


    @Override
    public FindExecutor<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findExecutor() {
        return findExecutor;
    }

    private final FindExecutor<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findExecutor =
            new FindExecutor<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean>() {
                @Override
                public Class<TEntity> getEntityType() {
                    return SyncReadOperationImpl.this.baseRepository.getEntityInformation().getEntityType();
                }

                @Override
                public TEntity doFindOne(FindOptions options) {
                    return SyncReadOperationImpl.this.doFindOne(options);
                }

                @Override
                public List<TEntity> doFindMany(FindOptions options) {
                    return SyncReadOperationImpl.this.doFindMany(options);
                }

                @Override
                public Long doCount(CountOptions options) {
                    return SyncReadOperationImpl.this.doCount(options);
                }

                @Override
                public Boolean doExists(ExistsOptions options) {
                    return SyncReadOperationImpl.this.doExists(options);
                }
            };

}
