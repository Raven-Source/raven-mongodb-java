package org.raven.mongodb;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoSessionInstance {

    private String cstr;

    private String dbName;

    public MongoSession init() {
        return new DefaultMongoSession(cstr
                , dbName, null, null);
    }
}
