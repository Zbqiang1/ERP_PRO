package com.erp.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.security.JwtTokenUtils;
import com.erp.modules.system.dto.LoginDTO;
import com.erp.modules.system.dto.UserDTO;
import com.erp.modules.system.dto.UserQueryDTO;
import com.erp.modules.system.entity.*;
import com.erp.modules.system.mapper.*;
import com.erp.modules.system.service.IUserService;
import com.erp.modules.system.vo.LoginVO;
import com.erp.modules.system.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final DepartmentMapper departmentMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;

    public UserServiceImpl(UserMapper userMapper, RoleMapper roleMapper,
                           UserRoleMapper userRoleMapper, DepartmentMapper departmentMapper,
                           PasswordEncoder passwordEncoder, JwtTokenUtils jwtTokenUtils) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.departmentMapper = departmentMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() == 0) {
            throw new BusinessException("该账户已被禁用，请联系管理员");
        }
        // 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        // 生成JWT令牌
        String token = jwtTokenUtils.generateToken(String.valueOf(user.getId()), user.getUsername());
        // 构建登录响应
        return LoginVO.builder()
                .token(token)
                .userId(String.valueOf(user.getId()))
                .username(user.getUsername())
                .realName(user.getRealName())
                .avatar(user.getAvatar())
                .build();
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToUserVO(user);
    }

    @Override
    public IPage<UserVO> pageUser(UserQueryDTO queryDTO) {
        Page<User> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getUsername()), User::getUsername, queryDTO.getUsername())
                .like(StringUtils.hasText(queryDTO.getRealName()), User::getRealName, queryDTO.getRealName())
                .like(StringUtils.hasText(queryDTO.getPhone()), User::getPhone, queryDTO.getPhone())
                .eq(queryDTO.getStatus() != null, User::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getDeptId() != null, User::getDeptId, queryDTO.getDeptId())
                .orderByDesc(User::getCreateTime);
        IPage<User> userPage = userMapper.selectPage(page, wrapper);
        // 转换为UserVO分页
        IPage<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::convertToUserVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        // 加密密码
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        int result = userMapper.insert(user);
        // 分配角色
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            saveUserRoles(String.valueOf(user.getId()), userDTO.getRoleIds());
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(Long id, UserDTO userDTO) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 如果修改了用户名，检查是否与其他用户冲突
        if (!user.getUsername().equals(userDTO.getUsername())) {
            User existUser = userMapper.selectByUsername(userDTO.getUsername());
            if (existUser != null) {
                throw new BusinessException("用户名已存在");
            }
        }
        BeanUtils.copyProperties(userDTO, user, "password");
        int result = userMapper.updateById(user);
        // 重新分配角色
        if (userDTO.getRoleIds() != null) {
            // 先删除原有角色关联
            LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserRole::getUserId, id);
            userRoleMapper.delete(wrapper);
            // 保存新的角色关联
            if (!userDTO.getRoleIds().isEmpty()) {
                saveUserRoles(String.valueOf(id), userDTO.getRoleIds());
            }
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserById(Long id) {
        // 删除用户角色关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
        // 逻辑删除用户
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public boolean resetPassword(Long id, String password) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(password));
        return userMapper.updateById(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(String userId, List<Long> roleIds) {
        // 先删除原有角色关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        userRoleMapper.delete(wrapper);
        // 保存新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            saveUserRoles(userId, roleIds);
        }
        return true;
    }

    /**
     * 保存用户角色关联
     */
    private void saveUserRoles(String userId, List<Long> roleIds) {
        List<UserRole> userRoles = roleIds.stream().map(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        // 使用ServiceImpl的saveBatch批量插入
        for (UserRole userRole : userRoles) {
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * 将User转换为UserVO
     */
    private UserVO convertToUserVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        // 设置部门名称
        if (user.getDeptId() != null) {
            Department dept = departmentMapper.selectById(user.getDeptId());
            if (dept != null) {
                vo.setDeptName(dept.getDeptName());
            }
        }
        // 设置角色名称列表
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(String.valueOf(user.getId()));
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleMapper.selectBatchIds(roleIds);
            List<String> roleNames = roles.stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toList());
            vo.setRoleNames(roleNames);
        } else {
            vo.setRoleNames(Collections.emptyList());
        }
        return vo;
    }
}
