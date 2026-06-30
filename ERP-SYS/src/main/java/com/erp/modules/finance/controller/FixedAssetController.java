package com.erp.modules.finance.controller;

import com.erp.common.result.Result;
import com.erp.modules.finance.entity.FixedAsset;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.service.IFixedAssetService;
import org.springframework.web.bind.annotation.*;

/**
 * 固定资产 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/finance/fixed-assets")
public class FixedAssetController {

    private final IFixedAssetService fixedAssetService;

    public FixedAssetController(IFixedAssetService fixedAssetService) {
        this.fixedAssetService = fixedAssetService;
    }

    /**
     * 分页查询固定资产
     */
    @GetMapping
    public Result pageQuery(FinanceQueryDTO dto) {
        return fixedAssetService.pageQuery(dto);
    }

    /**
     * 根据ID查询固定资产
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return fixedAssetService.getById(id);
    }

    /**
     * 新增固定资产
     */
    @PostMapping
    public Result add(@RequestBody FixedAsset entity) {
        return fixedAssetService.add(entity);
    }

    /**
     * 修改固定资产
     */
    @PutMapping
    public Result update(@RequestBody FixedAsset entity) {
        return fixedAssetService.update(entity);
    }

    /**
     * 删除固定资产
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return fixedAssetService.delete(id);
    }
}
