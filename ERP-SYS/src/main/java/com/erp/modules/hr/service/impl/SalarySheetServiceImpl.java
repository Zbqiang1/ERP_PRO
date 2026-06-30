package com.erp.modules.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.hr.dto.SalarySheetDTO;
import com.erp.modules.hr.dto.SalarySheetQueryDTO;
import com.erp.modules.hr.entity.SalarySheet;
import com.erp.modules.hr.mapper.SalarySheetMapper;
import com.erp.modules.hr.service.ISalarySheetService;
import com.erp.modules.hr.vo.SalarySheetVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * 工资单 服务实现类
 */
@Service
public class SalarySheetServiceImpl extends ServiceImpl<SalarySheetMapper, SalarySheet> implements ISalarySheetService {

    @Override
    public Page<SalarySheetVO> queryPage(SalarySheetQueryDTO queryDTO) {
        Page<SalarySheet> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<SalarySheet> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getSalaryNo()), SalarySheet::getSalaryNo, queryDTO.getSalaryNo());
        wrapper.eq(StringUtils.hasText(queryDTO.getSalaryMonth()), SalarySheet::getSalaryMonth, queryDTO.getSalaryMonth());
        wrapper.orderByDesc(SalarySheet::getCreateTime);
        Page<SalarySheet> resultPage = this.page(page, wrapper);
        Page<SalarySheetVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public SalarySheetVO getDetailById(Long id) {
        SalarySheet salary = this.getById(id);
        if (salary == null) {
            throw new BusinessException("工资单不存在");
        }
        return toVO(salary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SalarySheetDTO dto) {
        SalarySheet salary = new SalarySheet();
        BeanUtils.copyProperties(dto, salary);
        // 计算实发工资
        BigDecimal netSalary = calNetSalary(salary);
        salary.setNetSalary(netSalary);
        this.save(salary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SalarySheetDTO dto) {
        SalarySheet existing = this.getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("工资单不存在");
        }
        BeanUtils.copyProperties(dto, existing);
        BigDecimal netSalary = calNetSalary(existing);
        existing.setNetSalary(netSalary);
        this.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!this.removeById(id)) {
            throw new BusinessException("工资单不存在");
        }
    }

    /**
     * 计算实发工资 = 基本工资 + 绩效奖金 + 加班工资 - 扣款 - 社保 - 个税
     */
    private BigDecimal calNetSalary(SalarySheet salary) {
        BigDecimal basicSalary = salary.getBasicSalary() != null ? salary.getBasicSalary() : BigDecimal.ZERO;
        BigDecimal performanceBonus = salary.getPerformanceBonus() != null ? salary.getPerformanceBonus() : BigDecimal.ZERO;
        BigDecimal overtimePay = salary.getOvertimePay() != null ? salary.getOvertimePay() : BigDecimal.ZERO;
        BigDecimal deduction = salary.getDeduction() != null ? salary.getDeduction() : BigDecimal.ZERO;
        BigDecimal socialInsurance = salary.getSocialInsurance() != null ? salary.getSocialInsurance() : BigDecimal.ZERO;
        BigDecimal tax = salary.getTax() != null ? salary.getTax() : BigDecimal.ZERO;
        return basicSalary.add(performanceBonus).add(overtimePay)
                .subtract(deduction).subtract(socialInsurance).subtract(tax);
    }

    private SalarySheetVO toVO(SalarySheet salary) {
        SalarySheetVO vo = new SalarySheetVO();
        BeanUtils.copyProperties(salary, vo);
        return vo;
    }
}
