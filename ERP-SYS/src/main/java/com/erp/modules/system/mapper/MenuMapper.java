package com.erp.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单Mapper接口
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID查询菜单（通过用户角色关联）
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Select("SELECT DISTINCT m.* FROM t_menu m " +
            "INNER JOIN t_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN t_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.deleted = 0 " +
            "ORDER BY m.sort_order ASC")
    List<Menu> selectByUserId(@Param("userId") String userId);
}
