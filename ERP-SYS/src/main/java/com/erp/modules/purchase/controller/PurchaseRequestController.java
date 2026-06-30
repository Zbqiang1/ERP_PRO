package com.erp.modules.purchase.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.purchase.dto.PurchaseRequestDTO;
import com.erp.modules.purchase.dto.PurchaseRequestQueryDTO;
import com.erp.modules.purchase.service.IPurchaseRequestService;
import com.erp.modules.purchase.vo.PurchaseRequestVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 采购申请 控制器
 */
@RestController
@RequestMapping("/api/purchase/requests")
public class PurchaseRequestController {

    private final IPurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(IPurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    /**
     * 分页查询采购申请
     */
    @GetMapping
    public Result<Page<PurchaseRequestVO>> list(PurchaseRequestQueryDTO queryDTO) {
        return Result.ok(purchaseRequestService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询采购申请（含明细）
     */
    @GetMapping("/{id}")
    public Result<PurchaseRequestVO> getById(@PathVariable Long id) {
        return Result.ok(purchaseRequestService.getDetailById(id));
    }

    /**
     * 新增采购申请
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody PurchaseRequestDTO dto) {
        purchaseRequestService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑采购申请
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody PurchaseRequestDTO dto) {
        purchaseRequestService.update(dto);
        return Result.ok();
    }

    /**
     * 删除采购申请
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        purchaseRequestService.delete(id);
        return Result.ok();
    }

    /**
     * 审批采购申请
     */
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        purchaseRequestService.approve(id);
        return Result.ok();
    }

    /**
     * 驳回采购申请
     */
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        purchaseRequestService.reject(id);
        return Result.ok();
    }
}
