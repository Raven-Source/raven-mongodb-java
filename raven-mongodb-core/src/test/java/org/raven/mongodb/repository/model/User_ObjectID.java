package org.raven.mongodb.repository.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.raven.commons.data.Entity;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.util.Date;

/**
 * User实体-自生成的ObjectId
 */
@Contract(formatType = MemberFormatType.PascalCase)
@Getter
@Setter
public class User_ObjectID implements Entity<ObjectId> {

    //主键
    @BsonId
    private ObjectId id;

    //姓名
    private String name;

    /*private String Address;

    public String getAddress() {
        return Address;
    }
    public void setAddress(String address){this.Address = address;}*/

    //年龄
    private int age;

    //创建时间
    private Date createTime;

    public User_ObjectID() {
        this.createTime = new Date();
        //this.id = new ObjectId();
        //this.Address = "add哈哈";
    }
}
