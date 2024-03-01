package org.raven.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.model.User;
import org.raven.mongodb.query.SortBuilder;

/**
 * @author by yanfeng
 * date 2021/9/13 0:07
 */
@Slf4j
public class SortBuilderTest {

    @Test
    public void sortTest() {

        SortBuilder<User> sortBuilder = new SortBuilder<>(User.class);
        sortBuilder
                .ascending("a")
                .descending("b")
                .ascending("c", "d")
                .descending("e", "f")
                .ascending("createDate");


        Bson bson = sortBuilder.build();
        log.info(bson.toBsonDocument().toJson());


        Assert.assertEquals("{\"a\": 1, \"b\": -1, \"c\": 1, \"d\": 1, \"e\": -1, \"f\": -1, \"CreateDate\": 1}"
                , bson.toBsonDocument().toJson()
        );
    }


}
