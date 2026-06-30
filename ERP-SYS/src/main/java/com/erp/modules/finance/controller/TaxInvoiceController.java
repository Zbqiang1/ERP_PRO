package com.erp.modules.finance.controller;

import com.erp.common.result.Result;
import com.erp.modules.finance.entity.TaxInvoice;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.service.ITaxInvoiceService;
import org.springframework.web.bind.annotation.*;

/**
 * 税务发票 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/finance/tax-invoices")
public class TaxInvoiceController {

    private final ITaxInvoiceService taxInvoiceService;

    public TaxInvoiceController(ITaxInvoiceService taxInvoiceService) {
        this.taxInvoiceService = taxInvoiceService;
    }

    /**
     * 分页查询税务发票
     */
    @GetMapping
    public Result pageQuery(FinanceQueryDTO dto) {
        return taxInvoiceService.pageQuery(dto);
    }

    /**
     * 根据ID查询税务发票
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return taxInvoiceService.getById(id);
    }

    /**
     * 新增税务发票
     */
    @PostMapping
    public Result add(@RequestBody TaxInvoice entity) {
        return taxInvoiceService.add(entity);
    }

    /**
     * 修改税务发票
     */
    @PutMapping
    public Result update(@RequestBody TaxInvoice entity) {
        return taxInvoiceService.update(entity);
    }

    /**
     * 删除税务发票
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return taxInvoiceService.delete(id);
    }
}
