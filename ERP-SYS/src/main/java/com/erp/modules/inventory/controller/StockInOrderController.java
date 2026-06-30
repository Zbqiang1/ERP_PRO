package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.StockInOrder;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IStockInOrderService;
import org.springframework.web.bind.annotation.*;

/**
 * 入库单 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/stock-in-orders")
public class StockInOrderController {

    private final IStockInOrderService stockInOrderService;

    public StockInOrderController(IStockInOrderService stockInOrderService) {
        this.stockInOrderService = stockInOrderService;
    }

    /**
     * 分页查询入库单
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return stockInOrderService.pageQuery(dto);
    }

    /**
     * 根据ID查询入库单
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return stockInOrderService.getById(id);
    }

    /**
     * 新增入库单
     */
    @PostMapping
    public Result add(@RequestBody StockInOrder entity) {
        return stockInOrderService.add(entity);
    }

    /**
     * 修改入库单
     */
    @PutMapping
    public Result update(@RequestBody StockInOrder entity) {
        return stockInOrderService.update(entity);
    }

    /**
     * 删除入库单
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return stockInOrderService.delete(id);
    }
}
