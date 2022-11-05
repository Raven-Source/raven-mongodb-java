package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.annotations.PreFind;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

import javax.annotation.Nullable;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 */
public abstract class AbstractMongoBaseRepository<TEntity extends Entity<TKey>, TKey>
        extends AbstractMongoRepository<TEntity, TKey>
        implements MongoBaseRepository<TEntity> {

    protected IdGenerator<TKey> idGenerator;

    protected MongoSession mongoSession;

    protected MongoDatabase mongoDatabase;

    /**
     * @return MongoDatabase
     */
    @Override
    public MongoDatabase getDatabase() {

        return mongoSession.getDatabase().withCodecRegistry(entityInformation.getCodecRegistry());
    }

    //#region constructor

    public AbstractMongoBaseRepository(final MongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<IdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {
        super(collectionName);

        this.mongoSession = mongoSession;
        this.mongoDatabase = mongoSession.getDatabase().withCodecRegistry(entityInformation.getCodecRegistry());

        this.idGenerator = idGeneratorProvider != null ?
                idGeneratorProvider.build(this.getCollectionName(),
                        sequence,
                        entityInformation.getEntityType(),
                        entityInformation.getIdType(),
                        this::getDatabase) :
                DefaultIdGeneratorProvider.Default.build(
                        this.getCollectionName(),
                        sequence,
                        entityInformation.getEntityType(),
                        entityInformation.getIdType(),
                        this::getDatabase);
    }


    public AbstractMongoBaseRepository(final MongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    public AbstractMongoBaseRepository(final MongoSession mongoSession, final String collectionName) {
        this(mongoSession, collectionName, null, null);
    }

    @SuppressWarnings({"unchecked"})
    public AbstractMongoBaseRepository(final MongoSession mongoSession, final MongoOptions mongoOptions) {
        this(mongoSession, null, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    @SuppressWarnings({"unchecked"})
    public AbstractMongoBaseRepository(final MongoSession mongoSession, final MongoOptions mongoOptions, final String collectionName) {
        this(mongoSession, collectionName, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
    }

    //#endregion


    //#region getCollection

    /**
     * Get the collection based on the data type
     *
     * @return Collection
     */
    @Override
    public MongoCollection<TEntity> getCollection() {
        return getDatabase().getCollection(getCollectionName(), entityInformation.getEntityType());
    }

    /**
     * Get the collection based on the data type
     *
     * @param writeConcern WriteConcern
     * @return Collection
     * @see WriteConcern
     */
    @Override
    public MongoCollection<TEntity> getCollection(final WriteConcern writeConcern) {
        MongoCollection<TEntity> collection = this.getCollection();
        if (writeConcern != null) {
            collection = collection.withWriteConcern(writeConcern);
        }
        return collection;
    }

    /**
     * Get the collection based on the data type
     *
     * @param readPreference ReadPreference
     * @return Collection
     * @see ReadPreference
     */
    @Override
    public MongoCollection<TEntity> getCollection(final ReadPreference readPreference) {
        MongoCollection<TEntity> collection = this.getCollection();
        if (readPreference != null) {
            collection = collection.withReadPreference(readPreference);
        }
        return collection;
    }

    //#endregion

    /**
     * @param findIterable findIterable
     * @param projection   projection
     * @param sort         sort
     * @param limit        limit
     * @param skip         skip
     * @param hint         hint
     * @return FindIterable
     */
    protected <TResult> FindIterable<TResult> findOptions(final FindIterable<TResult> findIterable,
                                                          @Nullable final Bson projection,
                                                          @Nullable final Bson sort,
                                                          final int limit,
                                                          final int skip,
                                                          @Nullable final Bson hint) {

        FindIterable<TResult> filter = findIterable;
        if (projection != null) {
            filter = filter.projection(projection);
        }

        if (limit > 0) {
            filter = filter.limit(limit);
        }

        if (skip > 0) {
            filter = filter.skip(skip);
        }

        if (sort != null) {
            filter = filter.sort(sort);
        }

        if (hint != null) {
//            Bson hintBson = new BsonDocument("$hint", hint.toBsonDocument());
            filter = filter.hint(hint);
        }

        return filter;

    }

    protected <TResult> FindIterable<TResult> doFind(final FindOptions options, final Class<TResult> resultClass) {

        if (options.filter() == null) {
            options.filter(Filters.empty());
        }

        Bson projection = BsonUtils.projection(entityInformation.getEntityType(),
                options.includeFields(), options.excludeFields());

        callGlobalInterceptors(PreFind.class, null, options);

        FindIterable<TResult> result = getCollection(options.readPreference()).find(options.filter(), resultClass);
        result = findOptions(result, projection, options.sort(), options.limit(), options.skip(), options.hint());

        return result;
    }

}
