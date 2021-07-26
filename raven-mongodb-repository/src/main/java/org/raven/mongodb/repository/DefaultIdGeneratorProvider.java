package org.raven.mongodb.repository;

import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.raven.commons.data.AutoIncr;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.IdGenerator;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;

import java.util.function.Supplier;

/**
 * @author yi.liang
 * date 2021.07.26 18:37
 */
public class DefaultIdGeneratorProvider implements IdGeneratorProvider<IdGenerator<?>, MongoDatabase> {

    public static final DefaultIdGeneratorProvider Default = new DefaultIdGeneratorProvider();

    @Override
    public <TEntity, TKey> IdGenerator<TKey> build(String collectionName, Class<TEntity> entityClazz, Class<TKey> keyClazz, Supplier<MongoDatabase> databaseSupplier) {

        if (BsonConstant.AUTO_INCR_CLASS.isAssignableFrom(entityClazz)) {
            return new IncrementIdGeneration(collectionName, new MongoSequence(), keyClazz, databaseSupplier);
        } else if (keyClazz.equals(ObjectId.class) || keyClazz.equals(String.class)) {
            return new ObjectIdIdGeneration<>(keyClazz);
        } else throw new MongoException("not super Key type");

    }
}
