package org.raven.mongodb.repository;

import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.27 11:17
 */
public interface MongoOptions {

    default Sequence getSequence() {
        return null;
    }

//    default String getCollectionName() {
//        return null;
//    }

//    default WriteConcern getWriteConcern() {
//        return null;
//    }
//
//    default ReadPreference getReadPreference() {
//        return null;
//    }

    default <T, TMongoDatabase> IdGeneratorProvider<T, TMongoDatabase> getIdGeneratorProvider() {
        return null;
    }

}
