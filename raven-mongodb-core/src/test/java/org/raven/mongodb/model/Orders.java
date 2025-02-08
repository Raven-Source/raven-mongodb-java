package org.raven.mongodb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.raven.commons.data.AutoIncr;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.math.BigDecimal;

@FieldNameConstants
@Getter
@Setter
public class Orders implements AutoIncr<Long> {

    @BsonId()
    private Long id;

    private Long uid;

    private Long itemsId;

    private String name;

    private Boolean isPay = Boolean.FALSE;

    private BigDecimal price;

    private Status status;

}
