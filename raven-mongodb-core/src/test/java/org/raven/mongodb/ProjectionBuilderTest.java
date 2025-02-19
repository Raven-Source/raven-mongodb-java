package org.raven.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.model.User;
import org.raven.mongodb.builders.ProjectionBuilder;

/**
 * date 2022/11/5 17:26
 */
@Slf4j
public class ProjectionBuilderTest {

    @Test
    public void projectionTest() {

        ProjectionBuilder<User> projectionBuilder = ProjectionBuilder.create(User.class);

        projectionBuilder.include(User.Fields.mall, User.Fields.name);
        projectionBuilder.exclude(User.Fields.createDate, User.Fields.version);
        projectionBuilder.excludeId();

        Bson bson = projectionBuilder.build();
        log.info(bson.toBsonDocument().toJson());

        Assert.assertEquals("{\"Mall\": 1, \"Name\": 1, \"CreateDate\": 0, \"Version\": 0, \"_id\": 0}"
                , bson.toBsonDocument().toJson()
        );

    }

}
