package org.raven.mongodb.repository.contants;

import org.bson.types.ObjectId;
import org.raven.commons.data.AutoIncr;
import org.raven.commons.data.Deletable;
import org.raven.commons.data.Versioned;

/**
 * @author yi.liang
 * @since JDK11
 */
@SuppressWarnings({"unchecked"})
public abstract class BsonConstant {

    public static final String PRIMARY_KEY_NAME = "_id";

    public static final Class<AutoIncr> AUTO_INCR_CLASS = AutoIncr.class;

    public static final Class<ObjectId> OBJECT_ID_CLASS = ObjectId.class;

    public static final Class<Deletable> DELETABLE_CLASS = Deletable.class;

    public static final Class<Versioned> VERSIONED_CLASS = Versioned.class;


}
