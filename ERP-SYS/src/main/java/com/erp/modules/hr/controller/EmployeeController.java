package com.erp.modules.hr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.hr.dto.EmployeeDTO;
import com.erp.modules.hr.dto.EmployeeQueryDTO;
import com.erp.modules.hr.service.IEmployeeService;
import com.erp.modules.hr.vo.EmployeeVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 员工 控制器
 */
@RestController
@RequestMapping("/api/hr/employees")
public class EmployeeController {

    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 分页查询员工
     */
    @GetMapping
    public Result<Page<EmployeeVO>> list(EmployeeQueryDTO queryDTO) {
        return Result.ok(employeeService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询员工
     */
    @GetMapping("/{id}")
    public Result<EmployeeVO> getById(@PathVariable Long id) {
        return Result.ok(employeeService.getDetailById(id));
    }

    /**
     * 新增员工
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody EmployeeDTO dto) {
        employeeService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑员工
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        employeeService.update(dto);
        return Result.ok();
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return Result.ok();
    }

    /**
     * 更新员工状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        employeeService.updateStatus(id, status);
        return Result.ok();
    }
}
