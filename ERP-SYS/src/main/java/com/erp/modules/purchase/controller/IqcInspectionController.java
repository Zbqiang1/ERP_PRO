package com.erp.modules.purchase.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.purchase.dto.IqcInspectionDTO;
import com.erp.modules.purchase.dto.IqcInspectionQueryDTO;
import com.erp.modules.purchase.service.IIqcInspectionService;
import com.erp.modules.purchase.vo.IqcInspectionVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 来料检验 控制器
 */
@RestController
@RequestMapping("/api/purchase/inspections")
public class IqcInspectionController {

    private final IIqcInspectionService inspectionService;

    public IqcInspectionController(IIqcInspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    /**
     * 分页查询来料检验
     */
    @GetMapping
    public Result<Page<IqcInspectionVO>> list(IqcInspectionQueryDTO queryDTO) {
        return Result.ok(inspectionService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询来料检验
     */
    @GetMapping("/{id}")
    public Result<IqcInspectionVO> getById(@PathVariable Long id) {
        return Result.ok(inspectionService.getDetailById(id));
    }

    /**
     * 新增来料检验
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody IqcInspectionDTO dto) {
        inspectionService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑来料检验
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody IqcInspectionDTO dto) {
        inspectionService.update(dto);
        return Result.ok();
    }

    /**
     * 删除来料检验
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        inspectionService.delete(id);
        return Result.ok();
    }
}
