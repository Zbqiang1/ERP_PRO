package com.erp.modules.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.sales.dto.CustomerDTO;
import com.erp.modules.sales.dto.CustomerQueryDTO;
import com.erp.modules.sales.entity.Customer;
import com.erp.modules.sales.mapper.CustomerMapper;
import com.erp.modules.sales.service.ICustomerService;
import com.erp.modules.sales.vo.CustomerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 客户 服务实现类
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Page<CustomerVO> queryPage(CustomerQueryDTO queryDTO) {
        Page<Customer> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getCustomerCode()), Customer::getCustomerCode, queryDTO.getCustomerCode());
        wrapper.like(StringUtils.hasText(queryDTO.getCustomerName()), Customer::getCustomerName, queryDTO.getCustomerName());
        Integer statusInt = null;
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            try { statusInt = Integer.valueOf(queryDTO.getStatus()); } catch (NumberFormatException e) { }
        }
        wrapper.eq(statusInt != null, Customer::getStatus, statusInt);
        wrapper.orderByDesc(Customer::getCreateTime);
        Page<Customer> resultPage = this.page(page, wrapper);
        Page<CustomerVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public CustomerVO getDetailById(Long id) {
        Customer customer = this.getById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        return toVO(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CustomerDTO dto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto, customer);
        this.save(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CustomerDTO dto) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(dto, customer);
        this.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Customer customer = this.getById(id);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }
        customer.setStatus(status);
        this.updateById(customer);
    }

    private CustomerVO toVO(Customer customer) {
        CustomerVO vo = new CustomerVO();
        BeanUtils.copyProperties(customer, vo);
        return vo;
    }
}
