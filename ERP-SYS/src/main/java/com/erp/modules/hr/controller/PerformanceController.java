package com.erp.modules.hr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.hr.dto.PerformanceDTO;
import com.erp.modules.hr.dto.PerformanceQueryDTO;
import com.erp.modules.hr.service.IPerformanceService;
import com.erp.modules.hr.vo.PerformanceVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 绩效考核 控制器
 */
@RestController
@RequestMapping("/api/hr/performances")
public class PerformanceController {

    private final IPerformanceService performanceService;

    public PerformanceController(IPerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    /**
     * 分页查询绩效考核
     */
    @GetMapping
    public Result<Page<PerformanceVO>> list(PerformanceQueryDTO queryDTO) {
        return Result.ok(performanceService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询绩效考核
     */
    @GetMapping("/{id}")
    public Result<PerformanceVO> getById(@PathVariable Long id) {
        return Result.ok(performanceService.getDetailById(id));
    }

    /**
     * 新增绩效考核
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody PerformanceDTO dto) {
        performanceService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑绩效考核
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody PerformanceDTO dto) {
        performanceService.update(dto);
        return Result.ok();
    }

    /**
     * 删除绩效考核
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        performanceService.delete(id);
        return Result.ok();
    }
}
