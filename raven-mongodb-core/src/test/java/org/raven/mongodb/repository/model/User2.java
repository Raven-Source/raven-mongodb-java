package org.raven.mongodb.repository.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.raven.commons.data.AutoIncr;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.time.LocalDateTime;

@Contract(formatType = MemberFormatType.PascalCase)
@Getter
@Setter
public class User2 implements AutoIncr<Long> {
    @BsonId()
    private Long id;

    private String name;

    private LocalDateTime createTime;

    public User2() {
        id = 0L;
    }

}
