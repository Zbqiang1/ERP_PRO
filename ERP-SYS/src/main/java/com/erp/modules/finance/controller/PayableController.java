package com.erp.modules.finance.controller;

import com.erp.common.result.Result;
import com.erp.modules.finance.entity.Payable;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.service.IPayableService;
import org.springframework.web.bind.annotation.*;

/**
 * 应付款 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/finance/payables")
public class PayableController {

    private final IPayableService payableService;

    public PayableController(IPayableService payableService) {
        this.payableService = payableService;
    }

    /**
     * 分页查询应付款
     */
    @GetMapping
    public Result pageQuery(FinanceQueryDTO dto) {
        return payableService.pageQuery(dto);
    }

    /**
     * 根据ID查询应付款
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return payableService.getById(id);
    }

    /**
     * 新增应付款
     */
    @PostMapping
    public Result add(@RequestBody Payable entity) {
        return payableService.add(entity);
    }

    /**
     * 修改应付款
     */
    @PutMapping
    public Result update(@RequestBody Payable entity) {
        return payableService.update(entity);
    }

    /**
     * 删除应付款
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return payableService.delete(id);
    }
}
