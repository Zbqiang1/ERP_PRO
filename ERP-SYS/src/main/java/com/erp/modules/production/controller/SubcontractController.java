package com.erp.modules.production.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.common.exception.BusinessException;
import com.erp.modules.production.entity.Subcontract;
import com.erp.modules.production.service.ISubcontractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 委外加工管理 控制器
 */
@RestController
@RequestMapping("/api/production/subcontracts")
@RequiredArgsConstructor
public class SubcontractController {

    private final ISubcontractService subcontractService;

    /** 分页查询 */
    @GetMapping
    public Result<Page<Subcontract>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(subcontractService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<Subcontract> getById(@PathVariable Long id) {
        return Result.ok(subcontractService.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Boolean> save(@RequestBody Subcontract entity) {
        return Result.ok(subcontractService.save(entity));
    }

    /** 更新 */
    @PutMapping
    public Result<Boolean> update(@RequestBody Subcontract entity) {
        return Result.ok(subcontractService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(subcontractService.removeById(id));
    }
}
