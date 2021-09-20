package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * @since JDK11
 */
@Data
@Accessors(fluent = true)
public class CountOptions {
    private Bson filter;
    private ReadPreference readPreference;

    private Bson hint;
    private int limit;
    private int skip;

    public CountOptions() {
    }

    /**
     * @return CountOptions
     */
    public static CountOptions Empty() {
        return new CountOptions();
    }

}
