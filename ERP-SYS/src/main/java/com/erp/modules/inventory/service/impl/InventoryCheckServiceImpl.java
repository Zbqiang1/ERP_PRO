package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.InventoryCheck;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.InventoryCheckMapper;
import com.erp.modules.inventory.service.IInventoryCheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 盘点单 服务实现类
 *
 * @author ERP
 */
@Service
public class InventoryCheckServiceImpl extends ServiceImpl<InventoryCheckMapper, InventoryCheck> implements IInventoryCheckService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<InventoryCheck> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(InventoryCheck::getCheckNo, dto.getKeyword()));
            }
            if (dto.getStatus() != null) {
                wrapper.eq(InventoryCheck::getStatus, dto.getStatus());
            }
            if (StringUtils.hasText(dto.getStartDate())) {
                wrapper.ge(InventoryCheck::getCheckDate, dto.getStartDate());
            }
            if (StringUtils.hasText(dto.getEndDate())) {
                wrapper.le(InventoryCheck::getCheckDate, dto.getEndDate());
            }
        }
        wrapper.orderByDesc(InventoryCheck::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<InventoryCheck> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        InventoryCheck entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("盘点单记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(InventoryCheck entity) {
        if (!save(entity)) {
            throw new BusinessException("新增盘点单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(InventoryCheck entity) {
        InventoryCheck existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("盘点单记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新盘点单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除盘点单记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
