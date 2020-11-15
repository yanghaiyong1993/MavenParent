package com.application.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Address is a Querydsl bean type
 */
@Data
@TableName("sys_address")
public class Address implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)// 默认是雪花算法
    private Long id;

    @Size(max = 30)
    private String name;

    private Long userId;

    @TableField(exist = false)
    private String createdBy;

    // 审计
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdDate = LocalDateTime.now(ZoneId.systemDefault());

    @TableField(exist = false)
    private String lastModifiedBy;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime lastModifiedDate = LocalDateTime.now(ZoneId.systemDefault());

    private Long manageId;

    @TableField(exist = false)
    private User user;

    public Address(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public Address(Long id, Long userId, User user) {
        this.id = id;
        this.userId = userId;
        this.user = user;
    }

    public Address(Long id) {
        this.id = id;
    }

    public Address() {
    }
}

