package com.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/8/23   11:03
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChildUser {
    private Long id;
    private String name;
    private Integer age;
}
