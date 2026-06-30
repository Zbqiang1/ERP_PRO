package com.erp.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.Payable;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.mapper.PayableMapper;
import com.erp.modules.finance.service.IPayableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 应付款 服务实现类
 *
 * @author ERP
 */
@Service
public class PayableServiceImpl extends ServiceImpl<PayableMapper, Payable> implements IPayableService {

    @Override
    public Result pageQuery(FinanceQueryDTO dto) {
        LambdaQueryWrapper<Payable> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(Payable::getPayableNo, dto.getKeyword())
                        .or().like(Payable::getSupplierName, dto.getKeyword()));
            }
            Integer statusInt = null;
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                try { statusInt = Integer.valueOf(dto.getStatus()); } catch (NumberFormatException e) { }
            }
            wrapper.eq(statusInt != null, Payable::getStatus, statusInt);
            if (StringUtils.hasText(dto.getStartDate())) {
                wrapper.ge(Payable::getDueDate, dto.getStartDate());
            }
            if (StringUtils.hasText(dto.getEndDate())) {
                wrapper.le(Payable::getDueDate, dto.getEndDate());
            }
        }
        wrapper.orderByDesc(Payable::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<Payable> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        Payable entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("应付款记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(Payable entity) {
        if (!save(entity)) {
            throw new BusinessException("新增应付款记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(Payable entity) {
        Payable existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("应付款记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新应付款记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除应付款记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
