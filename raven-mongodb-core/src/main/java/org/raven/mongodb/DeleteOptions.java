package org.raven.mongodb;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author yi.liang
 * date 2021.09.23 00:16
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(fluent = true)
public class DeleteOptions extends AbstractModifyOptions<DeleteOptions> implements CommandOptions {

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
