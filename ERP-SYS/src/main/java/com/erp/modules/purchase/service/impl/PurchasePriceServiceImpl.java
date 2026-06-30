package com.erp.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.purchase.dto.PurchasePriceDTO;
import com.erp.modules.purchase.dto.PurchasePriceQueryDTO;
import com.erp.modules.purchase.entity.PurchasePrice;
import com.erp.modules.purchase.mapper.PurchasePriceMapper;
import com.erp.modules.purchase.service.IPurchasePriceService;
import com.erp.modules.purchase.vo.PurchasePriceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 采购价格 服务实现类
 */
@Service
public class PurchasePriceServiceImpl extends ServiceImpl<PurchasePriceMapper, PurchasePrice> implements IPurchasePriceService {

    @Override
    public Page<PurchasePriceVO> queryPage(PurchasePriceQueryDTO queryDTO) {
        Page<PurchasePrice> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<PurchasePrice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(PurchasePrice::getCreateTime);
        Page<PurchasePrice> resultPage = this.page(page, wrapper);
        Page<PurchasePriceVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public PurchasePriceVO getDetailById(Long id) {
        PurchasePrice price = this.getById(id);
        if (price == null) {
            throw new BusinessException("采购价格记录不存在");
        }
        return toVO(price);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PurchasePriceDTO dto) {
        PurchasePrice price = new PurchasePrice();
        BeanUtils.copyProperties(dto, price);
        this.save(price);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PurchasePriceDTO dto) {
        PurchasePrice price = new PurchasePrice();
        BeanUtils.copyProperties(dto, price);
        this.updateById(price);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    private PurchasePriceVO toVO(PurchasePrice price) {
        PurchasePriceVO vo = new PurchasePriceVO();
        BeanUtils.copyProperties(price, vo);
        return vo;
    }
}
