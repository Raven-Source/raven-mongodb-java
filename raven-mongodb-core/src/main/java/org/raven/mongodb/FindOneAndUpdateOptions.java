package org.raven.mongodb;

import com.mongodb.client.model.ReturnDocument;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.bson.conversions.Bson;

/**
 * @author yi.liang
 * date 2021/9/22 20:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(fluent = true)
public class FindOneAndUpdateOptions extends UpdateOptions {

    private Bson sort;
    private ReturnDocument returnDocument = ReturnDocument.AFTER;

    /**
     *
     */
    public FindOneAndUpdateOptions() {
    }


    /**
     * @return FindOneAndUpdateOptions
     */
    public static FindOneAndUpdateOptions empty() {
        return new FindOneAndUpdateOptions();
    }
}
