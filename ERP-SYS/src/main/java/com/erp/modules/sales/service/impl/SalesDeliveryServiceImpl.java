package com.erp.modules.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.sales.dto.SalesDeliveryDTO;
import com.erp.modules.sales.dto.SalesDeliveryQueryDTO;
import com.erp.modules.sales.entity.SalesDelivery;
import com.erp.modules.sales.mapper.SalesDeliveryMapper;
import com.erp.modules.sales.service.ISalesDeliveryService;
import com.erp.modules.sales.vo.SalesDeliveryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 销售发货 服务实现类
 */
@Service
public class SalesDeliveryServiceImpl extends ServiceImpl<SalesDeliveryMapper, SalesDelivery> implements ISalesDeliveryService {

    @Override
    public Page<SalesDeliveryVO> queryPage(SalesDeliveryQueryDTO queryDTO) {
        Page<SalesDelivery> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<SalesDelivery> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getDeliveryNo()), SalesDelivery::getDeliveryNo, queryDTO.getDeliveryNo());
        wrapper.eq(queryDTO.getSoId() != null, SalesDelivery::getSoId, queryDTO.getSoId());
        wrapper.like(StringUtils.hasText(queryDTO.getCustomerName()), SalesDelivery::getCustomerName, queryDTO.getCustomerName());
        Integer statusInt = null;
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            try { statusInt = Integer.valueOf(queryDTO.getStatus()); } catch (NumberFormatException e) { }
        }
        wrapper.eq(statusInt != null, SalesDelivery::getStatus, statusInt);
        wrapper.orderByDesc(SalesDelivery::getCreateTime);
        Page<SalesDelivery> resultPage = this.page(page, wrapper);
        Page<SalesDeliveryVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public SalesDeliveryVO getDetailById(Long id) {
        SalesDelivery delivery = this.getById(id);
        if (delivery == null) {
            throw new BusinessException("销售发货记录不存在");
        }
        return toVO(delivery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SalesDeliveryDTO dto) {
        SalesDelivery delivery = new SalesDelivery();
        BeanUtils.copyProperties(dto, delivery);
        this.save(delivery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SalesDeliveryDTO dto) {
        SalesDelivery delivery = new SalesDelivery();
        BeanUtils.copyProperties(dto, delivery);
        this.updateById(delivery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deliver(Long id) {
        SalesDelivery delivery = this.getById(id);
        if (delivery == null) {
            throw new BusinessException("销售发货记录不存在");
        }
        delivery.setStatus(1);
        this.updateById(delivery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(Long id) {
        SalesDelivery delivery = this.getById(id);
        if (delivery == null) {
            throw new BusinessException("销售发货记录不存在");
        }
        delivery.setStatus(2);
        this.updateById(delivery);
    }

    private SalesDeliveryVO toVO(SalesDelivery delivery) {
        SalesDeliveryVO vo = new SalesDeliveryVO();
        BeanUtils.copyProperties(delivery, vo);
        return vo;
    }
}
