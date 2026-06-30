package com.erp.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.purchase.dto.PurchaseOrderDTO;
import com.erp.modules.purchase.dto.PurchaseOrderDetailDTO;
import com.erp.modules.purchase.dto.PurchaseOrderQueryDTO;
import com.erp.modules.purchase.entity.PurchaseOrder;
import com.erp.modules.purchase.entity.PurchaseOrderDetail;
import com.erp.modules.purchase.mapper.PurchaseOrderDetailMapper;
import com.erp.modules.purchase.mapper.PurchaseOrderMapper;
import com.erp.modules.purchase.service.IPurchaseOrderService;
import com.erp.modules.purchase.vo.PurchaseOrderDetailVO;
import com.erp.modules.purchase.vo.PurchaseOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购订单 服务实现类
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements IPurchaseOrderService {

    private final PurchaseOrderDetailMapper detailMapper;

    public PurchaseOrderServiceImpl(PurchaseOrderDetailMapper detailMapper) {
        this.detailMapper = detailMapper;
    }

    @Override
    public Page<PurchaseOrderVO> queryPage(PurchaseOrderQueryDTO queryDTO) {
        Page<PurchaseOrder> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<PurchaseOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getPoNo()), PurchaseOrder::getPoNo, queryDTO.getPoNo());
        wrapper.like(StringUtils.hasText(queryDTO.getSupplierName()), PurchaseOrder::getSupplierName, queryDTO.getSupplierName());
        wrapper.eq(queryDTO.getStatus() != null, PurchaseOrder::getStatus, queryDTO.getStatus());
        wrapper.orderByDesc(PurchaseOrder::getCreateTime);
        Page<PurchaseOrder> resultPage = this.page(page, wrapper);
        Page<PurchaseOrderVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public PurchaseOrderVO getDetailById(Long id) {
        PurchaseOrder po = this.getById(id);
        if (po == null) {
            throw new BusinessException("采购订单不存在");
        }
        PurchaseOrderVO vo = toVO(po);
        LambdaQueryWrapper<PurchaseOrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PurchaseOrderDetail::getPoId, id);
        List<PurchaseOrderDetail> details = detailMapper.selectList(detailWrapper);
        vo.setDetails(details.stream().map(this::detailToVO).collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PurchaseOrderDTO dto) {
        PurchaseOrder po = new PurchaseOrder();
        BeanUtils.copyProperties(dto, po);
        this.save(po);
        if (!CollectionUtils.isEmpty(dto.getDetails())) {
            for (PurchaseOrderDetailDTO detailDTO : dto.getDetails()) {
                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                BeanUtils.copyProperties(detailDTO, detail);
                detail.setPoId(po.getId());
                detail.setReceivedQty(java.math.BigDecimal.ZERO);
                if (detail.getQuantity() != null && detail.getUnitPrice() != null) {
                    detail.setAmount(detail.getQuantity().multiply(detail.getUnitPrice()));
                }
                detailMapper.insert(detail);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseOrderDTO dto) {
        PurchaseOrder po = new PurchaseOrder();
        BeanUtils.copyProperties(dto, po);
        this.updateById(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        LambdaQueryWrapper<PurchaseOrderDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PurchaseOrderDetail::getPoId, id);
        detailMapper.delete(detailWrapper);
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        PurchaseOrder po = this.getById(id);
        if (po == null) {
            throw new BusinessException("采购订单不存在");
        }
        po.setStatus(1);
        this.updateById(po);
    }

    private PurchaseOrderVO toVO(PurchaseOrder po) {
        PurchaseOrderVO vo = new PurchaseOrderVO();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }

    private PurchaseOrderDetailVO detailToVO(PurchaseOrderDetail detail) {
        PurchaseOrderDetailVO vo = new PurchaseOrderDetailVO();
        BeanUtils.copyProperties(detail, vo);
        return vo;
    }
}
