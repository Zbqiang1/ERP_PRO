package com.erp.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.system.entity.Menu;
import com.erp.modules.system.vo.MenuVO;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 获取菜单树
     *
     * @return 菜单树列表
     */
    List<MenuVO> getMenuTree();

    /**
     * 根据用户ID获取菜单树（用户拥有的权限）
     *
     * @param userId 用户ID
     * @return 菜单树列表
     */
    List<MenuVO> getMenuTreeByUserId(String userId);

    /**
     * 根据ID获取菜单
     *
     * @param id 菜单ID
     * @return 菜单实体
     */
    Menu getMenuById(Long id);

    /**
     * 新增菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    boolean addMenu(Menu menu);

    /**
     * 修改菜单
     *
     * @param menu 菜单信息
     * @return 是否成功
     */
    boolean updateMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 是否成功
     */
    boolean deleteMenuById(Long id);

    /**
     * 获取所有菜单列表（平铺）
     *
     * @return 菜单列表
     */
    List<Menu> listAll();
}
