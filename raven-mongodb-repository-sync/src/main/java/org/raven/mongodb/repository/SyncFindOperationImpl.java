package org.raven.mongodb.repository;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.raven.commons.function.Function2;
import org.raven.mongodb.repository.contants.BsonConstant;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SyncFindOperationImpl<TEntity, TKey> implements SyncFindOperation<TEntity, TKey> {

    private final DoFindProxy doFindProxy;
    private final Function2<ClientSession, CountOptions, Long> doCountProxy;
//    private final FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> proxy;

    protected final EntityInformation<TEntity, TKey> entityInformation;

    protected final @Nullable ClientSession clientSession;

    public SyncFindOperationImpl(DoFindProxy doFindProxy,
                                 Function2<ClientSession, CountOptions, Long> doCountProxy,
                                 EntityInformation<TEntity, TKey> entityInformation,
                                 @Nullable ClientSession clientSession) {
        this.doFindProxy = doFindProxy;
        this.doCountProxy = doCountProxy;
        this.entityInformation = entityInformation;
        this.clientSession = clientSession;
    }

    protected SyncFindOperationImpl<TEntity, TKey> clone(ClientSession clientSession) {
        return new SyncFindOperationImpl<>(this.doFindProxy, this.doCountProxy, this.entityInformation, clientSession);
    }

    @Override
    public FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> findProxy() {
        return proxy;
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
        return this.doFindOne(options, entityInformation.getEntityType());
    }

    protected <TResult> TResult doFindOne(final FindOptions options, Class<TResult> resultClass) {
        return doFindProxy.doFind(clientSession, options, resultClass).first();
    }

    protected List<TEntity> doFindList(final FindOptions options) {
        return this.doFindList(options, entityInformation.getEntityType());
    }

    protected <TResult> List<TResult> doFindList(final FindOptions options, Class<TResult> resultClass) {
        FindIterable<TResult> result = doFindProxy.doFind(clientSession, options, resultClass);

        ArrayList<TResult> list = new ArrayList<>();
        for (TResult entity : result) {
            list.add(entity);
        }

        return list;
    }

    protected long doCount(final CountOptions options) {
        return doCountProxy.apply(clientSession, options);
    }

    protected boolean doExists(final ExistsOptions options) {

        Bson _filter = options.filter();
        if (_filter == null) {
            _filter = Filters.empty();
        }

        List<String> includeFields = new ArrayList<>(1);
        includeFields.add(BsonConstant.PRIMARY_KEY_NAME);

        return this.findOne(_filter, includeFields, null, options.hint(), options.readPreference()) != null;
    }

    //endregion

    public interface DoFindProxy {

        <TResult> FindIterable<TResult> doFind(@Nullable final ClientSession session, final FindOptions options, final Class<TResult> resultClass);
    }

    public FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> proxy() {
        return proxy;
    }

    private final FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean> proxy =
            new FindProxy<TEntity, TKey, TEntity, List<TEntity>, Long, Boolean>() {
                @Override
                public EntityInformation<TEntity, TKey> getEntityInformation() {
                    return SyncFindOperationImpl.this.entityInformation;
                }

                @Override
                public TEntity doFindOne(FindOptions options) {
                    return SyncFindOperationImpl.this.doFindOne(options);
                }

                @Override
                public List<TEntity> doFindList(FindOptions options) {
                    return SyncFindOperationImpl.this.doFindList(options);
                }

                @Override
                public Long doCount(CountOptions options) {
                    return SyncFindOperationImpl.this.doCount(options);
                }

                @Override
                public Boolean doExists(ExistsOptions options) {
                    return SyncFindOperationImpl.this.doExists(options);
                }
            };

//    public interface DoCountProxy {
//
//        long doCount(@Nullable final ClientSession session, final CountOptions options);
//    }

}
