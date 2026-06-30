package com.erp.modules.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.hr.dto.EmployeeDTO;
import com.erp.modules.hr.dto.EmployeeQueryDTO;
import com.erp.modules.hr.entity.Employee;
import com.erp.modules.hr.mapper.EmployeeMapper;
import com.erp.modules.hr.service.IEmployeeService;
import com.erp.modules.hr.vo.EmployeeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 员工 服务实现类
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Override
    public Page<EmployeeVO> queryPage(EmployeeQueryDTO queryDTO) {
        Page<Employee> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getEmployeeNo()), Employee::getEmployeeNo, queryDTO.getEmployeeNo());
        wrapper.like(StringUtils.hasText(queryDTO.getRealName()), Employee::getRealName, queryDTO.getRealName());
        wrapper.like(StringUtils.hasText(queryDTO.getPhone()), Employee::getPhone, queryDTO.getPhone());
        wrapper.eq(queryDTO.getOrgId() != null, Employee::getOrgId, queryDTO.getOrgId());
        Integer statusInt = null;
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            try { statusInt = Integer.valueOf(queryDTO.getStatus()); } catch (NumberFormatException e) { }
        }
        wrapper.eq(statusInt != null, Employee::getStatus, statusInt);
        wrapper.orderByDesc(Employee::getCreateTime);
        Page<Employee> resultPage = this.page(page, wrapper);
        Page<EmployeeVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public EmployeeVO getDetailById(Long id) {
        Employee employee = this.getById(id);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        return toVO(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EmployeeDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        this.save(employee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EmployeeDTO dto) {
        Employee existing = this.getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("员工不存在");
        }
        BeanUtils.copyProperties(dto, existing);
        this.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!this.removeById(id)) {
            throw new BusinessException("员工不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Employee employee = this.getById(id);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        employee.setStatus(status);
        this.updateById(employee);
    }

    private EmployeeVO toVO(Employee employee) {
        EmployeeVO vo = new EmployeeVO();
        BeanUtils.copyProperties(employee, vo);
        return vo;
    }
}
