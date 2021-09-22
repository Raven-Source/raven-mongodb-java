package org.raven.mongodb.repository;

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
public class ExistsOptions extends AbstractFindOptions {

    public ExistsOptions() {
    }

    /**
     * @return
     */
    public static ExistsOptions Empty() {
        return new ExistsOptions();
    }
}
