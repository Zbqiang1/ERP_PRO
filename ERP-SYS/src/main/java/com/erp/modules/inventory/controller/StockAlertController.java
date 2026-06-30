package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.StockAlert;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IStockAlertService;
import org.springframework.web.bind.annotation.*;

/**
 * 库存预警 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/stock-alerts")
public class StockAlertController {

    private final IStockAlertService stockAlertService;

    public StockAlertController(IStockAlertService stockAlertService) {
        this.stockAlertService = stockAlertService;
    }

    /**
     * 分页查询库存预警
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return stockAlertService.pageQuery(dto);
    }

    /**
     * 根据ID查询库存预警
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return stockAlertService.getById(id);
    }

    /**
     * 新增库存预警
     */
    @PostMapping
    public Result add(@RequestBody StockAlert entity) {
        return stockAlertService.add(entity);
    }

    /**
     * 修改库存预警
     */
    @PutMapping
    public Result update(@RequestBody StockAlert entity) {
        return stockAlertService.update(entity);
    }

    /**
     * 删除库存预警
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return stockAlertService.delete(id);
    }
}
