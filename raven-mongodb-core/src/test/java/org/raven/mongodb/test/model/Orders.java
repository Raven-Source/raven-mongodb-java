package org.raven.mongodb.test.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.raven.commons.data.AutoIncr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@FieldNameConstants
@Getter
@Setter
public class Orders implements AutoIncr<Long> {

    @BsonId()
    private Long id;

    private Long uid;

    private List<Integer> refs = new ArrayList<>();

    private Long itemsId;

    private String name;

    private Boolean isPay = Boolean.FALSE;

    private BigDecimal price;

    private Status status = Status.Normal;

    private Date createTime;

    private Long version;


}
