package org.raven.mongodb.repository;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author by yanfeng
 * date 2021/9/22 21:34
 */
@Data
@Accessors(fluent = true)
public class FindOneAndDeleteOptions {
    private Bson sort;
    private Bson filter;
    private Bson update;
    private Boolean upsert;
    private Bson hint;

    /**
     *
     */
    public FindOneAndDeleteOptions() {
    }


    /**
     * @return Empty
     */
    public static FindOneAndDeleteOptions Empty() {
        return new FindOneAndDeleteOptions();
    }
}
