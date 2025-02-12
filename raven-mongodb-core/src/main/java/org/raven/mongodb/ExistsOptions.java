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
public class ExistsOptions extends AbstractFindOptions<ExistsOptions> {

    public ExistsOptions() {
    }

    /**
     * @return ExistsOptions
     */
    public static ExistsOptions empty() {
        return new ExistsOptions();
    }
}
