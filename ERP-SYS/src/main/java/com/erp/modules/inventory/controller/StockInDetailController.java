package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.StockInDetail;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IStockInDetailService;
import org.springframework.web.bind.annotation.*;

/**
 * 入库明细 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/stock-in-details")
public class StockInDetailController {

    private final IStockInDetailService stockInDetailService;

    public StockInDetailController(IStockInDetailService stockInDetailService) {
        this.stockInDetailService = stockInDetailService;
    }

    /**
     * 分页查询入库明细
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return stockInDetailService.pageQuery(dto);
    }

    /**
     * 根据ID查询入库明细
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return stockInDetailService.getById(id);
    }

    /**
     * 新增入库明细
     */
    @PostMapping
    public Result add(@RequestBody StockInDetail entity) {
        return stockInDetailService.add(entity);
    }

    /**
     * 修改入库明细
     */
    @PutMapping
    public Result update(@RequestBody StockInDetail entity) {
        return stockInDetailService.update(entity);
    }

    /**
     * 删除入库明细
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return stockInDetailService.delete(id);
    }
}
