package com.erp.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.purchase.dto.SupplierDTO;
import com.erp.modules.purchase.dto.SupplierQueryDTO;
import com.erp.modules.purchase.entity.Supplier;
import com.erp.modules.purchase.mapper.SupplierMapper;
import com.erp.modules.purchase.service.ISupplierService;
import com.erp.modules.purchase.vo.SupplierVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 供应商 服务实现类
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Override
    public Page<SupplierVO> queryPage(SupplierQueryDTO queryDTO) {
        Page<Supplier> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getSupplierCode()), Supplier::getSupplierCode, queryDTO.getSupplierCode());
        wrapper.like(StringUtils.hasText(queryDTO.getSupplierName()), Supplier::getSupplierName, queryDTO.getSupplierName());
        Integer statusInt = null;
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            try { statusInt = Integer.valueOf(queryDTO.getStatus()); } catch (NumberFormatException e) { }
        }
        wrapper.eq(statusInt != null, Supplier::getStatus, statusInt);
        wrapper.orderByDesc(Supplier::getCreateTime);
        Page<Supplier> resultPage = this.page(page, wrapper);
        Page<SupplierVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(java.util.stream.Collectors.toList()));
        return voPage;
    }

    @Override
    public SupplierVO getDetailById(Long id) {
        Supplier supplier = this.getById(id);
        if (supplier == null) {
            throw new BusinessException("供应商不存在");
        }
        return toVO(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(dto, supplier);
        this.save(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SupplierDTO dto) {
        // 实际使用中需从数据库获取已有记录再更新——这里假设dto中携带id字段
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(dto, supplier);
        this.updateById(supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Supplier supplier = this.getById(id);
        if (supplier == null) {
            throw new BusinessException("供应商不存在");
        }
        supplier.setStatus(status);
        this.updateById(supplier);
    }

    private SupplierVO toVO(Supplier supplier) {
        SupplierVO vo = new SupplierVO();
        BeanUtils.copyProperties(supplier, vo);
        return vo;
    }
}
