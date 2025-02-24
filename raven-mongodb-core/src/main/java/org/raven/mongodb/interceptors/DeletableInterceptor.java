package org.raven.mongodb.interceptors;

import com.mongodb.client.model.Filters;
import org.bson.BsonDocument;
import org.bson.codecs.pojo.PropertyModel;
import org.bson.conversions.Bson;
import org.raven.commons.data.Deletable;
import org.raven.mongodb.criteria.BaseFindOptions;
import org.raven.mongodb.criteria.BaseUpdateOptions;
import org.raven.mongodb.util.BsonUtils;
import org.raven.mongodb.EntityInformation;

/**
 * @author by yanfeng
 * date 2021/9/22 20:54
 */
public class DeletableInterceptor implements EntityInterceptor {

    @Override
    public void preFind(final BaseFindOptions<?> options,
                        final EntityInformation<?, ?> entityInformation) {

        if (Deletable.class.isAssignableFrom(entityInformation.getEntityType())) {

            Bson filter = filterProcess(options.filter(), entityInformation);
            options.filter(filter);

        }

    }

    @Override
    public void preUpdate(BaseUpdateOptions<?> options, EntityInformation<?, ?> entityInformation) {

        if (Deletable.class.isAssignableFrom(entityInformation.getEntityType())) {

            Bson filter = filterProcess(options.filter(), entityInformation);
            options.filter(filter);

        }
    }

    /**
     *
     */
    private Bson filterProcess(final Bson filter, final EntityInformation<?, ?> entityInformation) {

        PropertyModel<?> propertyModel = entityInformation.getClassModel().getPropertyModel(Deletable.DELETED);

        Bson delBson = Filters.eq(propertyModel.getWriteName(), false);

        if (filter == null) {
            return delBson;
        } else {
            BsonDocument bsonDocument = filter.toBsonDocument();
            if (!bsonDocument.containsKey(propertyModel.getName())) {

                return BsonUtils.combine(filter, delBson);
            } else {
                return filter;
            }
        }
    }
}
