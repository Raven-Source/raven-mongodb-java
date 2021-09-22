package org.raven.mongodb.repository;

import org.bson.codecs.pojo.annotations.BsonId;
import org.raven.commons.data.AutoIncr;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.time.LocalDateTime;

@Contract(formatType = MemberFormatType.PascalCase)
public class User2 implements AutoIncr<Long> {
    @BsonId()
    private Long id;

    private String name;

    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User2() {
        id = 0L;
    }

}
