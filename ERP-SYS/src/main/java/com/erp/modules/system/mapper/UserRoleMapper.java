package com.erp.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.system.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @Select("SELECT role_id FROM t_user_role WHERE user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") String userId);
}
