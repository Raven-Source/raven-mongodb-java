package org.raven.mongodb.repository;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.raven.commons.data.AutoIncr;
import org.raven.commons.data.Deletable;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.util.Date;

@Contract(formatType = MemberFormatType.PascalCase)
@FieldNameConstants
public final class User implements AutoIncr<Long>, Deletable {
    @BsonId()
    private Long id;

    private String name;

    private int age;

    @BsonIgnore
    private Status status;

    private boolean del;

    private Date createDate;

    private Mall mall;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean isDel() {
        return del;
    }

    @Override
    public void setDel(boolean del) {
        this.del = del;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Mall getMall() {
        return mall;
    }

    public void setMall(Mall mall) {
        this.mall = mall;
    }

    public User(){
        status = Status.Normal;
        createDate = new Date();
    }
}