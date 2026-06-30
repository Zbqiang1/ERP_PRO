package com.erp.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.erp.common.result.Result;
import com.erp.modules.system.entity.Role;
import com.erp.modules.system.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 分页查询角色
     *
     * @param page    当前页码
     * @param size    每页大小
     * @param keyword 关键字
     * @return 分页结果
     */
    @GetMapping
    public Result<IPage<Role>> list(@RequestParam(defaultValue = "1") Long page,
                                     @RequestParam(defaultValue = "10") Long size,
                                     @RequestParam(required = false) String keyword) {
        IPage<Role> rolePage = roleService.pageRole(page, size, keyword);
        return Result.ok(rolePage);
    }

    /**
     * 获取所有启用角色
     *
     * @return 角色列表
     */
    @GetMapping("/all")
    public Result<List<Role>> listAll() {
        List<Role> roles = roleService.listAllEnabled();
        return Result.ok(roles);
    }

    /**
     * 根据ID获取角色详情
     *
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return Result.ok(role);
    }

    /**
     * 新增角色
     *
     * @param role 角色信息
     * @return 操作结果
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody Role role) {
        boolean result = roleService.addRole(role);
        return Result.ok(result);
    }

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        boolean result = roleService.updateRole(role);
        return Result.ok(result);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = roleService.deleteRoleById(id);
        return Result.ok(result);
    }

    /**
     * 为角色分配菜单权限
     *
     * @param id      角色ID
     * @param menuIds 菜单ID列表
     * @return 操作结果
     */
    @PutMapping("/{id}/menus")
    public Result<Boolean> assignMenus(@PathVariable Long id, @RequestBody List<Long> menuIds) {
        boolean result = roleService.assignMenus(id, menuIds);
        return Result.ok(result);
    }
}
