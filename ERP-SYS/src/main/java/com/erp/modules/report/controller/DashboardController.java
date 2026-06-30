package com.erp.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.report.entity.DashboardWidget;
import com.erp.modules.report.service.IDashboardWidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 仪表盘管理 控制器
 */
@RestController
@RequestMapping("/api/report/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardWidgetService dashboardWidgetService;

    // ==================== 数据看板统计接口 ====================

    /** 经营总览统计 */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalUsers", 0);
        stats.put("totalOrders", 0);
        stats.put("totalProducts", 0);
        stats.put("todayIncome", "0");
        stats.put("pendingDeliveries", 0);
        stats.put("onlineUsers", 1);
        return Result.ok(stats);
    }

    /** 月度销售趋势 */
    @GetMapping("/sales-trend")
    public Result<List<Map<String, Object>>> getSalesTrend() {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] months = {"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
        for (String m : months) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("month", m);
            item.put("amount", 0);
            list.add(item);
        }
        return Result.ok(list);
    }

    /** 热销产品Top10 */
    @GetMapping("/top-products")
    public Result<List<Map<String, Object>>> getTopProducts() {
        return Result.ok(new ArrayList<>());
    }

    /** 待发货订单 */
    @GetMapping("/pending-orders")
    public Result<List<Map<String, Object>>> getPendingOrders() {
        return Result.ok(new ArrayList<>());
    }

    /** 库存预警统计 */
    @GetMapping("/inventory-alert")
    public Result<Map<String, Object>> getInventoryAlert() {
        Map<String, Object> alert = new LinkedHashMap<>();
        alert.put("lowStockCount", 0);
        alert.put("overStockCount", 0);
        alert.put("slowMovingCount", 0);
        return Result.ok(alert);
    }

    /** 生产完成率 */
    @GetMapping("/production-rate")
    public Result<Map<String, Object>> getProductionRate() {
        Map<String, Object> rate = new LinkedHashMap<>();
        rate.put("completedCount", 0);
        rate.put("totalCount", 0);
        rate.put("rate", 0);
        return Result.ok(rate);
    }

    // ==================== 仪表盘组件CRUD ====================

    /** 分页查询 */
    @GetMapping
    public Result<Page<DashboardWidget>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(dashboardWidgetService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/widgets/{id}")
    public Result<DashboardWidget> getById(@PathVariable Long id) {
        DashboardWidget entity = dashboardWidgetService.getById(id);
        if (entity == null) {
            return Result.notFound("仪表盘组件不存在");
        }
        return Result.ok(entity);
    }

    /** 新增 */
    @PostMapping("/widgets")
    public Result<Boolean> save(@RequestBody DashboardWidget entity) {
        return Result.ok(dashboardWidgetService.save(entity));
    }

    /** 更新 */
    @PutMapping("/widgets")
    public Result<Boolean> update(@RequestBody DashboardWidget entity) {
        return Result.ok(dashboardWidgetService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/widgets/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (!dashboardWidgetService.removeById(id)) {
            return Result.notFound("仪表盘组件不存在");
        }
        return Result.ok(true);
    }
}
