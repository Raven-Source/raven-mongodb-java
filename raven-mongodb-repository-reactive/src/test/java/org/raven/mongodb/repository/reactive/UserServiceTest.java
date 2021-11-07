package org.raven.mongodb.repository.reactive;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteConcern;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.raven.mongodb.repository.reactive.withobjectid.User_ObjectID;
import org.raven.mongodb.repository.reactive.withobjectid.User_ObjectIDRepository;
import org.raven.mongodb.repository.reactive.withstringid.User_StringID;
import org.raven.mongodb.repository.reactive.withstringid.User_StringIDRepository;
import reactor.core.publisher.Mono;

import java.util.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {
    private static List<String> abcList = new ArrayList<String>();

    static User_StringIDRepository usRep = null;
    static User_ObjectIDRepository uoRep = null;

    @BeforeClass
    public static void init() {

        usRep = new User_StringIDRepository();
        uoRep = new User_ObjectIDRepository();

        Mono.from(usRep.getDatabase().getCollection(usRep.getCollectionName()).drop()).block();
        Mono.from(uoRep.getDatabase().getCollection(usRep.getCollectionName()).drop()).block();

        //#region 初始化abcList
        abcList.add("a");
        abcList.add("b");
        abcList.add("c");
        abcList.add("d");
        abcList.add("e");
        abcList.add("f");
        abcList.add("g");
        abcList.add("h");
        abcList.add("i");
        abcList.add("j");
        abcList.add("k");
        abcList.add("l");
        abcList.add("m");
        abcList.add("n");
        abcList.add("o");
        abcList.add("p");
        abcList.add("q");
        abcList.add("r");
        abcList.add("s");
        abcList.add("t");
        abcList.add("u");
        abcList.add("v");
        abcList.add("w");
        abcList.add("x");
        abcList.add("y");
        abcList.add("z");
        //#endregion
    }

    //#region 获得Int类型随机数

    /**
     * 获得Int类型随机数
     *
     * @param min 最小边界值（包含）
     * @param max 最大边界值（不包含）
     * @return
     */
    public int GetRandomInt(int min, int max) {
        int intBounded = min + ((int) (new Random().nextFloat() * (max - min)));
        return intBounded;
    }
    //#endregion

    //#region 获取User_StringID实体

    /**
     * 获取User_StringID实体
     *
     * @param id        主键
     * @param name      姓名
     * @param friendSet 朋友姓名集合
     * @param classMap  所选科目及分数字典
     * @return
     */
    private User_StringID GetUS(String id, String name, HashSet<String> friendSet, HashMap<String, Double> classMap) {
        User_StringID us = new User_StringID();
        us.setId(id);
        us.setName(name);
        if (friendSet == null) {
            friendSet = new HashSet<>();
            friendSet.add("jame");
            friendSet.add("bob");
            friendSet.add("1");
        }
        us.setFriendSet(friendSet);
        if (classMap == null) {
            classMap = new HashMap<>();
            classMap.put("语文", 100D);
            classMap.put("数学", 99D);
            classMap.put("英语", 98D);
        }
        us.setClassMap(classMap);
        return us;
    }

    /**
     * 获取User_StringID实体
     *
     * @param id   主键
     * @param name 姓名
     * @return
     */
    private User_StringID GetUS(String id, String name) {
        User_StringID us = new User_StringID();
        us.setId(id);
        us.setName(name);
        return us;
    }
    //#endregion

    //#region 获取User_ObjectID实体

    /**
     * 获取User_StringID实体
     *
     * @param name 姓名
     * @return
     */
    private User_ObjectID GetUO(String name) {
        User_ObjectID us = new User_ObjectID();
        us.setName(name);
        return us;
    }
    //#endregion

    //#region StringID测试

    /**
     * 测试类别描述如下：
     * 1：新增String主键的数据是否正常
     * 2：新增String主键的特殊列数据是否正常（HashSet<String>、HashMap<String,Double>）
     * 3：第一次新增成功后，再新增同样的数据会不会报错
     * 4：批量新增随机条数的数据（5-1000条）
     *
     * @throws MongoException
     */
    @Test(expected = MongoWriteException.class)
    public void a01Insert() throws MongoException {
        //#region InsertBatch
        //循环次数
        int forCount = GetRandomInt(50, 1000);
        ArrayList<User_StringID> usList = new ArrayList<>();
        for (int i = 0; i < forCount; i++) {
            int random1 = GetRandomInt(0, 26);
            int random2 = GetRandomInt(0, 26);
            //String id = abcList.get(random1) + "_" + abcList.get(random2) + "_" + i;
            String id = UUID.randomUUID().toString();
            User_StringID us = null;
            if (i % 2 == 0) {
                us = GetUS(id, "RandomMan1");
            } else {
                us = GetUS(id, "RandomMan2", null, null);
            }
            usList.add(us);
        }
        usRep.insertBatch(usList).block();
        //#endregion

        //#region Insert
        User_StringID us2 = GetUS("def_456", "aa", null, null);
        usRep.insert(us2).block();
        Assert.assertEquals(us2.getId(), "def_456");
        Assert.assertEquals(us2.getName(), "aa");

        User_StringID us3 = GetUS("ghi_789", "bb", null, null);
        usRep.insert(us3).block();
        Assert.assertEquals(us3.getId(), "ghi_789");

        User_StringID us4 = GetUS("jkl_101112", "cc", null, null);
        usRep.insert(us4).block();
        Assert.assertEquals(us4.getId(), "jkl_101112");

        User_StringID us5 = GetUS("mno_131415", "d", null, null);
        usRep.insert(us5).block();
        Assert.assertEquals(us5.getId(), "mno_131415");

        User_StringID us1 = GetUS("abc_123", "aa");
        usRep.insert(us1, WriteConcern.ACKNOWLEDGED).block();
        Assert.assertEquals(us1.getId(), "abc_123");

        User_StringID us11 = GetUS("abc_123", "aa");
        usRep.insert(us11, WriteConcern.ACKNOWLEDGED).block();
        Assert.assertEquals(us11.getId(), "abc_123");
        //#endregion
    }

    /**
     * 测试类别描述如下：
     * 1：获取存在此主键的数据是否正常
     * 2：获取不存在此主键的数据是否正常
     *
     * @throws MongoException
     */
    @Test
    public void a02Get() throws MongoException {
        String id = "abc_123";//有
        User_StringID us = usRep.get(id).block().orElse(null);
        Assert.assertEquals(us.getId(), id);

        String id2 = "a1234";//没有
        User_StringID us2 = usRep.get(id2).block().orElse(null);
        Assert.assertNull(us2);
    }

    /**
     * 测试类别描述如下：
     * 1：获取集合是否正常
     * 2：获取集合、且指定需要的列、排序、分页是否正常
     *
     * @throws MongoException
     */
    @Test
    public void a03GetList() throws MongoException {
        String id = "abc_123";//有

        Bson filter = Filters.or(Filters.eq("_id", "abc_123"), Filters.eq("_id", "def_456"));
        List<User_StringID> usList1 = usRep.getList(filter).block();
        Assert.assertNotNull(usList1);
        Assert.assertTrue(usList1.size() >= 1);

        List<String> includeFields = new ArrayList<>();
        includeFields.add("_id");
        includeFields.add("ClassMap");
        includeFields.add("Name");
        Bson sort = Sorts.descending("Name");
        List<User_StringID> usList2 = usRep.getList(filter, includeFields, sort, 100, 1).block();
        Assert.assertNotNull(usList2);
        Assert.assertTrue(usList2.size() >= 0);
        Assert.assertNotNull(usList2.get(0).getName());
    }

    /**
     * 测试类别描述如下：
     * 1：修改数据库中的数据是否正常（指定列）
     * 2：修改数据库中的数据是否正常（修改整个实体）
     * 3：修改数据库中不存在的数据是否正常
     * 4：批量修改数据库中的数据是否正常（一条存在，一条不存在）
     *
     * @throws MongoException
     */
    @Test
    public void a04Update() throws MongoException {
        String id = "abc_123";//有
        UpdateResult updateResult1 = usRep.updateOne(Filters.eq("_id", id), Updates.set("Name", "Update_OK"), false, WriteConcern.ACKNOWLEDGED).block();
        Assert.assertEquals(updateResult1.getMatchedCount(), 1);
        Assert.assertTrue(updateResult1.getModifiedCount() <= 1);

        User_StringID us = GetUS(id, "UUU");
        UpdateResult updateResult2 = usRep.updateOne(Filters.eq("_id", id), us, false, WriteConcern.ACKNOWLEDGED).block();
        Assert.assertEquals(updateResult2.getMatchedCount(), 1);
        Assert.assertEquals(updateResult2.getModifiedCount(), 1);

        String id2 = "sdsdfsd21f3d123123";//没有
        UpdateResult updateResult3 = usRep.updateOne(Filters.eq("_id", id2), Updates.set("Name", "Update_OK"), true, WriteConcern.ACKNOWLEDGED).block();

        Bson updateManyFilter = Filters.or(Filters.eq("_id", id), Filters.eq("_id", id2));
        UpdateResult updateResult4 = usRep.updateMany(updateManyFilter, Updates.set("Name", "Update_OK"), WriteConcern.ACKNOWLEDGED).block();
        Assert.assertTrue(updateResult4.getMatchedCount() >= 1);
        Assert.assertTrue(updateResult4.getModifiedCount() <= 1);
    }

    /**
     * 测试类别描述如下：
     * 1：修改数据库中有的数据的某一列（有排序）
     * 2：修改数据库中没有的一条数据
     * 3：删除数据库中没有的一条数据
     * 4：删除数据库中有的一条数据
     *
     * @throws MongoException
     */
    @Test
    public void a05FindOneAnd() throws MongoException {
        String id = "abc_123";//有
        String id2 = "sdsdfsd21f3d";//没有
        String id3 = "def_456";//有（删除测试）

        User_StringID us = usRep.findOneAndUpdate(Filters.eq("_id", id), Updates.set("Name", "FOU_OK"), false, Sorts.descending("_id")).block();
        Assert.assertNotNull(us);
        Assert.assertEquals(us.getId(), id);
        //Assert.assertEquals(us.getName(),"FOU_OK");

        User_StringID us2 = usRep.findOneAndUpdate(Filters.eq("_id", id2), Updates.set("Name", "FOU_OK"), false, Sorts.descending("_id")).block();
        Assert.assertNull(us2);

        User_StringID us3 = usRep.findOneAndDelete(Filters.eq("_id", id2)).block();
        Assert.assertNull(us3);

        User_StringID us4 = usRep.findOneAndDelete(Filters.eq("_id", id3)).block();
        Assert.assertNotNull(us4);
        Assert.assertEquals(us4.getId(), id3);
    }

    /**
     * 测试类别描述如下：
     * 1：删除数据库中有的一条数据
     * 2：删除数据库中没有的一条数据（WriteConcern.ACKNOWLEDGED）
     * 3：批量删除数据库中的数据（只有一条有，其它都没有）
     *
     * @throws MongoException
     */
    @Test
    public void a06Delete() throws MongoException {
        String id = "ghi_789";//有
        String id2 = "12321dsfdf";//没有
        String id3 = "mno_131415";//有
        DeleteResult drl1 = usRep.deleteOne(Filters.eq("_id", id)).block();

        DeleteResult drl2 = usRep.deleteOne(Filters.eq("_id", id2), WriteConcern.ACKNOWLEDGED).block();
        Assert.assertNotNull(drl2);
        Assert.assertEquals(drl2.getDeletedCount(), 0);

        Bson filter = Filters.and(Filters.eq("_id", id), Filters.eq("_id", id2), Filters.eq("_id", id3));
        DeleteResult drl3 = usRep.deleteMany(filter).block();
    }
    //#endregion

    //#region ObjectID测试

    /**
     * 测试类别描述如下：
     * 1：新增ObjectID主键的数据是否正常
     * 2：批量新增随机条数的数据（5-1000条）
     *
     * @throws MongoException
     */
    @Test()
    public void a07InsertObjectID() throws MongoException {
        //#region Insert
        User_ObjectID us1 = GetUO("bb");
        uoRep.insert(us1, WriteConcern.ACKNOWLEDGED).block();
        //Assert.assertNull(us1.getId());
        Assert.assertEquals(us1.getName(), "bb");

        User_ObjectID us2 = GetUO("bb");
        uoRep.insert(us2).block();
        //Assert.assertNull(us2.getId());
        Assert.assertEquals(us2.getName(), "bb");

        //#endregion

        //#region InsertBatch
        //循环次数
        int forCount = GetRandomInt(5, 1000);
        ArrayList<User_ObjectID> usList = new ArrayList<>();
        for (int i = 0; i < forCount; i++) {
            int random1 = GetRandomInt(0, 26);
            int random2 = GetRandomInt(0, 26);
            String name = abcList.get(random1) + "_" + abcList.get(random2) + "_" + i;
            User_ObjectID us = GetUO(name);
            usList.add(us);
        }
        uoRep.insertBatch(usList).block();
        try {
            for (User_ObjectID userValue : usList) {
                //Assert.assertNotNull(userValue.getId());
            }
        } catch (Exception ex) {
            String s = "";
        }
        String a = "";
        //#endregion
    }

    /**
     * 测试类别描述如下：
     * 1：根据ID获取一条对应数据(MongoDBRepository.Get(id))
     * 2：根据ID获取一条对应数据（MongoDBRepository.Get(Filters.eq("_id",""))）
     * 3：根据name获取对应的已经有的集合数据
     * 4：根据name调用count获取在表中匹配到的count
     * 5. 根据name获取没有的数据
     *
     * @throws MongoException
     */
    @Test
    public void a08GetObjectID() throws MongoException {
        User_ObjectID getUo = uoRep.get(Filters.eq("Name", "bb")).block().orElse(null);
        Assert.assertNotNull(getUo);

        User_ObjectID uo1 = uoRep.get(new ObjectId(getUo.getId().toString())).block().orElse(null); //取不到数据
        User_ObjectID uo3 = uoRep.get(Filters.eq("_id", "ObjectId(\"" + getUo.getId().toString() + "\")")).block().orElse(null); //取不到数据
        User_ObjectID uo5 = uoRep.get(Filters.eq("_id", getUo.getId())).block().orElse(null);//有数据
        Assert.assertNotNull(uo1);
        Assert.assertNotNull(uo1.getId());

        //根据ID获取一条实体数据
        ObjectId id3 = new ObjectId(getUo.getId().toString());
        User_ObjectID uoModel3 = uoRep.get(id3).block().orElse(null);

        List<User_ObjectID> uoList3 = uoRep.getList(Filters.eq("Name", "bb")).block();

        //新增一条实体数据
        User_ObjectID us2 = GetUO("啦啦啦啦11");
        uoRep.insert(us2).block();


        User_ObjectID uoModel = GetUO("啦啦啦啦11");
        Bson filterUpdate = Filters.eq("Name", "啦啦啦啦13");
        long count2 = uoRep.count(filterUpdate).block();
        UpdateResult update = uoRep.updateOne(filterUpdate, uoModel, true, null).block();
        //BsonValue bv = update.getUpsertedId();

        Bson filter = Filters.eq("Name", "bb");
        long count = uoRep.count(filter).block();

        //自动生成ObjectId
        ObjectId oi = new ObjectId();

        List<User_ObjectID> uoList = uoRep.getList(filter).block();
        Assert.assertNotNull(uoList);
        Assert.assertTrue(uoList.size() >= 1);
        Assert.assertNotNull(uoList.get(0).getId());
    }

    /**
     * 测试类别描述如下：
     * 1：批量删除数据是否正常
     *
     * @throws MongoException
     */
    @Test
    public void a09DeleteObjectID() throws MongoException {
        /*Bson filterList = null;
        for(String objID : ObjectIDList){
            Bson filter = Filters.eq("_id",objID);
            if (filterList == null) {
                filterList = filter;
            }
            else{
                filterList = Filters.and(filterList,filter);
            }
        }
        DeleteResult drl = uoRep.deleteMany(filterList,WriteConcern.ACKNOWLEDGED);
        Assert.assertNotNull(drl);
        Assert.assertTrue(drl.getDeletedCount() == ObjectIDList.size());*/
    }

    /**
     * 测试类别描述如下：
     * 1：根据ID修改一条数据是否正常（指定修改列）
     * 2：根据实际没有的ID修改一条数据是否正常（用TEntity，IsUpsert:true）
     *
     * @throws MongoException
     */
    @Test
    public void a10UpdateObjectID() throws MongoException {
        User_ObjectID getUo = uoRep.get(Filters.eq("Name", "bb")).block().orElse(null);
        Assert.assertNotNull(getUo);
        UpdateResult updateResult1 = uoRep.updateOne(Filters.eq("_id", getUo.getId()), Updates.set("Name", "Update_OK"), false, WriteConcern.ACKNOWLEDGED).block();
        Assert.assertEquals(updateResult1.getMatchedCount(), 1);
        Assert.assertEquals(updateResult1.getModifiedCount(), 1);

        User_ObjectID us = GetUO("UpOK");
        UpdateResult updateResult2 = uoRep.updateOne(Filters.eq("_id", getUo.getId()), us, true, WriteConcern.ACKNOWLEDGED).block();
        Assert.assertTrue(updateResult2.getMatchedCount() >= 0);
        Assert.assertTrue(updateResult2.getModifiedCount() >= 0);
    }
    //#endregion
}