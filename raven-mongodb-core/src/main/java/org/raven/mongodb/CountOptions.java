package org.raven.mongodb;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author yi.liang
 */
@Getter
@Setter
@ToString(callSuper = true)
@Accessors(fluent = true)
public class CountOptions extends AbstractFindOptions<CountOptions> {

    private int limit;
    private int skip;

    public CountOptions() {
    }

    /**
     * @return CountOptions
     */
    public static CountOptions create() {
        return new CountOptions();
    }

}
