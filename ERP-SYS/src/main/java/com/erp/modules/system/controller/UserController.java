package com.erp.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erp.common.result.Result;
import com.erp.modules.system.dto.UserDTO;
import com.erp.modules.system.dto.UserQueryDTO;
import com.erp.modules.system.service.IUserService;
import com.erp.modules.system.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    @GetMapping
    public Result<IPage<UserVO>> list(UserQueryDTO queryDTO) {
        IPage<UserVO> page = userService.pageUser(queryDTO);
        return Result.ok(page);
    }

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.ok(userVO);
    }

    /**
     * 新增用户
     *
     * @param userDTO 用户信息
     * @return 操作结果
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody UserDTO userDTO) {
        boolean result = userService.addUser(userDTO);
        return Result.ok(result);
    }

    /**
     * 修改用户
     *
     * @param id      用户ID
     * @param userDTO 用户信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        boolean result = userService.updateUser(id, userDTO);
        return Result.ok(result);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = userService.deleteUserById(id);
        return Result.ok(result);
    }

    /**
     * 重置用户密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 操作结果
     */
    @PutMapping("/{id}/password")
    public Result<Boolean> resetPassword(@PathVariable Long id, @RequestParam String password) {
        boolean result = userService.resetPassword(id, password);
        return Result.ok(result);
    }

    /**
     * 为用户分配角色
     *
     * @param id      用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @PutMapping("/{id}/roles")
    public Result<Boolean> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        boolean result = userService.assignRoles(String.valueOf(id), roleIds);
        return Result.ok(result);
    }
}
