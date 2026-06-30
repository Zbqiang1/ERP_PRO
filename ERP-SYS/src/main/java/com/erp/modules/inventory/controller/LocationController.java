package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.Location;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.ILocationService;
import org.springframework.web.bind.annotation.*;

/**
 * 库位 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/locations")
public class LocationController {

    private final ILocationService locationService;

    public LocationController(ILocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * 分页查询库位
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return locationService.pageQuery(dto);
    }

    /**
     * 根据ID查询库位
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return locationService.getById(id);
    }

    /**
     * 新增库位
     */
    @PostMapping
    public Result add(@RequestBody Location entity) {
        return locationService.add(entity);
    }

    /**
     * 修改库位
     */
    @PutMapping
    public Result update(@RequestBody Location entity) {
        return locationService.update(entity);
    }

    /**
     * 删除库位
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return locationService.delete(id);
    }
}
