package com.erp.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.report.entity.DashboardWidget;
import com.erp.modules.report.service.IDashboardWidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 仪表盘管理 控制器
 */
@RestController
@RequestMapping("/api/report/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardWidgetService dashboardWidgetService;

    /** 分页查询 */
    @GetMapping
    public Result<Page<DashboardWidget>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(dashboardWidgetService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<DashboardWidget> getById(@PathVariable Long id) {
        return Result.ok(dashboardWidgetService.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Boolean> save(@RequestBody DashboardWidget entity) {
        return Result.ok(dashboardWidgetService.save(entity));
    }

    /** 更新 */
    @PutMapping
    public Result<Boolean> update(@RequestBody DashboardWidget entity) {
        return Result.ok(dashboardWidgetService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(dashboardWidgetService.removeById(id));
    }
}
