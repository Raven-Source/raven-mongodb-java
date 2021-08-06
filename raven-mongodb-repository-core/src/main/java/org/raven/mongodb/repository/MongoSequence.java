package org.raven.mongodb.repository;

import lombok.Getter;
import lombok.Setter;
import org.raven.mongodb.repository.spi.Sequence;

/**
 * @author yi.liang
 * @since JDK11
 */
@Getter
@Setter
public class MongoSequence implements Sequence {

    private String sequenceName;

    private String collectionName;

    private String incrementName;

    /**
     * @param sequence
     * @param collectionName
     * @param incrementName
     */
    public MongoSequence(final String sequence, final String collectionName, final String incrementName) {
        this.sequenceName = sequence;
        this.collectionName = collectionName;
        this.incrementName = incrementName;
    }

    /**
     *
     */
    public MongoSequence() {

        this.sequenceName = "_sequence";
        this.collectionName = "_id";
        this.incrementName = "incr";
    }
}
