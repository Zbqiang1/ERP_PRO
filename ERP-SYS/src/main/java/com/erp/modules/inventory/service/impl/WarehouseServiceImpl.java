package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.Warehouse;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.WarehouseMapper;
import com.erp.modules.inventory.service.IWarehouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 仓库 服务实现类
 *
 * @author ERP
 */
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<Warehouse> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(Warehouse::getWarehouseCode, dto.getKeyword())
                        .or().like(Warehouse::getWarehouseName, dto.getKeyword()));
            }
            Integer statusInt = null;
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                try { statusInt = Integer.valueOf(dto.getStatus()); } catch (NumberFormatException e) { }
            }
            wrapper.eq(statusInt != null, Warehouse::getStatus, statusInt);
        }
        wrapper.orderByDesc(Warehouse::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<Warehouse> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        Warehouse entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("仓库记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(Warehouse entity) {
        if (!save(entity)) {
            throw new BusinessException("新增仓库记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(Warehouse entity) {
        Warehouse existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("仓库记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新仓库记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除仓库记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
