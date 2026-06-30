package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.StockOutDetail;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.StockOutDetailMapper;
import com.erp.modules.inventory.service.IStockOutDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 出库明细 服务实现类
 *
 * @author ERP
 */
@Service
public class StockOutDetailServiceImpl extends ServiceImpl<StockOutDetailMapper, StockOutDetail> implements IStockOutDetailService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<StockOutDetail> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(StockOutDetail::getBatchNo, dto.getKeyword()));
            }
        }
        // 明细表无 createTime 字段，按 id 降序排列
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<StockOutDetail> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        StockOutDetail entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("出库明细记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(StockOutDetail entity) {
        if (!save(entity)) {
            throw new BusinessException("新增出库明细记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(StockOutDetail entity) {
        StockOutDetail existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("出库明细记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新出库明细记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除出库明细记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
