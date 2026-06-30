package com.erp.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.report.entity.BusinessAlert;
import com.erp.modules.report.service.IBusinessAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 业务预警管理 控制器
 */
@RestController
@RequestMapping("/api/report/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final IBusinessAlertService businessAlertService;

    /** 分页查询 */
    @GetMapping
    public Result<Page<BusinessAlert>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(businessAlertService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<BusinessAlert> getById(@PathVariable Long id) {
        return Result.ok(businessAlertService.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Boolean> save(@RequestBody BusinessAlert entity) {
        return Result.ok(businessAlertService.save(entity));
    }

    /** 更新 */
    @PutMapping
    public Result<Boolean> update(@RequestBody BusinessAlert entity) {
        return Result.ok(businessAlertService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(businessAlertService.removeById(id));
    }
}
