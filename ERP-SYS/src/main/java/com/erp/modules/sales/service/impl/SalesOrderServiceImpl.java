package com.erp.modules.sales.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.sales.dto.SalesOrderDTO;
import com.erp.modules.sales.dto.SalesOrderDetailDTO;
import com.erp.modules.sales.dto.SalesOrderQueryDTO;
import com.erp.modules.sales.entity.SalesOrder;
import com.erp.modules.sales.entity.SalesOrderDetail;
import com.erp.modules.sales.mapper.SalesOrderDetailMapper;
import com.erp.modules.sales.mapper.SalesOrderMapper;
import com.erp.modules.sales.service.ISalesOrderService;
import com.erp.modules.sales.vo.SalesOrderDetailVO;
import com.erp.modules.sales.vo.SalesOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售订单 服务实现类
 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements ISalesOrderService {

    private final SalesOrderDetailMapper detailMapper;

    public SalesOrderServiceImpl(SalesOrderDetailMapper detailMapper) {
        this.detailMapper = detailMapper;
    }

    @Override
    public Page<SalesOrderVO> queryPage(SalesOrderQueryDTO queryDTO) {
        Page<SalesOrder> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<SalesOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getSoNo()), SalesOrder::getSoNo, queryDTO.getSoNo());
        wrapper.like(StringUtils.hasText(queryDTO.getCustomerName()), SalesOrder::getCustomerName, queryDTO.getCustomerName());
        wrapper.eq(queryDTO.getStatus() != null, SalesOrder::getStatus, queryDTO.getStatus());
        wrapper.orderByDesc(SalesOrder::getCreateTime);
        Page<SalesOrder> resultPage = this.page(page, wrapper);
        Page<SalesOrderVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public SalesOrderVO getDetailById(Long id) {
        SalesOrder so = this.getById(id);
        if (so == null) {
            throw new BusinessException("销售订单不存在");
        }
        SalesOrderVO vo = toVO(so);
        LambdaQueryWrapper<SalesOrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(SalesOrderDetail::getSoId, id);
        List<SalesOrderDetail> details = detailMapper.selectList(detailWrapper);
        vo.setDetails(details.stream().map(this::detailToVO).collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SalesOrderDTO dto) {
        SalesOrder so = new SalesOrder();
        BeanUtils.copyProperties(dto, so);
        this.save(so);
        if (!CollectionUtils.isEmpty(dto.getDetails())) {
            for (SalesOrderDetailDTO detailDTO : dto.getDetails()) {
                SalesOrderDetail detail = new SalesOrderDetail();
                BeanUtils.copyProperties(detailDTO, detail);
                detail.setSoId(so.getId());
                detail.setShippedQty(BigDecimal.ZERO);
                if (detail.getQuantity() != null && detail.getUnitPrice() != null) {
                    detail.setAmount(detail.getQuantity().multiply(detail.getUnitPrice()));
                }
                detailMapper.insert(detail);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SalesOrderDTO dto) {
        SalesOrder so = new SalesOrder();
        BeanUtils.copyProperties(dto, so);
        this.updateById(so);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        LambdaQueryWrapper<SalesOrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(SalesOrderDetail::getSoId, id);
        detailMapper.delete(detailWrapper);
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        SalesOrder so = this.getById(id);
        if (so == null) {
            throw new BusinessException("销售订单不存在");
        }
        so.setStatus(1);
        this.updateById(so);
    }

    private SalesOrderVO toVO(SalesOrder so) {
        SalesOrderVO vo = new SalesOrderVO();
        BeanUtils.copyProperties(so, vo);
        return vo;
    }

    private SalesOrderDetailVO detailToVO(SalesOrderDetail detail) {
        SalesOrderDetailVO vo = new SalesOrderDetailVO();
        BeanUtils.copyProperties(detail, vo);
        return vo;
    }
}
