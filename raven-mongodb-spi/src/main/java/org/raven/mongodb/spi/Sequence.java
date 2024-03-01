package org.raven.mongodb.spi;

public interface Sequence {

    String getSequenceName();

    String getCollectionName();

    String getIncrementName();

}
