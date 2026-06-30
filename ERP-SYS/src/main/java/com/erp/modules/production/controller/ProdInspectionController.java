package com.erp.modules.production.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.common.exception.BusinessException;
import com.erp.modules.production.entity.ProdInspection;
import com.erp.modules.production.service.IProdInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 生产检验管理 控制器
 */
@RestController
@RequestMapping("/api/production/inspections")
@RequiredArgsConstructor
public class ProdInspectionController {

    private final IProdInspectionService inspectionService;

    /** 分页查询 */
    @GetMapping
    public Result<Page<ProdInspection>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(inspectionService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<ProdInspection> getById(@PathVariable Long id) {
        ProdInspection entity = inspectionService.getById(id);
        if (entity == null) {
            return Result.notFound("生产检验记录不存在");
        }
        return Result.ok(entity);
    }

    /** 新增 */
    @PostMapping
    public Result<Boolean> save(@RequestBody ProdInspection entity) {
        return Result.ok(inspectionService.save(entity));
    }

    /** 更新 */
    @PutMapping
    public Result<Boolean> update(@RequestBody ProdInspection entity) {
        return Result.ok(inspectionService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (!inspectionService.removeById(id)) {
            return Result.notFound("生产检验记录不存在");
        }
        return Result.ok(true);
    }
}
