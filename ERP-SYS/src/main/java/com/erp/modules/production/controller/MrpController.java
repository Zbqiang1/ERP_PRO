package com.erp.modules.production.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.common.exception.BusinessException;
import com.erp.modules.production.entity.MrpResult;
import com.erp.modules.production.service.IMrpResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * MRP运算 控制器
 */
@RestController
@RequestMapping("/api/production/mrp")
@RequiredArgsConstructor
public class MrpController {

    private final IMrpResultService mrpResultService;

    /** 分页查询 */
    @GetMapping
    public Result<Page<MrpResult>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(mrpResultService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<MrpResult> getById(@PathVariable Long id) {
        return Result.ok(mrpResultService.getById(id));
    }

    /** 新增 */
    @PostMapping
    public Result<Boolean> save(@RequestBody MrpResult entity) {
        return Result.ok(mrpResultService.save(entity));
    }

    /** 更新 */
    @PutMapping
    public Result<Boolean> update(@RequestBody MrpResult entity) {
        return Result.ok(mrpResultService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(mrpResultService.removeById(id));
    }
}
