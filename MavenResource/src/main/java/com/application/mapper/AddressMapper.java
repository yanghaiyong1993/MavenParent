package com.application.mapper;

import com.application.domain.Address;
import com.application.domain.User;
import com.application.service.dto.AddressDTO1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/18   18:12
 */
public interface AddressMapper extends BaseMapper<Address> {

    /**
     * 如果findById 没有前缀则默认使用当前Mapper也就是 AddressMapper.findById
     *
     * @param id
     * @return
     */
    @Arg(column = "id", javaType = Long.class, id = true)
    @Arg(column = "user_id", javaType = User.class, select = "com.application.mapper.UserMapper.findById")
    @Select("select * from sys_address where id = #{id}")
    public Address selectByArg(Long id);


    @ConstructorArgs(value = {
            @Arg(column = "id", javaType = Long.class),
            @Arg(column = "user_id", javaType = User.class, select = "com.application.mapper.UserMapper.findById")
    })
    @Select("select * from sys_address where id = #{id}")
    public Address selectByConstructArg(Long id);


    @ConstructorArgs(value = {
            @Arg(column = "id", javaType = Long.class, id = true),
            @Arg(column = "user_id", javaType = Long.class),
            @Arg(column = "user_id", javaType = User.class,
                    select = "com.application.mapper.UserMapper.findById", columnPrefix = "U_")
    })
    @Select("select S.*,U.id U_id,U.name U_name from sys_address as S left join sys_user as U on S.user_id = U.id where S.id = #{id}")
    public Address selectByLeftJoin(Long id);

    @ConstructorArgs(value = {
            @Arg(column = "id", javaType = Long.class, id = true),
            @Arg(column = "user_id", javaType = Long.class),
            @Arg(column = "user_id", javaType = User.class, columnPrefix = "U_", resultMap = "com.application.mapper.UserMapper.userMap")
    })
    @Select("select S.*,U.id U_id,U.name U_name from sys_address as S left join sys_user as U on S.user_id = U.id where S.id = #{id}")
    public Address selectByResultMap(Long id);

    @Results(id = "addressMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "name", property = "name"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_id", property = "user",
                    one = @One(select = "com.application.mapper.UserMapper.findById", fetchType = FetchType.LAZY))
    })
    @Select("select S.*,U.id U_id,U.name U_name from sys_address as S left join sys_user as U on S.user_id = U.id where S.id = #{id}")
    public Address selectByResults(Long id);

    // 映射结果处理,但是SQL查询不是很友好,但是处理好的话还是很好优化,常用语分组分页等等
    @Select("select * from sys_address where id = #{id} ")
    @ConstructorArgs(value = {
            @Arg(column = "id", javaType = Long.class, id = true),
            @Arg(column = "user_id", select = "com.application.mapper.UserMapper.selectById", javaType = User.class)
    })
    public AddressDTO1 findAddressByConstructArgDomain(Long id);


    // 使用第三方SQL 查询
    @SelectProvider(type = SqlProvider.class, method = "selectById")
    List<Address> findByProvider(@Param("id") Long id);

    public static class SqlProvider {
        public static String selectById() {
            return new SQL() {{
                SELECT("A.id,A.user_id");
                FROM("sys_address A");
                /*FROM("sys_user");*/
                LEFT_OUTER_JOIN("sys_user U on A.user_id = U.id");
                WHERE("A.id = #{id}");
                WHERE("U.age < 18");
                OR();
                WHERE("U.name = 'Jone' ");
                GROUP_BY("A.id,A.user_id");
                HAVING("A.id > 0");
                OR();
                HAVING("A.id is not null");
                OFFSET(0);
                LIMIT(10);
            }}.toString();
        }
    }
}
