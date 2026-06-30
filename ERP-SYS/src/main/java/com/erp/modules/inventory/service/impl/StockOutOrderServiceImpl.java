package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.StockOutOrder;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.StockOutOrderMapper;
import com.erp.modules.inventory.service.IStockOutOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 出库单 服务实现类
 *
 * @author ERP
 */
@Service
public class StockOutOrderServiceImpl extends ServiceImpl<StockOutOrderMapper, StockOutOrder> implements IStockOutOrderService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<StockOutOrder> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(StockOutOrder::getOutNo, dto.getKeyword()));
            }
            if (dto.getStatus() != null) {
                wrapper.eq(StockOutOrder::getStatus, dto.getStatus());
            }
            if (StringUtils.hasText(dto.getStartDate())) {
                wrapper.ge(StockOutOrder::getCreateTime, dto.getStartDate());
            }
            if (StringUtils.hasText(dto.getEndDate())) {
                wrapper.le(StockOutOrder::getCreateTime, dto.getEndDate());
            }
        }
        wrapper.orderByDesc(StockOutOrder::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<StockOutOrder> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        StockOutOrder entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("出库单记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(StockOutOrder entity) {
        if (!save(entity)) {
            throw new BusinessException("新增出库单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(StockOutOrder entity) {
        StockOutOrder existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("出库单记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新出库单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除出库单记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
