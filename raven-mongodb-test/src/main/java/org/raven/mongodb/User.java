package org.raven.mongodb;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.raven.commons.data.AutoIncr;
import org.raven.commons.data.Deletable;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.Versioned;
import org.raven.commons.data.annotation.Contract;
import org.raven.mongodb.annotation.EntityListeners;
import org.raven.mongodb.interceptors.DeletableInterceptor;

import java.util.Date;

@Contract(formatType = MemberFormatType.PascalCase)
@FieldNameConstants
@Getter
@Setter
@EntityListeners({DeletableInterceptor.class})
public class User implements AutoIncr<Long>, Deletable, Versioned<Long> {
    @BsonId()
    private Long id;

    private String name;

    private int age;

    private Long version = 0L;

    @BsonIgnore
    private Status status;

    private Boolean deleted;

    private Date createDate;

    private Mall mall;

    public User() {
        status = Status.Normal;
        createDate = new Date();
    }
}