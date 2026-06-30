package com.erp.modules.finance.controller;

import com.erp.common.result.Result;
import com.erp.modules.finance.entity.Receivable;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.service.IReceivableService;
import org.springframework.web.bind.annotation.*;

/**
 * 应收款 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/finance/receivables")
public class ReceivableController {

    private final IReceivableService receivableService;

    public ReceivableController(IReceivableService receivableService) {
        this.receivableService = receivableService;
    }

    /**
     * 分页查询应收款
     */
    @GetMapping
    public Result pageQuery(FinanceQueryDTO dto) {
        return receivableService.pageQuery(dto);
    }

    /**
     * 根据ID查询应收款
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return receivableService.getById(id);
    }

    /**
     * 新增应收款
     */
    @PostMapping
    public Result add(@RequestBody Receivable entity) {
        return receivableService.add(entity);
    }

    /**
     * 修改应收款
     */
    @PutMapping
    public Result update(@RequestBody Receivable entity) {
        return receivableService.update(entity);
    }

    /**
     * 删除应收款
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return receivableService.delete(id);
    }
}
