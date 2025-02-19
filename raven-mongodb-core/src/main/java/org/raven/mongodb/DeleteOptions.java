package org.raven.mongodb;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yi.liang
 * date 2021.09.23 00:16
 */
@Data
@Accessors(fluent = true)
public class DeleteOptions extends AbstractModifyOptions<DeleteOptions> implements Options {

    /**
     *
     */
    public DeleteOptions() {
    }


    /**
     * @return DeleteOptions
     */
    public static DeleteOptions create() {
        return new DeleteOptions();
    }
}
