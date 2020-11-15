package com.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/25   11:24
 */
public interface SysBaseMapper<T> extends BaseMapper<T> {
    public int deleteAll();

    public int insertBatchSomeColumn(List<T> list);
}
