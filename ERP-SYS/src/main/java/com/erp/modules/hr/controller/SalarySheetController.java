package com.erp.modules.hr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.hr.dto.SalarySheetDTO;
import com.erp.modules.hr.dto.SalarySheetQueryDTO;
import com.erp.modules.hr.service.ISalarySheetService;
import com.erp.modules.hr.vo.SalarySheetVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 工资单 控制器
 */
@RestController
@RequestMapping("/api/hr/salaries")
public class SalarySheetController {

    private final ISalarySheetService salarySheetService;

    public SalarySheetController(ISalarySheetService salarySheetService) {
        this.salarySheetService = salarySheetService;
    }

    /**
     * 分页查询工资单
     */
    @GetMapping
    public Result<Page<SalarySheetVO>> list(SalarySheetQueryDTO queryDTO) {
        return Result.ok(salarySheetService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询工资单
     */
    @GetMapping("/{id}")
    public Result<SalarySheetVO> getById(@PathVariable Long id) {
        return Result.ok(salarySheetService.getDetailById(id));
    }

    /**
     * 新增工资单
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody SalarySheetDTO dto) {
        salarySheetService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑工资单
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SalarySheetDTO dto) {
        dto.setId(id);
        salarySheetService.update(dto);
        return Result.ok();
    }

    /**
     * 删除工资单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        salarySheetService.delete(id);
        return Result.ok();
    }
}
