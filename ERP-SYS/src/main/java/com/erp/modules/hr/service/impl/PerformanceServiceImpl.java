package com.erp.modules.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.hr.dto.PerformanceDTO;
import com.erp.modules.hr.dto.PerformanceQueryDTO;
import com.erp.modules.hr.entity.Performance;
import com.erp.modules.hr.mapper.PerformanceMapper;
import com.erp.modules.hr.service.IPerformanceService;
import com.erp.modules.hr.vo.PerformanceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 绩效考核 服务实现类
 */
@Service
public class PerformanceServiceImpl extends ServiceImpl<PerformanceMapper, Performance> implements IPerformanceService {

    @Override
    public Page<PerformanceVO> queryPage(PerformanceQueryDTO queryDTO) {
        Page<Performance> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<Performance> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getAssessmentPeriod()), Performance::getAssessmentPeriod, queryDTO.getAssessmentPeriod());
        wrapper.eq(StringUtils.hasText(queryDTO.getGrade()), Performance::getGrade, queryDTO.getGrade());
        wrapper.orderByDesc(Performance::getCreateTime);
        Page<Performance> resultPage = this.page(page, wrapper);
        Page<PerformanceVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public PerformanceVO getDetailById(Long id) {
        Performance performance = this.getById(id);
        if (performance == null) {
            throw new BusinessException("绩效考核记录不存在");
        }
        return toVO(performance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PerformanceDTO dto) {
        Performance performance = new Performance();
        BeanUtils.copyProperties(dto, performance);
        this.save(performance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PerformanceDTO dto) {
        Performance existing = this.getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("绩效考核记录不存在");
        }
        BeanUtils.copyProperties(dto, existing);
        this.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!this.removeById(id)) {
            throw new BusinessException("绩效考核记录不存在");
        }
    }

    private PerformanceVO toVO(Performance performance) {
        PerformanceVO vo = new PerformanceVO();
        BeanUtils.copyProperties(performance, vo);
        return vo;
    }
}
