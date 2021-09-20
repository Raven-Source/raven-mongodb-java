package org.raven.mongodb.repository.support;

import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.ClassModelUtils;
import org.raven.commons.data.Entity;
import org.raven.mongodb.repository.BsonUtils;
import org.raven.mongodb.repository.DocumentNamed;
import org.raven.mongodb.repository.EntityInformation;
import org.raven.mongodb.repository.codec.PojoCodecRegistry;
import org.raven.mongodb.repository.contants.BsonConstant;
import org.raven.mongodb.repository.spi.IdGenerationType;

/**
 * @author by yanfeng
 * date 2021/9/20 17:34
 */
public class EntityInformationSupport<TEntity extends Entity<TEntity>, TKey> implements EntityInformation<TEntity, TKey> {

    private final Class<TEntity> entityClass;
    protected Class<TKey> keyClass;
    private final CodecRegistry pojoCodecRegistry;
    private final ClassModel<TEntity> classModel;

    private IdGenerationType idGenerationType;

    /**
     * Constructor
     *
     * @param entityClass TEntity
     * @param keyClass    TKey
     */
    public EntityInformationSupport(Class<TEntity> entityClass, Class<TKey> keyClass) {
        this.entityClass = entityClass;
        this.keyClass = keyClass;
        this.pojoCodecRegistry = PojoCodecRegistry.CODEC_REGISTRY;

        this.classModel = ClassModelUtils.getClassModel(entityClass);

        if (BsonConstant.AUTO_INCR_CLASS.isAssignableFrom(entityClass)) {
            idGenerationType = IdGenerationType.AUTO_INCR;
        } else if (BsonConstant.OBJECT_ID_CLASS.isAssignableFrom(entityClass)) {
            idGenerationType = IdGenerationType.OBJECT_ID;
        } else
            idGenerationType = IdGenerationType.NONE;
    }

    @Override
    public BsonDocument toBsonDocument(TEntity entity) {
        return BsonUtils.convertToBsonDocument(entity, pojoCodecRegistry.get(entityClass));
    }

    @Override
    public CodecRegistry getCodecRegistry() {
        return pojoCodecRegistry;
    }

    @Override
    public Class<TEntity> getEntityType() {
        return entityClass;
    }

    @Override
    public Class<TKey> getIdType() {
        return keyClass;
    }

    @Override
    public IdGenerationType getIdGenerationType() {
        return idGenerationType;
    }

    @Override
    public String getEntityName() {
        return DocumentNamed.getNamed(entityClass);
    }
}
