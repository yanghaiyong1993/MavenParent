package com.application.web.rest;

import com.application.domain.User;
import com.application.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codahale.metrics.annotation.Timed;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/7/18   18:51
 */
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class UserResource {

    private final UserMapper userMapper;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "保存接口", notes = "保存用户")

    @PostMapping("/user")
    public ResponseEntity<User> user(@Valid @RequestBody User user) throws URISyntaxException {
        userMapper.insert(user);
        return ResponseEntity.created(new URI("/api/user/" + user.getId())).body(user);
    }


    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "更新接口", notes = "更新用户")
    @Timed
    @PutMapping("/user")
    public ResponseEntity<User> update(@Valid @RequestBody User user) throws URISyntaxException {
        userMapper.update(user, null);
        return ResponseEntity.created(new URI("/api/user/" + user.getId())).body(user);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "删除接口", notes = "删除用户")
    @Timed
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userMapper.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "查询接口", notes = "查询用户(根据id)")
    @Timed
    @GetMapping("/user/{id}")
    public ResponseEntity<User> find(@PathVariable Long id) {
        return ResponseEntity.of(Optional.of(userMapper.selectById(id)));
    }


    @ApiOperationSupport(includeParameters = {"page", "size", "sort"}, order = 5)
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @PostMapping(value = "/users")
    public ResponseEntity<Page<User>> findAllUser(
            @RequestBody(required = false) User user,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        // TODO 此处稍等处理
        return ResponseEntity.ok().body(userMapper.selectPage(new Page<>(pageable.getOffset(), pageable.getPageSize()), null));
    }


}
