package com.erp.modules.purchase.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.purchase.dto.PurchaseReturnDTO;
import com.erp.modules.purchase.dto.PurchaseReturnQueryDTO;
import com.erp.modules.purchase.service.IPurchaseReturnService;
import com.erp.modules.purchase.vo.PurchaseReturnVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 采购退货 控制器
 */
@RestController
@RequestMapping("/api/purchase/returns")
public class PurchaseReturnController {

    private final IPurchaseReturnService purchaseReturnService;

    public PurchaseReturnController(IPurchaseReturnService purchaseReturnService) {
        this.purchaseReturnService = purchaseReturnService;
    }

    /**
     * 分页查询采购退货
     */
    @GetMapping
    public Result<Page<PurchaseReturnVO>> list(PurchaseReturnQueryDTO queryDTO) {
        return Result.ok(purchaseReturnService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询采购退货
     */
    @GetMapping("/{id}")
    public Result<PurchaseReturnVO> getById(@PathVariable Long id) {
        return Result.ok(purchaseReturnService.getDetailById(id));
    }

    /**
     * 新增采购退货
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody PurchaseReturnDTO dto) {
        purchaseReturnService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑采购退货
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody PurchaseReturnDTO dto) {
        purchaseReturnService.update(dto);
        return Result.ok();
    }

    /**
     * 删除采购退货
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        purchaseReturnService.delete(id);
        return Result.ok();
    }
}
