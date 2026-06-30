package com.erp.modules.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.purchase.dto.IqcInspectionDTO;
import com.erp.modules.purchase.dto.IqcInspectionQueryDTO;
import com.erp.modules.purchase.entity.IqcInspection;
import com.erp.modules.purchase.mapper.IqcInspectionMapper;
import com.erp.modules.purchase.service.IIqcInspectionService;
import com.erp.modules.purchase.vo.IqcInspectionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 来料检验 服务实现类
 */
@Service
public class IqcInspectionServiceImpl extends ServiceImpl<IqcInspectionMapper, IqcInspection> implements IIqcInspectionService {

    @Override
    public Page<IqcInspectionVO> queryPage(IqcInspectionQueryDTO queryDTO) {
        Page<IqcInspection> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<IqcInspection> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getInspectionNo()), IqcInspection::getInspectionNo, queryDTO.getInspectionNo());
        wrapper.eq(queryDTO.getPoId() != null, IqcInspection::getPoId, queryDTO.getPoId());
        wrapper.eq(queryDTO.getInspectionResult() != null, IqcInspection::getInspectionResult, queryDTO.getInspectionResult());
        wrapper.orderByDesc(IqcInspection::getCreateTime);
        Page<IqcInspection> resultPage = this.page(page, wrapper);
        Page<IqcInspectionVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public IqcInspectionVO getDetailById(Long id) {
        IqcInspection inspection = this.getById(id);
        if (inspection == null) {
            throw new BusinessException("来料检验记录不存在");
        }
        return toVO(inspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(IqcInspectionDTO dto) {
        IqcInspection inspection = new IqcInspection();
        BeanUtils.copyProperties(dto, inspection);
        this.save(inspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(IqcInspectionDTO dto) {
        IqcInspection inspection = new IqcInspection();
        BeanUtils.copyProperties(dto, inspection);
        this.updateById(inspection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    private IqcInspectionVO toVO(IqcInspection inspection) {
        IqcInspectionVO vo = new IqcInspectionVO();
        BeanUtils.copyProperties(inspection, vo);
        return vo;
    }
}
