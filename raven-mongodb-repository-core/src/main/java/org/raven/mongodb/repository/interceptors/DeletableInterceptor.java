package org.raven.mongodb.repository.interceptors;

import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.codecs.pojo.PropertyModel;
import org.bson.conversions.Bson;
import org.raven.commons.data.Deletable;
import org.raven.mongodb.repository.AbstractFindOptions;
import org.raven.mongodb.repository.EntityInformation;
import org.raven.mongodb.repository.UpdateOptions;

/**
 * @author by yanfeng
 * date 2021/9/22 20:54
 */
public class DeletableInterceptor implements EntityInterceptor {

    @Override
    public void preFind(final AbstractFindOptions options,
                        final EntityInformation<?, ?> entityInformation) {

        if (Deletable.class.isAssignableFrom(entityInformation.getEntityType())) {

            Bson filter = filterProcess(options.filter(), entityInformation);
            options.filter(filter);

        }

    }

    @Override
    public void postUpdate(UpdateOptions options, EntityInformation<?, ?> entityInformation) {

        if (Deletable.class.isAssignableFrom(entityInformation.getEntityType())) {

            Bson filter = filterProcess(options.filter(), entityInformation);
            options.filter(filter);

        }
    }

    /**
     *
     */
    private Bson filterProcess(final Bson filter, final EntityInformation<?, ?> entityInformation) {

        PropertyModel<?> propertyModel = entityInformation.getClassModel().getPropertyModel(Deletable.DEL);

        BsonDocument bsonDocument;
        if (filter == null) {
            bsonDocument = new BsonDocument();
        } else {
            bsonDocument = filter.toBsonDocument();
        }

        if (!bsonDocument.containsKey(propertyModel.getName())) {
            return Filters.and(filter, Filters.eq(propertyModel.getWriteName(), false));
        } else {
            return filter;
        }
    }
}
