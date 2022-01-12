package org.raven.mongodb.repository;

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
public class FindOptions extends AbstractFindOptions {

    private List<String> includeFields;
    private Bson sort;
    private int limit;
    private int skip;

    /**
     *
     */
    public FindOptions() {
    }


    /**
     * @return Empty
     */
    public static FindOptions Empty() {
        return new FindOptions();
    }

}
