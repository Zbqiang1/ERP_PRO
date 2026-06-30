package com.erp.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    @Select("SELECT * FROM t_user WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);
}
