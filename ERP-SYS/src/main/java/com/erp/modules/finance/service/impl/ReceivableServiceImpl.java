package com.erp.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.Receivable;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.mapper.ReceivableMapper;
import com.erp.modules.finance.service.IReceivableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 应收款 服务实现类
 *
 * @author ERP
 */
@Service
public class ReceivableServiceImpl extends ServiceImpl<ReceivableMapper, Receivable> implements IReceivableService {

    @Override
    public Result pageQuery(FinanceQueryDTO dto) {
        LambdaQueryWrapper<Receivable> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(Receivable::getReceivableNo, dto.getKeyword())
                        .or().like(Receivable::getCustomerName, dto.getKeyword()));
            }
            if (dto.getStatus() != null) {
                wrapper.eq(Receivable::getStatus, dto.getStatus());
            }
            if (StringUtils.hasText(dto.getStartDate())) {
                wrapper.ge(Receivable::getDueDate, dto.getStartDate());
            }
            if (StringUtils.hasText(dto.getEndDate())) {
                wrapper.le(Receivable::getDueDate, dto.getEndDate());
            }
        }
        wrapper.orderByDesc(Receivable::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<Receivable> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        Receivable entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("应收款记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(Receivable entity) {
        if (!save(entity)) {
            throw new BusinessException("新增应收款记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(Receivable entity) {
        Receivable existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("应收款记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新应收款记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除应收款记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
