package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.StockOutOrder;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IStockOutOrderService;
import org.springframework.web.bind.annotation.*;

/**
 * 出库单 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/stock-out-orders")
public class StockOutOrderController {

    private final IStockOutOrderService stockOutOrderService;

    public StockOutOrderController(IStockOutOrderService stockOutOrderService) {
        this.stockOutOrderService = stockOutOrderService;
    }

    /**
     * 分页查询出库单
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return stockOutOrderService.pageQuery(dto);
    }

    /**
     * 根据ID查询出库单
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return stockOutOrderService.getById(id);
    }

    /**
     * 新增出库单
     */
    @PostMapping
    public Result add(@RequestBody StockOutOrder entity) {
        return stockOutOrderService.add(entity);
    }

    /**
     * 修改出库单
     */
    @PutMapping
    public Result update(@RequestBody StockOutOrder entity) {
        return stockOutOrderService.update(entity);
    }

    /**
     * 删除出库单
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return stockOutOrderService.delete(id);
    }
}
