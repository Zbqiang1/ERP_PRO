package com.erp.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.system.entity.Role;
import com.erp.modules.system.entity.RoleMenu;
import com.erp.modules.system.mapper.RoleMapper;
import com.erp.modules.system.mapper.RoleMenuMapper;
import com.erp.modules.system.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;

    public RoleServiceImpl(RoleMapper roleMapper, RoleMenuMapper roleMenuMapper) {
        this.roleMapper = roleMapper;
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public IPage<Role> pageRole(Long page, Long size, String keyword) {
        Page<Role> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Role::getRoleName, keyword)
                    .or()
                    .like(Role::getRoleCode, keyword);
        }
        wrapper.orderByAsc(Role::getSortOrder);
        return roleMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Role getRoleById(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRole(Role role) {
        // 检查角色编码是否已存在
        Role existRole = roleMapper.selectByRoleCode(role.getRoleCode());
        if (existRole != null) {
            throw new BusinessException("角色编码已存在");
        }
        return roleMapper.insert(role) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        Role existRole = roleMapper.selectById(role.getId());
        if (existRole == null) {
            throw new BusinessException("角色不存在");
        }
        // 如果修改了角色编码，检查是否与其他角色冲突
        if (!existRole.getRoleCode().equals(role.getRoleCode())) {
            Role codeRole = roleMapper.selectByRoleCode(role.getRoleCode());
            if (codeRole != null && !codeRole.getId().equals(role.getId())) {
                throw new BusinessException("角色编码已存在");
            }
        }
        return roleMapper.updateById(role) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleById(Long id) {
        // 删除角色菜单关联
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);
        // 逻辑删除角色
        return roleMapper.deleteById(id) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenus(Long roleId, List<Long> menuIds) {
        // 先删除原有菜单关联
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
        // 保存新的菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleMenu> roleMenus = menuIds.stream().map(menuId -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            for (RoleMenu roleMenu : roleMenus) {
                roleMenuMapper.insert(roleMenu);
            }
        }
        return true;
    }

    @Override
    public List<Role> listAllEnabled() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, 1)
                .orderByAsc(Role::getSortOrder);
        return roleMapper.selectList(wrapper);
    }
}
