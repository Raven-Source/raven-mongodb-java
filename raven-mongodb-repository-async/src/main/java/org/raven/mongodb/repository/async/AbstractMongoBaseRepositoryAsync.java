package org.raven.mongodb.repository.async;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.async.client.FindIterable;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.MongoRepositoryOptions;
import org.raven.mongodb.repository.MongoSequence;
import org.raven.mongodb.repository.codec.PojoCodecRegistrys;
import org.raven.mongodb.repository.contants.BsonConstant;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public abstract class AbstractMongoBaseRepositoryAsync<TEntity extends Entity<TKey>, TKey>
    implements MongoBaseRepositoryAsync<TEntity> {
    protected Class<TEntity> entityClazz;
    protected Class<TKey> keyClazz;
    protected Boolean isAutoIncrClass;

    /**
     * Mongo Auto Increment ID Collection
     */
    protected MongoSequence _sequence;

    protected MongoSessionAsync _mongoSession;

    private String collectionName;

    private CodecRegistry pojoCodecRegistry;


    /**
     * Collection Name
     *
     * @return Collection Name
     */
    protected String getCollectionName() {
        return collectionName;
    }

    /**
     * MongoDatabase
     *
     * @return MongoDatabase
     */
    @Override
    public MongoDatabase getDatabase() {

        return _mongoSession.getDatabase().withCodecRegistry(pojoCodecRegistry);

    }

    //#region constructor

    private AbstractMongoBaseRepositoryAsync() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClazz = (Class) params[0];
        keyClazz = (Class) params[1];
        isAutoIncrClass = BsonConstant.AUTO_INCR_CLASS.isAssignableFrom(entityClazz);

        pojoCodecRegistry = PojoCodecRegistrys.CODEC_REGISTRY;
    }

    /**
     * constructor
     *
     * @param connString     connString
     * @param dbName         dbName
     * @param collectionName collectionName
     * @param writeConcern   WriteConcern
     * @param readPreference ReadPreference
     * @param sequence       Mongo Auto Increment ID Collection
     * @see WriteConcern
     * @see ReadPreference
     */
    public AbstractMongoBaseRepositoryAsync(final String connString, final String dbName, final String collectionName, final WriteConcern writeConcern, final ReadPreference readPreference, final MongoSequence sequence) {
        this();
        this._sequence = sequence != null ? sequence : new MongoSequence();
        this._mongoSession = new MongoSessionAsync(connString, dbName, writeConcern, false, readPreference);
        this.collectionName = collectionName;
        if (this.collectionName == null || this.collectionName.isEmpty()) {
            //String[] arr = entityClazz.getName().split("\\.");
            this.collectionName = entityClazz.getSimpleName();
        }
    }

    /**
     * constructor
     *
     * @param connString connString
     * @param dbName     dbName
     */
    public AbstractMongoBaseRepositoryAsync(final String connString, final String dbName) {
        this(connString, dbName, null, null, null, null);
    }

    /**
     * constructor
     *
     * @param options MongoRepositoryOptions
     * @see MongoRepositoryOptions
     */
    public AbstractMongoBaseRepositoryAsync(final MongoRepositoryOptions options) {
        this(options.getConnString(), options.getDbName(), options.getCollectionName(), options.getWriteConcern(), options.getReadPreference(), options.getSequence());
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
        return getDatabase().getCollection(getCollectionName(), entityClazz);
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
     * @param entity entity
     * @return BsonDocument
     */
    protected BsonDocument toBsonDocument(final TEntity entity) {

        return BsonConstant.convertToBsonDocument(entity, pojoCodecRegistry.get(entityClazz));
    }

    /**
     * @param includeFields includeFields
     * @return Bson
     */
    protected Bson includeFields(final List<String> includeFields) {
        return BsonConstant.includeFields(includeFields);
    }


    /**
     * @param findIterable findIterable
     * @param projection   projection
     * @param sort         sort
     * @param limit        limit
     * @param skip         skip
     * @param hint         hint
     * @return FindIterable
     */
    protected FindIterable<TEntity> findOptions(final FindIterable<TEntity> findIterable, final Bson projection, final Bson sort
        , final int limit, final int skip, final BsonValue hint) {

        FindIterable<TEntity> filter = findIterable;
        if (projection != null) {
            filter = findIterable.projection(projection);
        }

        if (limit > 0) {
            filter = findIterable.limit(limit);
        }

        if (skip > 0) {
            filter = findIterable.skip(skip);
        }

        if (sort != null) {
            filter = findIterable.sort(sort);
        }

        if (hint != null) {
            Bson hintBson = new BsonDocument("$hint", hint);
            filter = findIterable.hint(hintBson);
        }

        return filter;

    }

    /**
     * ID assignment
     *
     * @param entity entity
     * @param id     id
     */
    protected void assignmentEntityID(final TEntity entity, final long id) {
        BsonConstant.assignmentEntityID(keyClazz, entity, id);
    }

    /**
     * ID assignment
     *
     * @param entity entity
     * @param id     id
     */
    protected void assignmentEntityID(final TEntity entity, final ObjectId id) {
        BsonConstant.assignmentEntityID(entity, id);

    }

}
