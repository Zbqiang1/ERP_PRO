package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.StockInDetail;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.StockInDetailMapper;
import com.erp.modules.inventory.service.IStockInDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 入库明细 服务实现类
 *
 * @author ERP
 */
@Service
public class StockInDetailServiceImpl extends ServiceImpl<StockInDetailMapper, StockInDetail> implements IStockInDetailService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<StockInDetail> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(StockInDetail::getBatchNo, dto.getKeyword()));
            }
        }
        // 明细表无 createTime 字段，按 id 降序排列
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<StockInDetail> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        StockInDetail entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("入库明细记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(StockInDetail entity) {
        if (!save(entity)) {
            throw new BusinessException("新增入库明细记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(StockInDetail entity) {
        StockInDetail existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("入库明细记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新入库明细记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除入库明细记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
