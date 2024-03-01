package org.raven.mongodb.spi;

import java.util.function.Supplier;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.26 18:37
 */
public interface IdGeneratorProvider<T, TMongoDatabase> {

    <TEntity, TKey> T build(String collectionName, Sequence sequence, Class<TEntity> entityClazz, Class<TKey> keyClazz, Supplier<TMongoDatabase> databaseSupplier);

}
