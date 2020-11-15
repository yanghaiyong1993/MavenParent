package com.application.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User is a Querydsl bean type
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> implements Serializable {

    @NotNull
    @TableId(type = IdType.ASSIGN_ID)// 默认是雪花算法
    private Long id;

    private Integer age;

    @Size(max = 50)
    private String email;

    @Size(max = 30)
    @TableField(condition = SqlCondition.LIKE)//%s&lt;&gt;#{%s} 或者自定义如此也可
    private String name;

    @TableLogic
    @TableField(select = false)// 查询不显示此字段
    private Integer deleted;

    @Version
    @TableField(select = false)
    private Long version;

    private Long manageId;
}

