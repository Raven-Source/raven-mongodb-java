package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @author yi.liang
 * @since JDK11
 */
@Data
@Accessors(fluent = true)
public class FindOptions {

    private ReadPreference readPreference;
    private Bson hint;
    private Bson filter;
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
     * @return
     */
    public static FindOptions Empty() {
        return new FindOptions();
    }

}
