package com.application.service.dto;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.jdo.annotations.PersistenceCapable;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/19   10:54
 */
@Data
@PersistenceCapable
@QueryEntity
public class UserDTO {
    private String name;
    private Integer age;
    private Long count;

    @QueryProjection
    public UserDTO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @QueryProjection
    public UserDTO(String name, Integer age, Long count) {
        this.name = name;
        this.age = age;
        this.count = count;
    }
}
