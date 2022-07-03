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
import org.raven.mongodb.repository.interceptors.EntityInterceptor;
//import org.raven.mongodb.repository.spi.IdGenerationType;
import org.raven.mongodb.repository.util.EntityInterceptorUtils;

import java.util.List;

/**
 * @author by yanfeng
 * date 2021/9/20 17:34
 */
public class EntityInformationSupport<TEntity extends Entity<TEntity>, TKey> implements EntityInformation<TEntity, TKey> {

    private final String collectionName;
    private final Class<TEntity> entityClass;
    private final Class<TKey> keyClass;
    private final CodecRegistry pojoCodecRegistry;
    private final ClassModel<TEntity> classModel;

//    private IdGenerationType idGenerationType;

    private final List<EntityInterceptor> interceptors;

    /**
     * Constructor
     *
     * @param entityClass    TEntity
     * @param keyClass       TKey
     * @param collectionName collectionName
     */
    public EntityInformationSupport(final Class<TEntity> entityClass, final Class<TKey> keyClass, final String collectionName) {
        this.entityClass = entityClass;
        this.keyClass = keyClass;
        this.pojoCodecRegistry = PojoCodecRegistry.CODEC_REGISTRY;

        this.classModel = ClassModelUtils.getClassModel(entityClass);
        this.interceptors = EntityInterceptorUtils.getInterceptors(entityClass);

//        if (BsonConstant.AUTO_INCR_CLASS.isAssignableFrom(entityClass)) {
//            idGenerationType = IdGenerationType.AUTO_INCR;
//        } else if (BsonConstant.OBJECT_ID_CLASS.isAssignableFrom(entityClass)) {
//            idGenerationType = IdGenerationType.OBJECT_ID;
//        } else
//            idGenerationType = IdGenerationType.NONE;

        this.collectionName = collectionName == null || collectionName.isEmpty()
                ? getEntityName()
                : collectionName;
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
    public List<EntityInterceptor> getInterceptors() {
        return interceptors;
    }

    @Override
    public ClassModel<TEntity> getClassModel() {
        return classModel;
    }

    @Override
    public String getCollectionName() {
        return collectionName;
    }

    @Override
    public Class<TEntity> getEntityType() {
        return entityClass;
    }

    @Override
    public Class<TKey> getIdType() {
        return keyClass;
    }

//    @Override
//    public IdGenerationType getIdGenerationType() {
//        return idGenerationType;
//    }

    @Override
    public String getEntityName() {
        return DocumentNamed.getNamed(entityClass);
    }

}
