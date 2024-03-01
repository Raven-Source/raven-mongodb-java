package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * date 2021.09.23 00:16
 */
@Data
@Accessors(fluent = true)
public class DeleteOptions implements Options {

    private Bson filter;
    private Bson hint;
    private WriteConcern writeConcern;

    /**
     *
     */
    public DeleteOptions() {
    }


    /**
     * @return Empty
     */
    public static DeleteOptions Empty() {
        return new DeleteOptions();
    }
}
