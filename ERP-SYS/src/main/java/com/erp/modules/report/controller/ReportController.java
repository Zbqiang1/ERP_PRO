package com.erp.modules.report.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.report.entity.ReportTemplate;
import com.erp.modules.report.service.IReportTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报表管理 控制器
 */
@RestController
@RequestMapping("/api/report/reports")
@RequiredArgsConstructor
public class ReportController {

    private final IReportTemplateService reportTemplateService;

    /** 分页查询 */
    @GetMapping
    public Result<Page<ReportTemplate>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.ok(reportTemplateService.page(new Page<>(page, size)));
    }

    /** 根据ID查询 */
    @GetMapping("/{id}")
    public Result<ReportTemplate> getById(@PathVariable Long id) {
        ReportTemplate entity = reportTemplateService.getById(id);
        if (entity == null) {
            return Result.notFound("报表模板不存在");
        }
        return Result.ok(entity);
    }

    /** 新增 */
    @PostMapping
    public Result<Boolean> save(@RequestBody ReportTemplate entity) {
        return Result.ok(reportTemplateService.save(entity));
    }

    /** 更新 */
    @PutMapping
    public Result<Boolean> update(@RequestBody ReportTemplate entity) {
        return Result.ok(reportTemplateService.updateById(entity));
    }

    /** 删除 */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        if (!reportTemplateService.removeById(id)) {
            return Result.notFound("报表模板不存在");
        }
        return Result.ok(true);
    }
}
