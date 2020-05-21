package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @param <TEntity>
 * @author yi.liang
 * @since JDK1.8
 */
public interface MongoBaseRepository<TEntity> {

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
