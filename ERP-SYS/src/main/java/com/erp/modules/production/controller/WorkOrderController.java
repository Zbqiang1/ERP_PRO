package com.erp.modules.production.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.common.exception.BusinessException;
import com.erp.modules.production.entity.WorkOrder;
import com.erp.modules.production.entity.WoRouting;
import com.erp.modules.production.service.IWorkOrderService;
import com.erp.modules.production.service.IWoRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 工单管理 控制器
 */
@RestController
@RequestMapping("/api/production/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final IWorkOrderService workOrderService;
    private final IWoRoutingService woRoutingService;

    // ==================== 工单 ====================

    /** 分页查询工单 */
    @GetMapping
    public Result<Page<WorkOrder>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(workOrderService.page(new Page<>(page, size)));
    }

    /** 根据ID查询工单 */
    @GetMapping("/{id}")
    public Result<WorkOrder> getById(@PathVariable Long id) {
        WorkOrder entity = workOrderService.getById(id);
        if (entity == null) {
            return Result.notFound("工单不存在");
        }
        return Result.ok(entity);
    }

    /** 新增工单 */
    @PostMapping
    public Result<Boolean> save(@RequestBody WorkOrder entity) {
        return Result.ok(workOrderService.save(entity));
    }

    /** 更新工单 */
    @PutMapping
    public Result<Boolean> update(@RequestBody WorkOrder entity) {
        return Result.ok(workOrderService.updateById(entity));
    }

    /** 删除工单 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (!workOrderService.removeById(id)) {
            return Result.notFound("工单不存在");
        }
        return Result.ok(true);
    }

    // ==================== 工艺路线 ====================

    /** 根据工单ID查询工艺路线 */
    @GetMapping("/{woId}/routings")
    public Result<java.util.List<WoRouting>> listRoutings(@PathVariable Long woId) {
        LambdaQueryWrapper<WoRouting> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WoRouting::getWoId, woId).orderByAsc(WoRouting::getOperationSeq);
        return Result.ok(woRoutingService.list(wrapper));
    }

    /** 新增工艺路线 */
    @PostMapping("/routing")
    public Result<Boolean> saveRouting(@RequestBody WoRouting entity) {
        return Result.ok(woRoutingService.save(entity));
    }

    /** 更新工艺路线 */
    @PutMapping("/routing")
    public Result<Boolean> updateRouting(@RequestBody WoRouting entity) {
        return Result.ok(woRoutingService.updateById(entity));
    }

    /** 删除工艺路线 */
    @DeleteMapping("/routing/{id}")
    public Result<Boolean> deleteRouting(@PathVariable Long id) {
        return Result.ok(woRoutingService.removeById(id));
    }
}
