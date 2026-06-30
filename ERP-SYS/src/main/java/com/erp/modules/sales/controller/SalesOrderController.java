package com.erp.modules.sales.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.sales.dto.SalesOrderDTO;
import com.erp.modules.sales.dto.SalesOrderQueryDTO;
import com.erp.modules.sales.service.ISalesOrderService;
import com.erp.modules.sales.vo.SalesOrderVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 销售订单 控制器
 */
@RestController
@RequestMapping("/api/sales/orders")
public class SalesOrderController {

    private final ISalesOrderService salesOrderService;

    public SalesOrderController(ISalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    /**
     * 分页查询销售订单
     */
    @GetMapping
    public Result<Page<SalesOrderVO>> list(SalesOrderQueryDTO queryDTO) {
        return Result.ok(salesOrderService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询销售订单（含明细）
     */
    @GetMapping("/{id}")
    public Result<SalesOrderVO> getById(@PathVariable Long id) {
        return Result.ok(salesOrderService.getDetailById(id));
    }

    /**
     * 新增销售订单
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody SalesOrderDTO dto) {
        salesOrderService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑销售订单
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SalesOrderDTO dto) {
        salesOrderService.update(dto);
        return Result.ok();
    }

    /**
     * 删除销售订单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        salesOrderService.delete(id);
        return Result.ok();
    }

    /**
     * 确认销售订单
     */
    @PutMapping("/{id}/confirm")
    public Result<Void> confirm(@PathVariable Long id) {
        salesOrderService.confirm(id);
        return Result.ok();
    }
}
