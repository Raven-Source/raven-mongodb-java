package org.raven.mongodb.repository.conventions;

import org.bson.codecs.pojo.PropertySerialization;

/**
 * @author yi.liang
 * @since JDK11
 * date 2020.05.22 02:33
 */
public class CustomPropertySerialization<T> implements PropertySerialization<T> {

    @Override
    public boolean shouldSerialize(T value) {
        return true;
    }
}
