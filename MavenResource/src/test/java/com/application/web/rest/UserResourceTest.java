package com.application.web.rest;

import com.application.config.MPConfiguration;
import com.application.domain.Address;
import com.application.domain.User;
import com.application.mapper.AddressMapper;
import com.application.mapper.UserMapper;
import com.application.service.dto.AddressDTO1;
import com.application.service.dto.UserDTO1;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/24   21:35
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("h2")
class UserResourceTest {

    @Inject
    @Named("sqlQueryFactory")
    private SQLQueryFactory sqlQueryFactory;

    @Inject
    @Named("sqlTemplates")
    private SQLTemplates sqlTemplates;

    @Inject
    @Named("userResource")
    private UserResource userResource;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AddressMapper addressMapper;

    @Test
    void findAll() {
        /**SQLQuery<?> query = sqlQueryFactory.query();
         QUser qUser = QUser.user;
         QAddress qAddress = QAddress.address;
         List<UserDTO> fetch = query
         .select(QUserDTO.create(qUser.name, qUser.age))
         .from(qUser)
         .fetch();
         JDOSQLQuery<Void> jdosqlQuery = new JDOSQLQuery<>(null, sqlTemplates);
         List<UserDTO> list = jdosqlQuery
         .select(QUserDTO.create(qUser.name, qUser.age))
         .from(qUser)
         .leftJoin(qAddress).on(qAddress.userId.eq(qUser.id)).fetch();

         QTblUser user = QTblUser.tblUser;
         QTblAddress address = QTblAddress.tblAddress;

         List<String> fetch = sqlQueryFactory.query().select(user.name).from(user).fetch();

         List<UserDTO> dtos = sqlQueryFactory.query()
         .select(Projections.constructor(UserDTO.class, user.name, user.age, address.id.countDistinct()))
         .from(user)
         .leftJoin(address).on(address.userId.eq(user.id))
         .groupBy(user.name, user.age)
         .orderBy(user.name.asc(), user.age.asc())
         .fetch();
         User user1 = new User();
         user1.setEmail("sfdsddf");
         sqlQueryFactory
         .update(user)
         .addBatch().limit(10)
         .populate(user1)// 将会更新已存在的属性
         .where(user.name.eq("Tom")).execute();*/
        // 测试动态表名
        MPConfiguration.myTableName.set("sys_user");
        userMapper.selectById(1L);
    }

    @Test
    public void findCustomer() {
        // 自定义的查询是没有使用实体属性上的限定条件
        User user = new User();
        user.setAge(1);
        user.setName("admin");

        // 这种方式可以绕过实体上的软删除等字段
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(true, User::getAge, 1);
        List<User> users = userMapper.findUsers(lambdaQuery);
    }

    @Test
    public void saveFillCreatedDate() {
        Address address = new Address();
        address.setName("中国");
        address.setUserId(1L);
        addressMapper.insert(address);
    }

    @Test
    public void saveFillVersion() {
        User user = new User();
        user.setAge(12);
        user.setEmail("123@qq.com");
        user.setName("张悦");
        userMapper.insert(user);
    }

    @Test
    public void deleteALl() {
        int deleteAll = userMapper.deleteAll();
    }

    @Test
    public void wrapper1() {
        Wrapper<User> userWrapper = new QueryWrapper<>();
        Page<User> userPage = new Page<>(1, 2);
        // 物理分页
        Page<User> selectPage = userMapper.selectPage(userPage, userWrapper);
    }

    @Test
    public void selectByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 25);
        List<User> users = userMapper.selectByMap(map);
        System.out.println(users);
    }

    @Test
    public void selectByWrapper() {
        QueryWrapper<User> query = Wrappers.query();
        query.eq("age", 1);
        query.isNotNull("id");
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapper1() {
        QueryWrapper<User> query = Wrappers.query();
        query.like("name", "王").and(wq -> wq.lt("age", 1).or().eq("id", 1));
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapper2() {
        QueryWrapper<User> query = Wrappers.query();
        query.like("name", "王")
                .and(wq -> wq.lt("age", 40).gt("age", 2).or().eq("id", 1));
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapper3() {
        QueryWrapper<User> query = Wrappers.query();
        query.nested(wq -> wq.eq("age", 1)).or().like("name", "王");
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapper4() {
        QueryWrapper<User> query = Wrappers.query();
        query.in("age", Arrays.asList(1, 2, 3));
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapper5() {
        QueryWrapper<User> query = Wrappers.query();
        query.in("age", Arrays.asList(1, 2, 3))
                .select(User.class, info -> !info.getColumn().equals("age") && !info.getColumn().equals("id"));
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapper6() {
        // 实体作为查询条件,默认是等值查询,如果不适用等值查询，则需要在实体上进行配置
        User user = new User();
        user.setName("你好");
        user.setAge(1);

        QueryWrapper<User> query = Wrappers.query(user);
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapperAllEq() {
        // 实体作为查询条件,默认是等值查询,如果不适用等值查询，则需要在实体上进行配置
        QueryWrapper<User> query = Wrappers.query();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王");
        params.put("age", null);//如果传入null,则会进行拼接 age is null
        query.allEq(params);
        //query.allEq(params,false);// 表示忽略null
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapperAllEq1() {
        // 实体作为查询条件,默认是等值查询,如果不适用等值查询，则需要在实体上进行配置
        QueryWrapper<User> query = Wrappers.query();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王");
        params.put("age", null);//如果传入null,则会进行拼接 age is null
        query.allEq((k, v) -> !k.equals("name"), params);// 等于name就会过滤掉,不加入条件中去
        //query.allEq(params,false);// 表示忽略null
        List<User> users = userMapper.selectList(query);
        System.out.println(users);
    }

    @Test
    public void selectByWrapperMaps() {
        // 实体作为查询条件,默认是等值查询,如果不适用等值查询，则需要在实体上进行配置
        QueryWrapper<User> query = Wrappers.query();
        query
                .select("sum(age) sum", "min(age) min", "max(age) max")
                .groupBy("email")
                .having("sum(age)<{0}", 500);

        List<Map<String, Object>> selectMaps = userMapper.selectMaps(query);
        System.out.println(selectMaps);
    }

    @Test
    public void update() {
        UpdateWrapper<User> update = Wrappers.update();
        update.eq("id", 1);

        User user = new User();
        user.setAge(23);
        // 不能跳过实体上的软删除
        int row = userMapper.update(user, update);

        System.out.println(row);
    }

    @Test
    public void update1() {
        UpdateWrapper<User> wrapper = Wrappers.<User>update()
                .eq("id", 1)
                .set("age", 323);

        // 不能跳过实体上的软删除,自定义的语句可以跳过逻辑软删除
        int row = userMapper.update(null, wrapper);

        System.out.println(row);
    }

    @Test
    public void update2() {
        // AR 模式的查询返回都是新的对象
        User user = new User();
        user.setId(1L);
        user.setName("你好");
        user.updateById();
        //user.insertOrUpdate();// 现查询再更新或新增
    }

    @Test
    public void update3() {
        // 自动填充实体不要设置默认值,否则会自动更新
        // 且需要配置
        List<UserDTO1> usersGroupBy = userMapper.findUsersGroupBy();

    }

    @Test
    public void queryWrapper1() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery(User.class);
        lambdaQuery.exists(true, "select * from sys_user where id = 1");
        userMapper.selectOne(lambdaQuery);
    }

    @Test
    public void queryWrapper2() {
        User user = userMapper.findById(1L);
        /*User mapperByMap = userMapper.findByMap(1L);*/
        Map<Long, User> selectMap = userMapper.selectMap();
        Address address = addressMapper.selectByArg(1L);
        Address select = addressMapper.selectByConstructArg(1L);
        Address leftJoin = addressMapper.selectByLeftJoin(1L);
        Address resultMap = addressMapper.selectByResultMap(1L);
        Address results = addressMapper.selectByResults(1L);

        System.out.println(address);
    }

    @Test
    public void queryWrapper3() {
        AddressDTO1 addressDTO1 = addressMapper.findAddressByConstructArgDomain(1L);
        System.out.println(addressDTO1);
    }
}