package com.application.mapper;

import com.application.domain.ChildUser;
import com.application.domain.User;
import com.application.service.dto.UserDTO1;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;
import java.util.Map;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/18   18:12
 */
public interface UserMapper extends SysBaseMapper<User> {

    //@SqlParser(filter = true)// 单独使用此注解就可以过滤多租户条件查询，TODO 同时也会对动态表名解析有影响,true 表示不会进行替换
    @Select("select * from sys_user ${ew.customSqlSegment}")
    public List<User> findUsers(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    // 使用自定义SQL 注入器
    //public int deleteAll(); 现已放在公用的mapper中,SysBaseMapper

    @Select("select max(age) maxAge,min(age) min,avg(age) avg from sys_user group by email")
    public List<UserDTO1> findUsersGroupBy();

    @Results(id = "userMap", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name")
    })
    @Select("select id,name from sys_user where id = #{id}")
    public User findById(Long id);
    
        // 使用自定义SQL 注入器
    //public int deleteAll(); 现已放在公用的mapper中,SysBaseMapper

    @Select("select * from sys_user where id in (${ids})")
    public List<User> findUserByBatch(@Param("ids") String ids);

    // 不需要参数注解
    @Select("select * from sys_user where name = #{name} and id = #{id} ")
    public User findUserByNoParams(Long id, String name);

    // 映射结果处理,但是SQL查询不是很友好,但是处理好的话还是很好优化,常用语分组分页等等
    @Select("select * from sys_user where name = #{name} and id = #{id} ")
    @ConstructorArgs(value = {
            @Arg(column = "id", javaType = Long.class, id = true),
            @Arg(column = "name", javaType = String.class),
            @Arg(column = "age", javaType = Integer.class),
            @Arg(column = "email", javaType = String.class)
    })
    public UserDTO1 findUserByConstructArg(Long id, String name);


    // 主动设置主键值,即时当前表的主键为自增依然可以设置,但是@SelectKey 会使@Options 失效
    // 其中 before=true 表示先查询再插入值(使用于oracle),反之则先插入再查询(适用于mysql)
    // statement 的返回值类型由resultType 决定
    // H2 函数 identity() ,mysql last_insert_id()
    @Insert("insert into sys_user(id,name,age,email) values(#{id},#{name},#{age},#{email})")
    @SelectKey(statement = "select identity()", keyProperty = "id", before = false, resultType = Long.class)
    public Long InsertUser(User user);

    // 主动设置主键值,即时当前表的主键为自增依然可以设置,但是@SelectKey 会使@Options 失效
    // 其中 before=true 表示先查询再插入值(使用于oracle),反之则先插入再查询(适用于mysql)
    // statement 的返回值类型由resultType 决定
    // H2 函数 identity() ,mysql last_insert_id()
    @Insert("insert into sys_user(id,name,age,email) values(#{user.id},#{user.name},#{user.age},#{user.email})")
    @SelectKey(statement = "select identity()", keyProperty = "user.id", before = false, resultType = Long.class)
    public Long InsertUserByParam(@Param("user") User user);


    // 根据结果集返回不同的实体接收
    @Select("SELECT id, name, age FROM sys_user ORDER BY id")
    @TypeDiscriminator(javaType = Integer.class, column = "age", cases = {
            @Case(value = "18", type = User.class),
            @Case(value = "20", type = ChildUser.class),
            @Case(value = "24", type = ChildUser.class,
                    constructArgs = {
                            @Arg(id = true, column = "id", javaType = Long.class),
                            @Arg(column = "name", javaType = String.class),
                            @Arg(column = "age", javaType = Integer.class)})
    })
    public List<User> selectAll();

    // 处理不同的结果集
    @Select("SELECT id, name, age FROM sys_user ORDER BY id")
    @ResultType(User.class)
    public void selectAllByType(ResultHandler<User> handler);


    // 使用缓存,并且新增一条刷新一次缓存,同时获取数据插入后的主键值,并指定自增属性,设置插入超时
    @Insert("insert into sys_user(name,age,email) values(#{user.name},#{user.age},#{user.email})")
    @Options(useCache = true, flushCache = Options.FlushCachePolicy.TRUE,
            useGeneratedKeys = true, keyProperty = "id", keyColumn = "id", timeout = 10000)
    public Long InsertUserByOptions(@Param("user") User user);

    // 使用缓存,查询时不刷新缓存,同时设置缓存时间为10秒
    @Select("SELECT id, name, age FROM sys_user ORDER BY id")
    @Options(useCache = true, flushCache = Options.FlushCachePolicy.FALSE, timeout = 10000)
    public List<User> selectUserByOptions();

    // 测试失败
    @Flush
    List<BatchResult> flush();

    @Select("select id,name,age,email from sys_user")
    public List<User> selectByAutoConstructor();

    @Select("SELECT id,name FROM sys_user")
    @MapKey("id")
    public Map<Long, User> selectMap();

/*    @ResultType(User.class)
    @Select("SELECT id, name FROM sys_user WHERE name LIKE #{name} || '%' ORDER BY id")
    public User findByMap(Long id, ResultHandler<User> handler);*/
}
