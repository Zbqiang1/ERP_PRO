package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.StockInOrder;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.StockInOrderMapper;
import com.erp.modules.inventory.service.IStockInOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 入库单 服务实现类
 *
 * @author ERP
 */
@Service
public class StockInOrderServiceImpl extends ServiceImpl<StockInOrderMapper, StockInOrder> implements IStockInOrderService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<StockInOrder> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(StockInOrder::getInNo, dto.getKeyword()));
            }
            if (dto.getStatus() != null) {
                wrapper.eq(StockInOrder::getStatus, dto.getStatus());
            }
            if (StringUtils.hasText(dto.getStartDate())) {
                wrapper.ge(StockInOrder::getCreateTime, dto.getStartDate());
            }
            if (StringUtils.hasText(dto.getEndDate())) {
                wrapper.le(StockInOrder::getCreateTime, dto.getEndDate());
            }
        }
        wrapper.orderByDesc(StockInOrder::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<StockInOrder> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        StockInOrder entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("入库单记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(StockInOrder entity) {
        if (!save(entity)) {
            throw new BusinessException("新增入库单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(StockInOrder entity) {
        StockInOrder existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("入库单记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新入库单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除入库单记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
