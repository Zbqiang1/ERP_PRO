package com.erp.modules.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.hr.dto.LeaveSheetDTO;
import com.erp.modules.hr.dto.LeaveSheetQueryDTO;
import com.erp.modules.hr.entity.LeaveSheet;
import com.erp.modules.hr.mapper.LeaveSheetMapper;
import com.erp.modules.hr.service.ILeaveSheetService;
import com.erp.modules.hr.vo.LeaveSheetVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * 请假单 服务实现类
 */
@Service
public class LeaveSheetServiceImpl extends ServiceImpl<LeaveSheetMapper, LeaveSheet> implements ILeaveSheetService {

    @Override
    public Page<LeaveSheetVO> queryPage(LeaveSheetQueryDTO queryDTO) {
        Page<LeaveSheet> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<LeaveSheet> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getLeaveNo()), LeaveSheet::getLeaveNo, queryDTO.getLeaveNo());
        wrapper.eq(queryDTO.getLeaveType() != null, LeaveSheet::getLeaveType, queryDTO.getLeaveType());
        wrapper.eq(queryDTO.getStatus() != null, LeaveSheet::getStatus, queryDTO.getStatus());
        wrapper.orderByDesc(LeaveSheet::getCreateTime);
        Page<LeaveSheet> resultPage = this.page(page, wrapper);
        Page<LeaveSheetVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public LeaveSheetVO getDetailById(Long id) {
        LeaveSheet leave = this.getById(id);
        if (leave == null) {
            throw new BusinessException("请假单不存在");
        }
        return toVO(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(LeaveSheetDTO dto) {
        LeaveSheet leave = new LeaveSheet();
        BeanUtils.copyProperties(dto, leave);
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            long hours = Duration.between(dto.getStartTime(), dto.getEndTime()).toHours();
            leave.setTotalHours(java.math.BigDecimal.valueOf(hours));
        }
        leave.setStatus(0);
        this.save(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LeaveSheetDTO dto) {
        LeaveSheet leave = new LeaveSheet();
        BeanUtils.copyProperties(dto, leave);
        this.updateById(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long id, String approver) {
        LeaveSheet leave = this.getById(id);
        if (leave == null) {
            throw new BusinessException("请假单不存在");
        }
        leave.setStatus(1);
        leave.setApproverId(approver);
        leave.setApproveTime(LocalDateTime.now());
        this.updateById(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long id, String approver) {
        LeaveSheet leave = this.getById(id);
        if (leave == null) {
            throw new BusinessException("请假单不存在");
        }
        leave.setStatus(2);
        leave.setApproverId(approver);
        leave.setApproveTime(LocalDateTime.now());
        this.updateById(leave);
    }

    private LeaveSheetVO toVO(LeaveSheet leave) {
        LeaveSheetVO vo = new LeaveSheetVO();
        BeanUtils.copyProperties(leave, vo);
        return vo;
    }
}
