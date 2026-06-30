package com.erp.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.FixedAsset;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.mapper.FixedAssetMapper;
import com.erp.modules.finance.service.IFixedAssetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 固定资产 服务实现类
 *
 * @author ERP
 */
@Service
public class FixedAssetServiceImpl extends ServiceImpl<FixedAssetMapper, FixedAsset> implements IFixedAssetService {

    @Override
    public Result pageQuery(FinanceQueryDTO dto) {
        LambdaQueryWrapper<FixedAsset> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(FixedAsset::getAssetNo, dto.getKeyword())
                        .or().like(FixedAsset::getAssetName, dto.getKeyword())
                        .or().like(FixedAsset::getCategory, dto.getKeyword()));
            }
            if (dto.getStatus() != null) {
                wrapper.eq(FixedAsset::getStatus, dto.getStatus());
            }
        }
        wrapper.orderByDesc(FixedAsset::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<FixedAsset> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        FixedAsset entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("固定资产记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(FixedAsset entity) {
        if (!save(entity)) {
            throw new BusinessException("新增固定资产记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(FixedAsset entity) {
        FixedAsset existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("固定资产记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新固定资产记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除固定资产记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
