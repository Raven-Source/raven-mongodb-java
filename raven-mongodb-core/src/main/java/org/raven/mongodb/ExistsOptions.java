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
public class ExistsOptions extends AbstractFindOptions<ExistsOptions> {

    public ExistsOptions() {
    }

    /**
     * @return ExistsOptions
     */
    public static ExistsOptions create() {
        return new ExistsOptions();
    }
}
