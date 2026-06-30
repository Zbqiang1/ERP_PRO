package com.erp.modules.production.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.common.exception.BusinessException;
import com.erp.modules.production.entity.MpsPlan;
import com.erp.modules.production.service.IMpsPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 主生产计划 控制器
 */
@RestController
@RequestMapping("/api/production/mps")
@RequiredArgsConstructor
public class MpsController {

    private final IMpsPlanService mpsPlanService;

    /** 分页查询 */
    @GetMapping
    public Result<Page<MpsPlan>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(mpsPlanService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<MpsPlan> getById(@PathVariable Long id) {
        MpsPlan entity = mpsPlanService.getById(id);
        if (entity == null) {
            return Result.notFound("主生产计划不存在");
        }
        return Result.ok(entity);
    }

    /** 新增 */
    @PostMapping
    public Result<Boolean> save(@RequestBody MpsPlan entity) {
        return Result.ok(mpsPlanService.save(entity));
    }

    /** 更新 */
    @PutMapping
    public Result<Boolean> update(@RequestBody MpsPlan entity) {
        return Result.ok(mpsPlanService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (!mpsPlanService.removeById(id)) {
            return Result.notFound("主生产计划不存在");
        }
        return Result.ok(true);
    }
}
