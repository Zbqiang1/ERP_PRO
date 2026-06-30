package com.erp.modules.system.controller;

import com.erp.common.result.Result;
import com.erp.modules.system.entity.Menu;
import com.erp.modules.system.service.IMenuService;
import com.erp.modules.system.vo.MenuVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final IMenuService menuService;

    public MenuController(IMenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 获取菜单树
     *
     * @return 菜单树
     */
    @GetMapping("/tree")
    public Result<List<MenuVO>> tree() {
        List<MenuVO> menuTree = menuService.getMenuTree();
        return Result.ok(menuTree);
    }

    /**
     * 根据用户ID获取菜单树
     *
     * @param userId 用户ID
     * @return 菜单树
     */
    @GetMapping("/tree/{userId}")
    public Result<List<MenuVO>> treeByUserId(@PathVariable String userId) {
        List<MenuVO> menuTree = menuService.getMenuTreeByUserId(userId);
        return Result.ok(menuTree);
    }

    /**
     * 获取所有菜单列表（平铺）
     *
     * @return 菜单列表
     */
    @GetMapping
    public Result<List<Menu>> list() {
        List<Menu> menus = menuService.listAll();
        return Result.ok(menus);
    }

    /**
     * 根据ID获取菜单详情
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    @GetMapping("/{id}")
    public Result<Menu> getById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);
        return Result.ok(menu);
    }

    /**
     * 新增菜单
     *
     * @param menu 菜单信息
     * @return 操作结果
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody Menu menu) {
        boolean result = menuService.addMenu(menu);
        return Result.ok(result);
    }

    /**
     * 修改菜单
     *
     * @param menu 菜单信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Menu menu) {
        menu.setId(id);
        boolean result = menuService.updateMenu(menu);
        return Result.ok(result);
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = menuService.deleteMenuById(id);
        return Result.ok(result);
    }
}
