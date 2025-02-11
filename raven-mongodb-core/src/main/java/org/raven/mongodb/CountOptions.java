package org.raven.mongodb;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author yi.liang
 * @since JDK11
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
    public static CountOptions empty() {
        return new CountOptions();
    }

}
