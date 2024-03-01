package org.raven.mongodb.reactive;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

/**
 * @param <TEntity> TEntity
 * @author yi.liang
 */
public interface ReactiveMongoBaseRepository<TEntity> {

    /**
     * @return collectionName
     */
    String getCollectionName();

    /**
     * @return MongoDatabase
     */
    MongoDatabase getDatabase();

    /**
     * Get the collection based on the data type
     *
     * @return MongoCollection
     */
    MongoCollection<TEntity> getCollection();

    /**
     * Get the collection based on the data type
     *
     * @param writeConcern WriteConcern
     * @return MongoCollection
     * @see WriteConcern
     */
    MongoCollection<TEntity> getCollection(WriteConcern writeConcern);

    /**
     * Get the collection based on the data type
     *
     * @param readPreference ReadPreference
     * @return MongoCollection
     * @see ReadPreference
     */
    MongoCollection<TEntity> getCollection(ReadPreference readPreference);
}
