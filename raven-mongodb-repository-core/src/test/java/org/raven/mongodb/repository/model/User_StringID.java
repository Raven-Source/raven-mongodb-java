package org.raven.mongodb.repository.model;

import org.bson.codecs.pojo.annotations.BsonId;
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

    //#region 主键
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    //#endregion

    //#region Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //#endregion

    //#region CreateTime
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    //#endregion

    //#region FriendSet
    public HashSet<String> getFriendSet() {
        return friendSet;
    }

    public void setFriendSet(HashSet<String> friendSet) {
        this.friendSet = friendSet;
    }
    //#endregion

    //#region ClassMap
    public HashMap<String, Double> getClassMap() {
        return classMap;
    }

    public void setClassMap(HashMap<String, Double> classMap) {
        this.classMap = classMap;
    }
    //#endregion

    public User_StringID() {
        this.createTime = new Date();
    }

}