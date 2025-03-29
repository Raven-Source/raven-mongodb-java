package org.raven.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.raven.commons.util.PropertiesUtils;
import org.raven.mongodb.spi.Sequence;

/**
 * @author yi.liang
 */
@Getter
@Setter
public class MongoSequence implements Sequence {

    private static final String PROPERTIES_FILE = "META-INF/mongodb.properties";

    private String sequenceName;

    private String collectionName;

    private String incrementName;

    /**
     * @param sequence       sequence
     * @param collectionName collectionName
     * @param incrementName  incrementName
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

        this.sequenceName = PropertiesUtils.getString(PROPERTIES_FILE, AvailableSettings.SEQUENCE_SEQUENCE_NAME, "_sequence");
        this.collectionName = PropertiesUtils.getString(PROPERTIES_FILE, AvailableSettings.SEQUENCE_COLLECTION_NAME, "_id");
        this.incrementName = PropertiesUtils.getString(PROPERTIES_FILE, AvailableSettings.SEQUENCE_INCREMENT_NAME, "incr");
    }
}
