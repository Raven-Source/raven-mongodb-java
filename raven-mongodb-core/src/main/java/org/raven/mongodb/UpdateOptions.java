package org.raven.mongodb;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * date 2021/9/22 16:56
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(fluent = true)
public class UpdateOptions extends AbstractModifyOptions<UpdateOptions> implements CommandOptions {
    private Bson update;
    private boolean upsert;

    public UpdateOptions() {
    }

    /**
     * @return UpdateOptions
     */
    public static UpdateOptions create() {
        return new UpdateOptions();
    }
}
