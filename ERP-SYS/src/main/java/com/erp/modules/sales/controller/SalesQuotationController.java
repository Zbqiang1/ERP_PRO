package com.erp.modules.sales.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.sales.dto.SalesQuotationDTO;
import com.erp.modules.sales.dto.SalesQuotationQueryDTO;
import com.erp.modules.sales.service.ISalesQuotationService;
import com.erp.modules.sales.vo.SalesQuotationVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 销售报价 控制器
 */
@RestController
@RequestMapping("/api/sales/quotations")
public class SalesQuotationController {

    private final ISalesQuotationService quotationService;

    public SalesQuotationController(ISalesQuotationService quotationService) {
        this.quotationService = quotationService;
    }

    /**
     * 分页查询销售报价
     */
    @GetMapping
    public Result<Page<SalesQuotationVO>> list(SalesQuotationQueryDTO queryDTO) {
        return Result.ok(quotationService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询销售报价
     */
    @GetMapping("/{id}")
    public Result<SalesQuotationVO> getById(@PathVariable Long id) {
        return Result.ok(quotationService.getDetailById(id));
    }

    /**
     * 新增销售报价
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody SalesQuotationDTO dto) {
        quotationService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑销售报价
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SalesQuotationDTO dto) {
        quotationService.update(dto);
        return Result.ok();
    }

    /**
     * 删除销售报价
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        quotationService.delete(id);
        return Result.ok();
    }

    /**
     * 确认报价
     */
    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        quotationService.confirm(id);
        return Result.ok();
    }
}
