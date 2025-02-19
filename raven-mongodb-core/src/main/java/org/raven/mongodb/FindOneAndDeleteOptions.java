package org.raven.mongodb;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author by yanfeng
 * date 2021/9/22 21:34
 */
@Data
@Accessors(fluent = true)
public class FindOneAndDeleteOptions extends AbstractFilterOptions<FindOneAndDeleteOptions> implements Options {

    private Bson sort;

    /**
     *
     */
    public FindOneAndDeleteOptions() {
    }


    /**
     * @return FindOneAndDeleteOptions
     */
    public static FindOneAndDeleteOptions create() {
        return new FindOneAndDeleteOptions();
    }
}
