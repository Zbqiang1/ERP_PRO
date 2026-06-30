package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.StockAlert;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.StockAlertMapper;
import com.erp.modules.inventory.service.IStockAlertService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 库存预警 服务实现类
 *
 * @author ERP
 */
@Service
public class StockAlertServiceImpl extends ServiceImpl<StockAlertMapper, StockAlert> implements IStockAlertService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<StockAlert> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.like(StockAlert::getAlertNo, dto.getKeyword());
            }
            if (dto.getStatus() != null) {
                wrapper.eq(StockAlert::getStatus, dto.getStatus());
            }
            if (StringUtils.hasText(dto.getStartDate())) {
                wrapper.ge(StockAlert::getAlertDate, dto.getStartDate());
            }
            if (StringUtils.hasText(dto.getEndDate())) {
                wrapper.le(StockAlert::getAlertDate, dto.getEndDate());
            }
        }
        wrapper.orderByDesc(StockAlert::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<StockAlert> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        StockAlert entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("库存预警记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(StockAlert entity) {
        if (!save(entity)) {
            throw new BusinessException("新增库存预警记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(StockAlert entity) {
        StockAlert existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("库存预警记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新库存预警记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除库存预警记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
