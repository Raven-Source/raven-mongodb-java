package org.raven.mongodb.repository;

import com.mongodb.WriteConcern;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * date 2021/9/22 16:56
 */
@Data
@Accessors(fluent = true)
public class UpdateOptions implements Options {
    private Bson filter;
    private Bson update;
    private boolean upsert;
    private Bson hint;
    private WriteConcern writeConcern;

    /**
     *
     */
    public UpdateOptions() {
    }


    /**
     * @return
     */
    public static UpdateOptions Empty() {
        return new UpdateOptions();
    }
}
