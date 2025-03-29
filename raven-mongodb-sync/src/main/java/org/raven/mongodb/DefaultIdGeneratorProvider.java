package org.raven.mongodb;

import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.raven.mongodb.contants.BsonConstant;
import org.raven.mongodb.spi.IdGenerator;
import org.raven.mongodb.spi.IdGeneratorProvider;
import org.raven.mongodb.spi.Sequence;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author yi.liang
 * date 2021.07.26 18:37
 */
@Slf4j
@SuppressWarnings({"unchecked"})
public class DefaultIdGeneratorProvider implements IdGeneratorProvider<IdGenerator<?>, MongoDatabase> {

    public static final DefaultIdGeneratorProvider Default = new DefaultIdGeneratorProvider();

    @Override
    public <TEntity, TKey> IdGenerator<TKey> build(String collectionName, Class<TEntity> entityClazz, Class<TKey> keyClazz, Supplier<MongoDatabase> databaseSupplier) {
        return build(collectionName, null, entityClazz, keyClazz, databaseSupplier);
    }

    public <TEntity, TKey> IdGenerator<TKey> build(String collectionName, @Nullable Sequence sequence, Class<TEntity> entityClazz, Class<TKey> keyClazz, Supplier<MongoDatabase> databaseSupplier) {

        if (BsonConstant.AUTO_INCR_CLASS.isAssignableFrom(entityClazz)) {
            return new IncrementIdGeneration(collectionName, Optional.ofNullable(sequence).orElseGet(MongoSequence::new), keyClazz, databaseSupplier);
        } else if (keyClazz.equals(ObjectId.class) || keyClazz.equals(String.class)) {
            return new ObjectIdIdGeneration<>(keyClazz);
        } else {
            log.info(String.format("entity %s key not super IdGenerator. ", entityClazz.getName()));
            return null;
        }

    }
}
