package com.erp.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.purchase.dto.PurchaseReturnDTO;
import com.erp.modules.purchase.dto.PurchaseReturnQueryDTO;
import com.erp.modules.purchase.entity.PurchaseReturn;
import com.erp.modules.purchase.mapper.PurchaseReturnMapper;
import com.erp.modules.purchase.service.IPurchaseReturnService;
import com.erp.modules.purchase.vo.PurchaseReturnVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 采购退货 服务实现类
 */
@Service
public class PurchaseReturnServiceImpl extends ServiceImpl<PurchaseReturnMapper, PurchaseReturn> implements IPurchaseReturnService {

    @Override
    public Page<PurchaseReturnVO> queryPage(PurchaseReturnQueryDTO queryDTO) {
        Page<PurchaseReturn> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<PurchaseReturn> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getReturnNo()), PurchaseReturn::getReturnNo, queryDTO.getReturnNo());
        wrapper.like(StringUtils.hasText(queryDTO.getSupplierName()), PurchaseReturn::getSupplierName, queryDTO.getSupplierName());
        Integer statusInt = null;
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            try { statusInt = Integer.valueOf(queryDTO.getStatus()); } catch (NumberFormatException e) { }
        }
        wrapper.eq(statusInt != null, PurchaseReturn::getStatus, statusInt);
        wrapper.orderByDesc(PurchaseReturn::getCreateTime);
        Page<PurchaseReturn> resultPage = this.page(page, wrapper);
        Page<PurchaseReturnVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public PurchaseReturnVO getDetailById(Long id) {
        PurchaseReturn purchaseReturn = this.getById(id);
        if (purchaseReturn == null) {
            throw new BusinessException("采购退货记录不存在");
        }
        return toVO(purchaseReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PurchaseReturnDTO dto) {
        PurchaseReturn purchaseReturn = new PurchaseReturn();
        BeanUtils.copyProperties(dto, purchaseReturn);
        this.save(purchaseReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchaseReturnDTO dto) {
        PurchaseReturn purchaseReturn = new PurchaseReturn();
        BeanUtils.copyProperties(dto, purchaseReturn);
        this.updateById(purchaseReturn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    private PurchaseReturnVO toVO(PurchaseReturn purchaseReturn) {
        PurchaseReturnVO vo = new PurchaseReturnVO();
        BeanUtils.copyProperties(purchaseReturn, vo);
        return vo;
    }
}
