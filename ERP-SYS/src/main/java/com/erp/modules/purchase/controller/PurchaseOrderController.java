package com.erp.modules.purchase.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.purchase.dto.PurchaseOrderDTO;
import com.erp.modules.purchase.dto.PurchaseOrderQueryDTO;
import com.erp.modules.purchase.service.IPurchaseOrderService;
import com.erp.modules.purchase.vo.PurchaseOrderVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 采购订单 控制器
 */
@RestController
@RequestMapping("/api/purchase/orders")
public class PurchaseOrderController {

    private final IPurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(IPurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    /**
     * 分页查询采购订单
     */
    @GetMapping
    public Result<Page<PurchaseOrderVO>> list(PurchaseOrderQueryDTO queryDTO) {
        return Result.ok(purchaseOrderService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询采购订单（含明细）
     */
    @GetMapping("/{id}")
    public Result<PurchaseOrderVO> getById(@PathVariable Long id) {
        return Result.ok(purchaseOrderService.getDetailById(id));
    }

    /**
     * 新增采购订单
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody PurchaseOrderDTO dto) {
        purchaseOrderService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑采购订单
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody PurchaseOrderDTO dto) {
        purchaseOrderService.update(dto);
        return Result.ok();
    }

    /**
     * 删除采购订单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        purchaseOrderService.delete(id);
        return Result.ok();
    }

    /**
     * 确认采购订单
     */
    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        purchaseOrderService.confirm(id);
        return Result.ok();
    }
}
