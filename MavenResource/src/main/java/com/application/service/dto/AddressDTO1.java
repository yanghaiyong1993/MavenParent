package com.application.service.dto;

import com.application.domain.User;
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
public class AddressDTO1 {
    private Long id;
    private Long userId;
    private User user;

    @QueryProjection
    public AddressDTO1(Long id, User user) {
        this.id = id;
        this.user = user;
    }
}
