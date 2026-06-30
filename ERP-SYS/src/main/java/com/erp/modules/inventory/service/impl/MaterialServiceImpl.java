package com.erp.modules.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.inventory.Material;
import com.erp.modules.inventory.dto.InventoryQueryDTO;
import com.erp.modules.inventory.mapper.MaterialMapper;
import com.erp.modules.inventory.service.IMaterialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 物料 服务实现类
 *
 * @author ERP
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements IMaterialService {

    @Override
    public Result pageQuery(InventoryQueryDTO dto) {
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(Material::getMaterialCode, dto.getKeyword())
                        .or().like(Material::getMaterialName, dto.getKeyword())
                        .or().like(Material::getCategoryName, dto.getKeyword())
                        .or().like(Material::getSpec, dto.getKeyword()));
            }
            Integer statusInt = null;
            if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                try { statusInt = Integer.valueOf(dto.getStatus()); } catch (NumberFormatException e) { }
            }
            wrapper.eq(statusInt != null, Material::getStatus, statusInt);
        }
        wrapper.orderByDesc(Material::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<Material> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        Material entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("物料记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(Material entity) {
        if (!save(entity)) {
            throw new BusinessException("新增物料记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(Material entity) {
        Material existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("物料记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新物料记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除物料记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
