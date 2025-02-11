package org.raven.mongodb;

import lombok.*;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @author yi.liang
 * @since JDK11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(fluent = true)
public class FindOptions extends AbstractFindOptions<FindOptions> {

    private List<String> includeFields;
    private List<String> excludeFields;
    private Bson sort;
    private int limit;
    private int skip;

    public FindOptions() {
    }


    /**
     * @return FindOptions
     */
    public static FindOptions empty() {
        return new FindOptions();
    }

}
