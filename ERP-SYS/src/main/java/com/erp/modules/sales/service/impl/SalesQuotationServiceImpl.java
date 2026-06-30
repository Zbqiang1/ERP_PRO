package com.erp.modules.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.sales.dto.SalesQuotationDTO;
import com.erp.modules.sales.dto.SalesQuotationQueryDTO;
import com.erp.modules.sales.entity.SalesQuotation;
import com.erp.modules.sales.mapper.SalesQuotationMapper;
import com.erp.modules.sales.service.ISalesQuotationService;
import com.erp.modules.sales.vo.SalesQuotationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 销售报价 服务实现类
 */
@Service
public class SalesQuotationServiceImpl extends ServiceImpl<SalesQuotationMapper, SalesQuotation> implements ISalesQuotationService {

    @Override
    public Page<SalesQuotationVO> queryPage(SalesQuotationQueryDTO queryDTO) {
        Page<SalesQuotation> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<SalesQuotation> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getQuotationNo()), SalesQuotation::getQuotationNo, queryDTO.getQuotationNo());
        wrapper.like(StringUtils.hasText(queryDTO.getCustomerName()), SalesQuotation::getCustomerName, queryDTO.getCustomerName());
        wrapper.eq(queryDTO.getStatus() != null, SalesQuotation::getStatus, queryDTO.getStatus());
        wrapper.orderByDesc(SalesQuotation::getCreateTime);
        Page<SalesQuotation> resultPage = this.page(page, wrapper);
        Page<SalesQuotationVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public SalesQuotationVO getDetailById(Long id) {
        SalesQuotation quotation = this.getById(id);
        if (quotation == null) {
            throw new BusinessException("销售报价不存在");
        }
        return toVO(quotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SalesQuotationDTO dto) {
        SalesQuotation quotation = new SalesQuotation();
        BeanUtils.copyProperties(dto, quotation);
        this.save(quotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SalesQuotationDTO dto) {
        SalesQuotation quotation = new SalesQuotation();
        BeanUtils.copyProperties(dto, quotation);
        this.updateById(quotation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        SalesQuotation quotation = this.getById(id);
        if (quotation == null) {
            throw new BusinessException("销售报价不存在");
        }
        quotation.setStatus(2);
        this.updateById(quotation);
    }

    private SalesQuotationVO toVO(SalesQuotation quotation) {
        SalesQuotationVO vo = new SalesQuotationVO();
        BeanUtils.copyProperties(quotation, vo);
        return vo;
    }
}
