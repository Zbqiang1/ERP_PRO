package com.erp.modules.purchase.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.purchase.dto.SupplierDTO;
import com.erp.modules.purchase.dto.SupplierQueryDTO;
import com.erp.modules.purchase.service.ISupplierService;
import com.erp.modules.purchase.vo.SupplierVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 供应商 控制器
 */
@RestController
@RequestMapping("/api/purchase/suppliers")
public class SupplierController {

    private final ISupplierService supplierService;

    public SupplierController(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**
     * 分页查询供应商
     */
    @GetMapping
    public Result<Page<SupplierVO>> list(SupplierQueryDTO queryDTO) {
        return Result.ok(supplierService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询供应商
     */
    @GetMapping("/{id}")
    public Result<SupplierVO> getById(@PathVariable Long id) {
        return Result.ok(supplierService.getDetailById(id));
    }

    /**
     * 新增供应商
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody SupplierDTO dto) {
        supplierService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑供应商
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SupplierDTO dto) {
        // 实际项目中 dto 应该携带 id 或者通过路径变量设置
        supplierService.update(dto);
        return Result.ok();
    }

    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        supplierService.delete(id);
        return Result.ok();
    }

    /**
     * 更新供应商状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        supplierService.updateStatus(id, status);
        return Result.ok();
    }
}
