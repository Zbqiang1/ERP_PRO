package com.erp.modules.hr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.hr.entity.Employee;
import com.erp.modules.hr.dto.EmployeeDTO;
import com.erp.modules.hr.dto.EmployeeQueryDTO;
import com.erp.modules.hr.vo.EmployeeVO;

/**
 * 员工 服务接口
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 分页查询员工
     */
    Page<EmployeeVO> queryPage(EmployeeQueryDTO queryDTO);

    /**
     * 根据ID查询员工
     */
    EmployeeVO getDetailById(Long id);

    /**
     * 新增员工
     */
    void create(EmployeeDTO dto);

    /**
     * 编辑员工
     */
    void update(EmployeeDTO dto);

    /**
     * 删除员工
     */
    void delete(Long id);

    /**
     * 更新员工状态
     */
    void updateStatus(Long id, Integer status);
}
