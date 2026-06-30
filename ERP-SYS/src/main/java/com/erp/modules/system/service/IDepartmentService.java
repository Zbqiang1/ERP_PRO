package com.erp.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.system.entity.Department;

import java.util.List;

/**
 * 部门服务接口
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获取部门树
     *
     * @return 部门树列表
     */
    List<Department> getDeptTree();

    /**
     * 根据ID获取部门
     *
     * @param id 部门ID
     * @return 部门实体
     */
    Department getDeptById(Long id);

    /**
     * 新增部门
     *
     * @param department 部门信息
     * @return 是否成功
     */
    boolean addDept(Department department);

    /**
     * 修改部门
     *
     * @param department 部门信息
     * @return 是否成功
     */
    boolean updateDept(Department department);

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 是否成功
     */
    boolean deleteDeptById(Long id);

    /**
     * 获取所有启用部门列表
     *
     * @return 部门列表
     */
    List<Department> listAllEnabled();
}
