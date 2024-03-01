package org.raven.mongodb.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.raven.commons.data.Entity;
import org.raven.commons.data.MemberFormatType;
import org.raven.commons.data.annotation.Contract;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * User实体-自拼接String主键i
 */
@Contract(formatType = MemberFormatType.PascalCase)
@Getter
@Setter
public class User_StringID implements Entity<String> {

    //主键
    @BsonId
    private String id;

    //姓名
    private String name;

    //年龄
    private int age;

    //创建时间
    private Date createTime;

    //朋友名字（StringJ集合）
    private HashSet<String> friendSet;

    //所选课程及得分（HashMap集合）
    private HashMap<String, Double> classMap;
    //#endregion

    public User_StringID() {
        this.createTime = new Date();
    }

}