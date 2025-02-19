package org.raven.mongodb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author by yanfeng
 * date 2021/9/22 21:34
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(fluent = true)
public class FindOneAndDeleteOptions extends AbstractFilterOptions<FindOneAndDeleteOptions> implements CommandOptions {

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
