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
public class ExistsOptions {
    private ReadPreference readPreference;
    private Bson hint;
    private Bson filter;

    public ExistsOptions() {
    }

    /**
     * @return
     */
    public static ExistsOptions Empty() {
        return new ExistsOptions();
    }
}
