package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.InventoryCheck;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IInventoryCheckService;
import org.springframework.web.bind.annotation.*;

/**
 * 盘点单 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/inventory-checks")
public class InventoryCheckController {

    private final IInventoryCheckService inventoryCheckService;

    public InventoryCheckController(IInventoryCheckService inventoryCheckService) {
        this.inventoryCheckService = inventoryCheckService;
    }

    /**
     * 分页查询盘点单
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return inventoryCheckService.pageQuery(dto);
    }

    /**
     * 根据ID查询盘点单
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return inventoryCheckService.getById(id);
    }

    /**
     * 新增盘点单
     */
    @PostMapping
    public Result add(@RequestBody InventoryCheck entity) {
        return inventoryCheckService.add(entity);
    }

    /**
     * 修改盘点单
     */
    @PutMapping
    public Result update(@RequestBody InventoryCheck entity) {
        return inventoryCheckService.update(entity);
    }

    /**
     * 删除盘点单
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return inventoryCheckService.delete(id);
    }
}
