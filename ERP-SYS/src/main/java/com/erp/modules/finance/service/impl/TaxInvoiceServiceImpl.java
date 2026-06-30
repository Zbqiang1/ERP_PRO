package com.erp.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.TaxInvoice;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.mapper.TaxInvoiceMapper;
import com.erp.modules.finance.service.ITaxInvoiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 税务发票 服务实现类
 *
 * @author ERP
 */
@Service
public class TaxInvoiceServiceImpl extends ServiceImpl<TaxInvoiceMapper, TaxInvoice> implements ITaxInvoiceService {

    @Override
    public Result pageQuery(FinanceQueryDTO dto) {
        LambdaQueryWrapper<TaxInvoice> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(TaxInvoice::getInvoiceNo, dto.getKeyword())
                        .or().like(TaxInvoice::getBuyerName, dto.getKeyword())
                        .or().like(TaxInvoice::getSellerName, dto.getKeyword()));
            }
            Integer statusInt = null;
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                try { statusInt = Integer.valueOf(dto.getStatus()); } catch (NumberFormatException e) { }
            }
            wrapper.eq(statusInt != null, TaxInvoice::getStatus, statusInt);
            if (StringUtils.hasText(dto.getStartDate())) {
                wrapper.ge(TaxInvoice::getInvoiceDate, dto.getStartDate());
            }
            if (StringUtils.hasText(dto.getEndDate())) {
                wrapper.le(TaxInvoice::getInvoiceDate, dto.getEndDate());
            }
        }
        wrapper.orderByDesc(TaxInvoice::getInvoiceDate, TaxInvoice::getId);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<TaxInvoice> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        TaxInvoice entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("税务发票记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(TaxInvoice entity) {
        if (!save(entity)) {
            throw new BusinessException("新增税务发票记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(TaxInvoice entity) {
        TaxInvoice existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("税务发票记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新税务发票记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除税务发票记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
