package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.Location;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.LocationMapper;
import com.erp.modules.inventory.service.ILocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 库位 服务实现类
 *
 * @author ERP
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements ILocationService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<Location> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(Location::getLocationCode, dto.getKeyword())
                        .or().like(Location::getLocationName, dto.getKeyword()));
            }
        }
        wrapper.orderByDesc(Location::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<Location> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        Location entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("库位记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(Location entity) {
        if (!save(entity)) {
            throw new BusinessException("新增库位记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(Location entity) {
        Location existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("库位记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新库位记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除库位记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
