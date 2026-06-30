package com.erp.modules.finance.controller;

import com.erp.common.result.Result;
import com.erp.modules.finance.entity.CostSheet;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.service.ICostSheetService;
import org.springframework.web.bind.annotation.*;

/**
 * 成本核算单 前端控制器
 *
 * @author ERP
 */
@RestController
@RequestMapping("/api/finance/cost-sheets")
public class CostSheetController {

    private final ICostSheetService costSheetService;

    public CostSheetController(ICostSheetService costSheetService) {
        this.costSheetService = costSheetService;
    }

    /**
     * 分页查询成本核算单
     */
    @GetMapping
    public Result pageQuery(FinanceQueryDTO dto) {
        return costSheetService.pageQuery(dto);
    }

    /**
     * 根据ID查询成本核算单
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return costSheetService.getById(id);
    }

    /**
     * 新增成本核算单
     */
    @PostMapping
    public Result add(@RequestBody CostSheet entity) {
        return costSheetService.add(entity);
    }

    /**
     * 修改成本核算单
     */
    @PutMapping
    public Result update(@RequestBody CostSheet entity) {
        return costSheetService.update(entity);
    }

    /**
     * 删除成本核算单
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return costSheetService.delete(id);
    }
}
