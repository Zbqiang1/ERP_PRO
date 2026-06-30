package com.erp.modules.sales.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.sales.dto.SalesDeliveryDTO;
import com.erp.modules.sales.dto.SalesDeliveryQueryDTO;
import com.erp.modules.sales.service.ISalesDeliveryService;
import com.erp.modules.sales.vo.SalesDeliveryVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 销售发货 控制器
 */
@RestController
@RequestMapping("/api/sales/deliveries")
public class SalesDeliveryController {

    private final ISalesDeliveryService salesDeliveryService;

    public SalesDeliveryController(ISalesDeliveryService salesDeliveryService) {
        this.salesDeliveryService = salesDeliveryService;
    }

    /**
     * 分页查询销售发货
     */
    @GetMapping
    public Result<Page<SalesDeliveryVO>> list(SalesDeliveryQueryDTO queryDTO) {
        return Result.ok(salesDeliveryService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询销售发货
     */
    @GetMapping("/{id}")
    public Result<SalesDeliveryVO> getById(@PathVariable Long id) {
        return Result.ok(salesDeliveryService.getDetailById(id));
    }

    /**
     * 新增销售发货
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody SalesDeliveryDTO dto) {
        salesDeliveryService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑销售发货
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody SalesDeliveryDTO dto) {
        salesDeliveryService.update(dto);
        return Result.ok();
    }

    /**
     * 删除销售发货
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        salesDeliveryService.delete(id);
        return Result.ok();
    }

    /**
     * 执行发货
     */
    @PutMapping("/{id}/deliver")
    public Result<Void> deliver(@PathVariable Long id) {
        salesDeliveryService.deliver(id);
        return Result.ok();
    }

    /**
     * 确认签收
     */
    @PutMapping("/{id}/sign")
    public Result<Void> sign(@PathVariable Long id) {
        salesDeliveryService.sign(id);
        return Result.ok();
    }
}
