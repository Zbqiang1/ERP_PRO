package com.erp.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.CostSheet;
import com.erp.modules.finance.dto.FinanceQueryDTO;
import com.erp.modules.finance.mapper.CostSheetMapper;
import com.erp.modules.finance.service.ICostSheetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 成本核算单 服务实现类
 *
 * @author ERP
 */
@Service
public class CostSheetServiceImpl extends ServiceImpl<CostSheetMapper, CostSheet> implements ICostSheetService {

    @Override
    public Result pageQuery(FinanceQueryDTO dto) {
        LambdaQueryWrapper<CostSheet> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            if (StringUtils.hasText(dto.getKeyword())) {
                wrapper.and(w -> w.like(CostSheet::getCostNo, dto.getKeyword())
                        .or().like(CostSheet::getProductName, dto.getKeyword())
                        .or().like(CostSheet::getCostPeriod, dto.getKeyword()));
            }
            if (StringUtils.hasText(dto.getStartDate()) || StringUtils.hasText(dto.getEndDate())) {
                if (StringUtils.hasText(dto.getStartDate())) {
                    wrapper.ge(CostSheet::getCostPeriod, dto.getStartDate());
                }
                if (StringUtils.hasText(dto.getEndDate())) {
                    wrapper.le(CostSheet::getCostPeriod, dto.getEndDate());
                }
            }
        }
        wrapper.orderByDesc(CostSheet::getCreateTime);
        int page = (dto != null && dto.getPage() != null) ? dto.getPage() : 1;
        int size = (dto != null && dto.getSize() != null) ? dto.getSize() : 10;
        Page<CostSheet> result = page(new Page<>(page, size), wrapper);
        return Result.ok(result);
    }

    @Override
    public Result getById(Long id) {
        CostSheet entity = baseMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException("成本核算单记录不存在");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(CostSheet entity) {
        if (!save(entity)) {
            throw new BusinessException("新增成本核算单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(CostSheet entity) {
        CostSheet existing = baseMapper.selectById(entity.getId());
        if (existing == null) {
            throw new BusinessException("成本核算单记录不存在");
        }
        if (!updateById(entity)) {
            throw new BusinessException("更新成本核算单记录失败");
        }
        return Result.ok(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        if (baseMapper.deleteById(id) <= 0) {
            throw new BusinessException("删除成本核算单记录失败或记录不存在");
        }
        return Result.ok(null);
    }
}
