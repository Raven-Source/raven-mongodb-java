package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.Sequence;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.27 11:17
 */
public interface MongoOptions {

    String getConnString();

    String getDbName();

    default Sequence getSequence() {
        return null;
    }

    default String getCollectionName() {
        return null;
    }

    default WriteConcern getWriteConcern() {
        return null;
    }

    default ReadPreference getReadPreference() {
        return null;
    }

    default IdGeneratorProvider getIdGeneratorProvider() {
        return null;
    }

}
