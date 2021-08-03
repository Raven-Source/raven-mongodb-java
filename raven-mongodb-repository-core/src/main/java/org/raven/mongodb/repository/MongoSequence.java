package org.raven.mongodb.repository;

/**
 * @author yi.liang
 * @since JDK11
 */
public class MongoSequence {

    private String sequenceName;

    private String collectionName;

    private String increment;

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }


    /**
     * @param sequence
     * @param collectionName
     * @param increment
     */
    public MongoSequence(final String sequence, final String collectionName, final String increment) {
        this.sequenceName = sequence;
        this.collectionName = collectionName;
        this.increment = increment;
    }

    /**
     *
     */
    public MongoSequence() {

        this.sequenceName = "_sequence";
        this.collectionName = "_id";
        this.increment = "incr";
    }
}
