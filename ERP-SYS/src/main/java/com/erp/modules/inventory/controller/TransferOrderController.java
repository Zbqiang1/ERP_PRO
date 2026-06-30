package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.TransferOrder;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.ITransferOrderService;
import org.springframework.web.bind.annotation.*;

/**
 * 调拨单 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/transfer-orders")
public class TransferOrderController {

    private final ITransferOrderService transferOrderService;

    public TransferOrderController(ITransferOrderService transferOrderService) {
        this.transferOrderService = transferOrderService;
    }

    /**
     * 分页查询调拨单
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return transferOrderService.pageQuery(dto);
    }

    /**
     * 根据ID查询调拨单
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return transferOrderService.getById(id);
    }

    /**
     * 新增调拨单
     */
    @PostMapping
    public Result add(@RequestBody TransferOrder entity) {
        return transferOrderService.add(entity);
    }

    /**
     * 修改调拨单
     */
    @PutMapping
    public Result update(@RequestBody TransferOrder entity) {
        return transferOrderService.update(entity);
    }

    /**
     * 删除调拨单
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return transferOrderService.delete(id);
    }
}
