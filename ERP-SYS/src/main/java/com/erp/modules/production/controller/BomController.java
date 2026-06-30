package com.erp.modules.production.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.common.exception.BusinessException;
import com.erp.modules.production.entity.BomHeader;
import com.erp.modules.production.entity.BomDetail;
import com.erp.modules.production.service.IBomHeaderService;
import com.erp.modules.production.service.IBomDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * BOM管理 控制器
 */
@RestController
@RequestMapping("/api/production/boms")
@RequiredArgsConstructor
public class BomController {

    private final IBomHeaderService bomHeaderService;
    private final IBomDetailService bomDetailService;

    // ==================== 表头 ====================

    /** 分页查询BOM表头 */
    @GetMapping
    public Result<Page<BomHeader>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(bomHeaderService.page(new Page<>(page, size)));
    }

    /** 根据ID查询BOM表头 */
    @GetMapping("/{id}")
    public Result<BomHeader> getById(@PathVariable Long id) {
        BomHeader entity = bomHeaderService.getById(id);
        if (entity == null) {
            return Result.notFound("BOM表头不存在");
        }
        return Result.ok(entity);
    }

    /** 新增BOM表头 */
    @PostMapping
    public Result<Boolean> save(@RequestBody BomHeader entity) {
        return Result.ok(bomHeaderService.save(entity));
    }

    /** 更新BOM表头 */
    @PutMapping
    public Result<Boolean> update(@RequestBody BomHeader entity) {
        return Result.ok(bomHeaderService.updateById(entity));
    }

    /** 删除BOM表头 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (!bomHeaderService.removeById(id)) {
            return Result.notFound("BOM表头不存在");
        }
        return Result.ok(true);
    }

    // ==================== 明细 ====================

    /** 根据表头ID查询明细列表 */
    @GetMapping("/detail/{bomId}")
    public Result<java.util.List<BomDetail>> listDetails(@PathVariable Long bomId) {
        LambdaQueryWrapper<BomDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BomDetail::getBomId, bomId).orderByAsc(BomDetail::getSortOrder);
        return Result.ok(bomDetailService.list(wrapper));
    }

    /** 新增BOM明细 */
    @PostMapping("/detail")
    public Result<Boolean> saveDetail(@RequestBody BomDetail entity) {
        return Result.ok(bomDetailService.save(entity));
    }

    /** 更新BOM明细 */
    @PutMapping("/detail")
    public Result<Boolean> updateDetail(@RequestBody BomDetail entity) {
        return Result.ok(bomDetailService.updateById(entity));
    }

    /** 删除BOM明细 */
    @DeleteMapping("/detail/{id}")
    public Result<Boolean> deleteDetail(@PathVariable Long id) {
        return Result.ok(bomDetailService.removeById(id));
    }
}
