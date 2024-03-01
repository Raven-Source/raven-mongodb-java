package org.raven.mongodb.repository;

import com.mongodb.ReadPreference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author by yanfeng
 * date 2021/9/22 21:17
 */
@Data
@Accessors(fluent = true)
public abstract class AbstractFindOptions implements Options {

    private ReadPreference readPreference;
    private Bson hint;
    private Bson filter;
}
