package org.raven.mongodb.repository.reactive;

import com.mongodb.MongoException;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.raven.commons.data.AutoIncr;
import org.raven.mongodb.repository.MongoSequence;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;

import java.util.function.Supplier;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.26 18:37
 */
public class DefaultIdGeneratorProvider implements IdGeneratorProvider<ReactiveIdGenerator<?>, MongoDatabase> {

    public static final DefaultIdGeneratorProvider Default = new DefaultIdGeneratorProvider();

    @Override
    public <TEntity, TKey> ReactiveIdGenerator<TKey> build(String collectionName, Class<TEntity> entityClazz, Class<TKey> keyClazz, Supplier<MongoDatabase> databaseSupplier) {

        if (BsonConstant.AUTO_INCR_CLASS.isAssignableFrom(entityClazz)) {
            return new IncrementReactiveIdGeneration(collectionName, new MongoSequence(), keyClazz, databaseSupplier);
        } else if (keyClazz.equals(ObjectId.class) || keyClazz.equals(String.class)) {
            return new ObjectIdReactiveIdGeneration<>(keyClazz);
        } else throw new MongoException("not super Key type");

    }
}
