package org.raven.mongodb.repository.reactive;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.*;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.List;

/**
 * @param <TEntity> TEntity
 * @param <TKey>    TKey
 * @author yi.liang
 * @since JDK11
 */
@SuppressWarnings({"unchecked"})
public abstract class AbstractReactiveMongoBaseRepository<TEntity extends Entity<TKey>, TKey>
        extends AbstractMongoRepository<TEntity, TKey>
        implements ReactiveMongoBaseRepository<TEntity> {

    protected ReactiveIdGenerator<TKey> idGenerator;

    protected ReactiveMongoSession mongoSession;

    protected MongoDatabase mongoDatabase;

    /**
     * Collection Name
     *
     * @return Collection Name
     */
    protected String getCollectionName() {
        return collectionName;
    }

    /**
     * @return MongoDatabase
     */
    @Override
    public MongoDatabase getDatabase() {

        return mongoDatabase;
    }

    //#region constructor
    public AbstractReactiveMongoBaseRepository(final ReactiveMongoSession mongoSession, final String collectionName, final Sequence sequence
            , final IdGeneratorProvider<ReactiveIdGenerator<TKey>, MongoDatabase> idGeneratorProvider) {

        super(collectionName);

        this.mongoSession = mongoSession;
        this.mongoDatabase = mongoSession.getDatabase().withCodecRegistry(entityInformation.getCodecRegistry());

        this.idGenerator = idGeneratorProvider != null ?
                idGeneratorProvider.build(this.collectionName,
                        sequence,
                        entityInformation.getEntityType(),
                        entityInformation.getIdType(),
                        this::getDatabase) :
                DefaultIdGeneratorProvider.Default.build(this.collectionName,
                        sequence,
                        entityInformation.getEntityType(),
                        entityInformation.getIdType(),
                        this::getDatabase);
    }

    /**
     * constructor
     *
     * @param mongoSession mongoSession
     */
    public AbstractReactiveMongoBaseRepository(final ReactiveMongoSession mongoSession) {
        this(mongoSession, null, null, null);
    }

    /**
     * constructor
     *
     * @param mongoOptions mongoOptions
     */
    public AbstractReactiveMongoBaseRepository(final MongoOptions mongoOptions, final String collectionName) {
        this(new DefaultReactiveMongoSession(mongoOptions), collectionName, mongoOptions.getSequence(), mongoOptions.getIdGeneratorProvider());
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
     * @param includeFields includeFields
     * @return Bson
     */
    protected Bson includeFields(final List<String> includeFields) {
        return BsonUtils.includeFields(includeFields);
    }

    /**
     * @param findPublisher findPublisher
     * @param projection    projection
     * @param sort          sort
     * @param limit         limit
     * @param skip          skip
     * @param hint          hint
     * @return FindIterable
     */
    protected FindPublisher<TEntity> findOptions(final FindPublisher<TEntity> findPublisher, final Bson projection, final Bson sort
            , final int limit, final int skip, final Bson hint) {

        FindPublisher<TEntity> filter = findPublisher;
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
            Bson hintBson = new BsonDocument("$hint", hint.toBsonDocument());
            filter = filter.hint(hintBson);
        }

        return filter;

    }

}
