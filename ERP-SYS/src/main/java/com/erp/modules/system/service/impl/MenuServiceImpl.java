package com.erp.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.system.entity.Menu;
import com.erp.modules.system.mapper.MenuMapper;
import com.erp.modules.system.service.IMenuService;
import com.erp.modules.system.vo.MenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public List<MenuVO> getMenuTree() {
        // 查询所有菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSortOrder);
        List<Menu> menuList = menuMapper.selectList(wrapper);
        // 转换为VO列表
        List<MenuVO> voList = menuList.stream().map(menu -> {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu, vo);
            return vo;
        }).collect(Collectors.toList());
        // 构建树形结构
        return buildMenuTree(voList, 0L);
    }

    @Override
    public List<MenuVO> getMenuTreeByUserId(String userId) {
        // 根据用户ID查询菜单
        List<Menu> menuList = menuMapper.selectByUserId(userId);
        // 转换为VO列表
        List<MenuVO> voList = menuList.stream().map(menu -> {
            MenuVO vo = new MenuVO();
            BeanUtils.copyProperties(menu, vo);
            return vo;
        }).collect(Collectors.toList());
        // 构建树形结构
        return buildMenuTree(voList, 0L);
    }

    @Override
    public Menu getMenuById(Long id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMenu(Menu menu) {
        return menuMapper.insert(menu) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(Menu menu) {
        Menu existMenu = menuMapper.selectById(menu.getId());
        if (existMenu == null) {
            throw new BusinessException("菜单不存在");
        }
        return menuMapper.updateById(menu) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenuById(Long id) {
        // 检查是否有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, id);
        Long childCount = menuMapper.selectCount(wrapper);
        if (childCount > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }
        return menuMapper.deleteById(id) > 0;
    }

    @Override
    public List<Menu> listAll() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getSortOrder);
        return menuMapper.selectList(wrapper);
    }

    /**
     * 构建菜单树
     *
     * @param menuList 菜单列表
     * @param parentId 父级ID
     * @return 树形菜单列表
     */
    private List<MenuVO> buildMenuTree(List<MenuVO> menuList, Long parentId) {
        List<MenuVO> treeList = new ArrayList<>();
        for (MenuVO menu : menuList) {
            // 将 null 和 0L 都视为根节点
            Long menuParentId = menu.getParentId() != null ? menu.getParentId() : 0L;
            if (parentId.equals(menuParentId)) {
                // 递归查找子节点
                List<MenuVO> children = buildMenuTree(menuList, menu.getId());
                menu.setChildren(children);
                treeList.add(menu);
            }
        }
        // 按排序字段排序
        treeList.sort(Comparator.comparing(MenuVO::getSortOrder, Comparator.nullsLast(Comparator.naturalOrder())));
        return treeList;
    }
}
