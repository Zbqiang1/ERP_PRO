package com.erp.modules.system.controller;

import com.erp.common.result.Result;
import com.erp.modules.system.entity.Department;
import com.erp.modules.system.service.IDepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final IDepartmentService departmentService;

    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取部门树
     *
     * @return 部门树
     */
    @GetMapping("/tree")
    public Result<List<Department>> tree() {
        List<Department> deptTree = departmentService.getDeptTree();
        return Result.ok(deptTree);
    }

    /**
     * 获取所有启用部门
     *
     * @return 部门列表
     */
    @GetMapping
    public Result<List<Department>> list() {
        List<Department> depts = departmentService.listAllEnabled();
        return Result.ok(depts);
    }

    /**
     * 根据ID获取部门详情
     *
     * @param id 部门ID
     * @return 部门信息
     */
    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        Department dept = departmentService.getDeptById(id);
        return Result.ok(dept);
    }

    /**
     * 新增部门
     *
     * @param department 部门信息
     * @return 操作结果
     */
    @PostMapping
    public Result<Boolean> add(@RequestBody Department department) {
        boolean result = departmentService.addDept(department);
        return Result.ok(result);
    }

    /**
     * 修改部门
     *
     * @param department 部门信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Department department) {
        department.setId(id);
        boolean result = departmentService.updateDept(department);
        return Result.ok(result);
    }

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean result = departmentService.deleteDeptById(id);
        return Result.ok(result);
    }
}
