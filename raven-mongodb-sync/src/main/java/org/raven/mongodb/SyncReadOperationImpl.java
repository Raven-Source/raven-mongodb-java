package org.raven.mongodb;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import lombok.NonNull;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.contants.BsonConstant;
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
    public <TResult> List<TResult> findList(FindOptions findOptions, Class<TResult> resultClass) {
        return doFindList(findOptions, resultClass);
    }

    //region protected

    protected TEntity doFindOne(final FindOptions options) {
        return this.doFindOne(options, baseRepository.entityInformation.getEntityType());
    }

    protected <TResult> TResult doFindOne(final FindOptions options, Class<TResult> resultClass) {
        return baseRepository.doFind(clientSession, options, resultClass).first();
    }

    protected List<TEntity> doFindList(final FindOptions options) {
        return this.doFindList(options, baseRepository.entityInformation.getEntityType());
    }

    protected <TResult> List<TResult> doFindList(final FindOptions options, Class<TResult> resultClass) {
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

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return this.findOne(_filter, includeFields, null, options.hint(), options.readPreference()) != null;
    }

    //endregion

//    public interface DoFindProxy {
//
//        <TResult> FindIterable<TResult> doFind(@Nullable final ClientSession session, final FindOptions options, final Class<TResult> resultClass);
//    }

    @Override
    public FindExecutor<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findExecutor() {
        return findExecutor;
    }

    private final FindExecutor<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findExecutor =
            new FindExecutor<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean>() {
                @Override
                public EntityInformation<TEntity, TKey> getEntityInformation() {
                    return SyncReadOperationImpl.this.baseRepository.getEntityInformation();
                }

                @Override
                public TEntity doFindOne(FindOptions options) {
                    return SyncReadOperationImpl.this.doFindOne(options);
                }

                @Override
                public List<TEntity> doFindList(FindOptions options) {
                    return SyncReadOperationImpl.this.doFindList(options);
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

//    public interface DoCountProxy {
//
//        long doCount(@Nullable final ClientSession session, final CountOptions options);
//    }

}
