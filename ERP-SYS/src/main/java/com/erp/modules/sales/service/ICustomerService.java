package com.erp.modules.sales.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.sales.entity.Customer;
import com.erp.modules.sales.dto.CustomerDTO;
import com.erp.modules.sales.dto.CustomerQueryDTO;
import com.erp.modules.sales.vo.CustomerVO;

/**
 * 客户 服务接口
 */
public interface ICustomerService extends IService<Customer> {

    /**
     * 分页查询客户
     */
    Page<CustomerVO> queryPage(CustomerQueryDTO queryDTO);

    /**
     * 根据ID查询客户
     */
    CustomerVO getDetailById(Long id);

    /**
     * 新增客户
     */
    void create(CustomerDTO dto);

    /**
     * 编辑客户
     */
    void update(CustomerDTO dto);

    /**
     * 删除客户
     */
    void delete(Long id);

    /**
     * 更新客户状态
     */
    void updateStatus(Long id, Integer status);
}
