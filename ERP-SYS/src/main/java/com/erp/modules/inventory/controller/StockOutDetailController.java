package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.StockOutDetail;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IStockOutDetailService;
import org.springframework.web.bind.annotation.*;

/**
 * 出库明细 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/stock-out-details")
public class StockOutDetailController {

    private final IStockOutDetailService stockOutDetailService;

    public StockOutDetailController(IStockOutDetailService stockOutDetailService) {
        this.stockOutDetailService = stockOutDetailService;
    }

    /**
     * 分页查询出库明细
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return stockOutDetailService.pageQuery(dto);
    }

    /**
     * 根据ID查询出库明细
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return stockOutDetailService.getById(id);
    }

    /**
     * 新增出库明细
     */
    @PostMapping
    public Result add(@RequestBody StockOutDetail entity) {
        return stockOutDetailService.add(entity);
    }

    /**
     * 修改出库明细
     */
    @PutMapping
    public Result update(@RequestBody StockOutDetail entity) {
        return stockOutDetailService.update(entity);
    }

    /**
     * 删除出库明细
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return stockOutDetailService.delete(id);
    }
}
