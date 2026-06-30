package com.erp.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.system.dto.LoginDTO;
import com.erp.modules.system.dto.UserDTO;
import com.erp.modules.system.dto.UserQueryDTO;
import com.erp.modules.system.entity.User;
import com.erp.modules.system.vo.LoginVO;
import com.erp.modules.system.vo.UserVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param loginDTO 登录请求
     * @return 登录响应
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 根据ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户视图对象
     */
    UserVO getUserById(Long id);

    /**
     * 分页查询用户
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<UserVO> pageUser(UserQueryDTO queryDTO);

    /**
     * 新增用户
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    boolean addUser(UserDTO userDTO);

    /**
     * 修改用户
     *
     * @param id      用户ID
     * @param userDTO 用户信息
     * @return 是否成功
     */
    boolean updateUser(Long id, UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUserById(Long id);

    /**
     * 重置密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long id, String password);

    /**
     * 为用户分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignRoles(String userId, List<Long> roleIds);
}
