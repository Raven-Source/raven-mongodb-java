package org.raven.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author yi.liang
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
