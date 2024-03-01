package org.raven.mongodb.repository.model;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.raven.commons.data.Entity;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.math.BigDecimal;

@Contract(formatType = MemberFormatType.PascalCase)
@FieldNameConstants
@Getter
@Setter
public class Mall implements Entity<String> {

    @BsonId
    private String id;

    private String name;

    private Status status = Status.Normal;

}
