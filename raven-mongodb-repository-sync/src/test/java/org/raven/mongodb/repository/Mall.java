package org.raven.mongodb.repository;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.raven.commons.data.Entity;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.math.BigDecimal;

@Contract(formatType = MemberFormatType.PascalCase)
@FieldNameConstants
public class Mall implements Entity<String> {

    @BsonId
    private String id;

    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
