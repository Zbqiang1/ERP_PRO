package com.erp.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.system.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface IRoleService extends IService<Role> {

    /**
     * 分页查询角色
     *
     * @param page    当前页码
     * @param size    每页大小
     * @param keyword 关键字（角色名称或编码）
     * @return 分页结果
     */
    IPage<Role> pageRole(Long page, Long size, String keyword);

    /**
     * 根据ID获取角色
     *
     * @param id 角色ID
     * @return 角色实体
     */
    Role getRoleById(Long id);

    /**
     * 新增角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean addRole(Role role);

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(Role role);

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteRoleById(Long id);

    /**
     * 为角色分配菜单权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return 是否成功
     */
    boolean assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 获取所有启用角色列表
     *
     * @return 角色列表
     */
    List<Role> listAllEnabled();
}
