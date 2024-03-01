package org.raven.mongodb.repository.spi;

public interface Sequence {

    String getSequenceName();

    String getCollectionName();

    String getIncrementName();

}
