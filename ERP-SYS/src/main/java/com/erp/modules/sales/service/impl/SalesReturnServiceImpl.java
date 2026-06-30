package com.erp.modules.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.sales.dto.SalesReturnDTO;
import com.erp.modules.sales.dto.SalesReturnQueryDTO;
import com.erp.modules.sales.entity.SalesReturn;
import com.erp.modules.sales.mapper.SalesReturnMapper;
import com.erp.modules.sales.service.ISalesReturnService;
import com.erp.modules.sales.vo.SalesReturnVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 销售退货 服务实现类
 */
@Service
public class SalesReturnServiceImpl extends ServiceImpl<SalesReturnMapper, SalesReturn> implements ISalesReturnService {

    @Override
    public Page<SalesReturnVO> queryPage(SalesReturnQueryDTO queryDTO) {
        Page<SalesReturn> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<SalesReturn> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getReturnNo()), SalesReturn::getReturnNo, queryDTO.getReturnNo());
        wrapper.eq(queryDTO.getSoId() != null, SalesReturn::getSoId, queryDTO.getSoId());
        wrapper.like(StringUtils.hasText(queryDTO.getCustomerName()), SalesReturn::getCustomerName, queryDTO.getCustomerName());
        Integer statusInt = null;
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            try { statusInt = Integer.valueOf(queryDTO.getStatus()); } catch (NumberFormatException e) { }
        }
        wrapper.eq(statusInt != null, SalesReturn::getStatus, statusInt);
        wrapper.orderByDesc(SalesReturn::getCreateTime);
        Page<SalesReturn> resultPage = this.page(page, wrapper);
        Page<SalesReturnVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public SalesReturnVO getDetailById(Long id) {
        SalesReturn salesReturn = this.getById(id);
        if (salesReturn == null) {
            throw new BusinessException("销售退货记录不存在");
        }
        return toVO(salesReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SalesReturnDTO dto) {
        SalesReturn salesReturn = new SalesReturn();
        BeanUtils.copyProperties(dto, salesReturn);
        this.save(salesReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SalesReturnDTO dto) {
        SalesReturn salesReturn = new SalesReturn();
        BeanUtils.copyProperties(dto, salesReturn);
        this.updateById(salesReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    private SalesReturnVO toVO(SalesReturn salesReturn) {
        SalesReturnVO vo = new SalesReturnVO();
        BeanUtils.copyProperties(salesReturn, vo);
        return vo;
    }
}
