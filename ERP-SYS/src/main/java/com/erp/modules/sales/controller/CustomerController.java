package com.erp.modules.sales.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.sales.dto.CustomerDTO;
import com.erp.modules.sales.dto.CustomerQueryDTO;
import com.erp.modules.sales.service.ICustomerService;
import com.erp.modules.sales.vo.CustomerVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 客户 控制器
 */
@RestController
@RequestMapping("/api/sales/customers")
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 分页查询客户
     */
    @GetMapping
    public Result<Page<CustomerVO>> list(CustomerQueryDTO queryDTO) {
        return Result.ok(customerService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询客户
     */
    @GetMapping("/{id}")
    public Result<CustomerVO> getById(@PathVariable Long id) {
        return Result.ok(customerService.getDetailById(id));
    }

    /**
     * 新增客户
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody CustomerDTO dto) {
        customerService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑客户
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CustomerDTO dto) {
        customerService.update(dto);
        return Result.ok();
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return Result.ok();
    }

    /**
     * 更新客户状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        customerService.updateStatus(id, status);
        return Result.ok();
    }
}
