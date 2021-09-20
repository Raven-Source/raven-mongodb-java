package org.raven.mongodb.repository;

import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;
import org.raven.mongodb.repository.query.HintBuilder;

/**
 * @author by yanfeng
 * date 2021/9/13 0:07
 */
@Slf4j
public class HintBuilderTest {

    @Test
    public void updaterTest() {

        HintBuilder<User> updateBuilder = new HintBuilder<>(User.class);
        updateBuilder
                .ascending("a")
                .descending("b")
                .ascending("c", "d")
                .descending("e", "f")
                .ascending("createDate");


        Bson bson = updateBuilder.build();
        log.info(bson.toBsonDocument().toJson());


        Assert.assertEquals("{\"a\": 1, \"b\": -1, \"c\": 1, \"d\": 1, \"e\": -1, \"f\": -1, \"CreateDate\": 1}"
                , bson.toBsonDocument().toJson()
        );
    }


}
