package com.erp.modules.sales.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.sales.dto.SalesReturnDTO;
import com.erp.modules.sales.dto.SalesReturnQueryDTO;
import com.erp.modules.sales.service.ISalesReturnService;
import com.erp.modules.sales.vo.SalesReturnVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 销售退货 控制器
 */
@RestController
@RequestMapping("/api/sales/returns")
public class SalesReturnController {

    private final ISalesReturnService salesReturnService;

    public SalesReturnController(ISalesReturnService salesReturnService) {
        this.salesReturnService = salesReturnService;
    }

    /**
     * 分页查询销售退货
     */
    @GetMapping
    public Result<Page<SalesReturnVO>> list(SalesReturnQueryDTO queryDTO) {
        return Result.ok(salesReturnService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询销售退货
     */
    @GetMapping("/{id}")
    public Result<SalesReturnVO> getById(@PathVariable Long id) {
        return Result.ok(salesReturnService.getDetailById(id));
    }

    /**
     * 新增销售退货
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody SalesReturnDTO dto) {
        salesReturnService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑销售退货
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SalesReturnDTO dto) {
        salesReturnService.update(dto);
        return Result.ok();
    }

    /**
     * 删除销售退货
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        salesReturnService.delete(id);
        return Result.ok();
    }
}
