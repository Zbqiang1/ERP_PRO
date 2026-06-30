package com.erp.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.system.entity.Department;
import com.erp.modules.system.mapper.DepartmentMapper;
import com.erp.modules.system.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 部门服务实现类
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Department> getDeptTree() {
        // 查询所有部门
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Department::getSortOrder);
        List<Department> deptList = departmentMapper.selectList(wrapper);
        // 构建树形结构
        return buildDeptTree(deptList, 0L);
    }

    @Override
    public Department getDeptById(Long id) {
        Department dept = departmentMapper.selectById(id);
        if (dept == null) {
            throw new BusinessException("部门不存在");
        }
        return dept;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDept(Department department) {
        return departmentMapper.insert(department) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(Department department) {
        Department existDept = departmentMapper.selectById(department.getId());
        if (existDept == null) {
            throw new BusinessException("部门不存在");
        }
        return departmentMapper.updateById(department) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeptById(Long id) {
        // 检查是否有子部门
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getParentId, id);
        Long childCount = departmentMapper.selectCount(wrapper);
        if (childCount > 0) {
            throw new BusinessException("存在子部门，无法删除");
        }
        return departmentMapper.deleteById(id) > 0;
    }

    @Override
    public List<Department> listAllEnabled() {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getStatus, 1)
                .orderByAsc(Department::getSortOrder);
        return departmentMapper.selectList(wrapper);
    }

    /**
     * 构建部门树
     *
     * @param deptList 部门列表
     * @param parentId 父级ID
     * @return 树形部门列表
     */
    private List<Department> buildDeptTree(List<Department> deptList, Long parentId) {
        List<Department> treeList = new ArrayList<>();
        for (Department dept : deptList) {
            if ((parentId == 0L && dept.getParentId() == null)
                    || (parentId != 0L && parentId.equals(dept.getParentId()))) {
                // 注意：Department实体没有children字段，这里返回的是平铺结构
                // 前端可以根据parentId自行构建树
                treeList.add(dept);
                treeList.addAll(buildDeptTree(deptList, dept.getId()));
            }
        }
        return treeList;
    }
}
