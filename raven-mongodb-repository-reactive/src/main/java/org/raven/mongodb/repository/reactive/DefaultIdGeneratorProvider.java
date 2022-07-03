package org.raven.mongodb.repository.reactive;

import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.raven.mongodb.repository.MongoSequence;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.IdGeneratorProvider;
import org.raven.mongodb.repository.spi.ReactiveIdGenerator;
import org.raven.mongodb.repository.spi.Sequence;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author yi.liang
 * @since JDK11
 * date 2021.07.26 18:37
 */
@Slf4j
@SuppressWarnings({"unchecked"})
public class DefaultIdGeneratorProvider implements IdGeneratorProvider<ReactiveIdGenerator<?>, MongoDatabase> {

    public static final DefaultIdGeneratorProvider Default = new DefaultIdGeneratorProvider();

    @Override
    public <TEntity, TKey> ReactiveIdGenerator<TKey> build(String collectionName, Sequence sequence, Class<TEntity> entityClazz, Class<TKey> keyClazz, Supplier<MongoDatabase> databaseSupplier) {

        if (BsonConstant.AUTO_INCR_CLASS.isAssignableFrom(entityClazz)) {
            return new IncrementReactiveIdGeneration(collectionName, Optional.ofNullable(sequence).orElseGet(MongoSequence::new), keyClazz, databaseSupplier);
        } else if (keyClazz.equals(ObjectId.class) || keyClazz.equals(String.class)) {
            return new ObjectIdReactiveIdGeneration<>(keyClazz);
        } else {
            log.info(String.format("entity %s key not super IdGenerator. ", entityClazz.getName()));
            return null;
        }

    }
}
