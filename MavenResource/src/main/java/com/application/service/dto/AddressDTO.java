package com.application.service.dto;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import javax.jdo.annotations.PersistenceCapable;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/19   11:30
 */
@Data
@PersistenceCapable
@QueryEntity
public class AddressDTO {
    private Long id;
    private String name;

    @QueryProjection
    public AddressDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
