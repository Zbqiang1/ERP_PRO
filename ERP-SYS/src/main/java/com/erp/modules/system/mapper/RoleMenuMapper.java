package com.erp.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.system.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色菜单关联Mapper接口
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色ID查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    @Select("SELECT menu_id FROM t_role_menu WHERE role_id = #{roleId}")
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
