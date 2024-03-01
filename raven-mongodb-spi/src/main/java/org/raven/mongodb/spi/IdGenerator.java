package org.raven.mongodb.spi;


import java.util.List;

/**
 * @author yi.liang
 * @since JDK11
 */
public interface IdGenerator<TKey> {

    TKey generateId();

    List<TKey> generateIdBatch(long count);

    String name();
}
