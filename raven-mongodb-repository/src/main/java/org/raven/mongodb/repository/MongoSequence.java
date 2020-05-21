package org.raven.mongodb.repository;

/**
 * @author yi.liang
 * @since JDK1.8
 */
public class MongoSequence {

    private String sequenceName;

    private String collectionName;

    private String incrementID;

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

    public String getIncrementID() {
        return incrementID;
    }

    public void setIncrementID(String incrementID) {
        this.incrementID = incrementID;
    }


    /**
     * @param sequence
     * @param collectionName
     * @param incrementID
     */
    public MongoSequence(final String sequence, final String collectionName, final String incrementID) {
        this.sequenceName = sequence;
        this.collectionName = collectionName;
        this.incrementID = incrementID;
    }

    /**
     *
     */
    public MongoSequence() {

        this.sequenceName = "_Sequence";
        this.collectionName = "_id";
        this.incrementID = "IncrID";
    }
}
