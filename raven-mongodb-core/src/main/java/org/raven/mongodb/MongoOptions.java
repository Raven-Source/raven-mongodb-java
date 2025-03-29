package org.raven.mongodb;

import org.raven.mongodb.spi.IdGeneratorProvider;
import org.raven.mongodb.spi.Sequence;

/**
 * @author yi.liang
 * date 2021.07.27 11:17
 */
public interface MongoOptions {

//    default Sequence getSequence() {
//        return null;
//    }

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
