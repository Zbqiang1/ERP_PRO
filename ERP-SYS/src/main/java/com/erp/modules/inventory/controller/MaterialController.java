package com.erp.modules.inventory.controller;

import com.erp.common.result.Result;
import com.erp.modules.inventory.Material;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.service.IMaterialService;
import org.springframework.web.bind.annotation.*;

/**
 * 物料 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/inventory/materials")
public class MaterialController {

    private final IMaterialService materialService;

    public MaterialController(IMaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * 分页查询物料
     */
    @GetMapping
    public Result pageQuery(InventoryQueryDTO dto) {
        return materialService.pageQuery(dto);
    }

    /**
     * 根据ID查询物料
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return materialService.getById(id);
    }

    /**
     * 新增物料
     */
    @PostMapping
    public Result add(@RequestBody Material entity) {
        return materialService.add(entity);
    }

    /**
     * 修改物料
     */
    @PutMapping
    public Result update(@RequestBody Material entity) {
        return materialService.update(entity);
    }

    /**
     * 删除物料
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return materialService.delete(id);
    }
}
