package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.TransferOrder;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.TransferOrderMapper;
import com.erp.modules.inventory.service.ITransferOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 调拨单 服务实现类
 *
 * @author ERP
 */
@Service
public class TransferOrderServiceImpl extends ServiceImpl<TransferOrderMapper, TransferOrder> implements ITransferOrderService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<TransferOrder> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(TransferOrder::getTransferNo, dto.getKeyword()));
            }
            if (dto.getStatus() != null) {
                wrapper.eq(TransferOrder::getStatus, dto.getStatus());
            }
        }
        wrapper.orderByDesc(TransferOrder::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<TransferOrder> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        TransferOrder entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("调拨单记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(TransferOrder entity) {
        if (!save(entity)) {
            throw new BusinessException("新增调拨单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(TransferOrder entity) {
        TransferOrder existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("调拨单记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新调拨单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除调拨单记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
