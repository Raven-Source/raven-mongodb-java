package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

/**
 * @author yi.liang
 * @since JDK1.8
 */
@Deprecated
public interface MongoRepositoryOptions {

    /**
     * Mongodb Uri
     *
     * @return
     */
    String getUri();

    /**
     * DbName
     */
    String getDbName();

    /**
     * @return CollectionName (Nullable)
     */
    default String getCollectionName() {
        return null;
    }

    /**
     * @return WriteConcern (Nullable)
     */
    default WriteConcern getWriteConcern() {
        return null;
    }

    /**
     * @return ReadPreference (Nullable)
     */
    default ReadPreference getReadPreference() {
        return null;
    }

    /**
     * @return MongoSequence (Nullable)
     */
    default MongoSequence getSequence() {
        return null;
    }


}
