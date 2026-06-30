package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.Warehouse;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IWarehouseService;
import org.springframework.web.bind.annotation.*;

/**
 * 仓库 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/warehouses")
public class WarehouseController {

    private final IWarehouseService warehouseService;

    public WarehouseController(IWarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * 分页查询仓库
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return warehouseService.pageQuery(dto);
    }

    /**
     * 根据ID查询仓库
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return warehouseService.getById(id);
    }

    /**
     * 新增仓库
     */
    @PostMapping
    public Result add(@RequestBody Warehouse entity) {
        return warehouseService.add(entity);
    }

    /**
     * 修改仓库
     */
    @PutMapping
    public Result update(@RequestBody Warehouse entity) {
        return warehouseService.update(entity);
    }

    /**
     * 删除仓库
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return warehouseService.delete(id);
    }
}
