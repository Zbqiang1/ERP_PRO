package com.erp.modules.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.hr.dto.AttendanceDTO;
import com.erp.modules.hr.dto.AttendanceQueryDTO;
import com.erp.modules.hr.entity.Attendance;
import com.erp.modules.hr.mapper.AttendanceMapper;
import com.erp.modules.hr.service.IAttendanceService;
import com.erp.modules.hr.vo.AttendanceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 考勤 服务实现类
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Override
    public Page<AttendanceVO> queryPage(AttendanceQueryDTO queryDTO) {
        Page<Attendance> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        Integer statusInt = null;
        if (queryDTO.getStatus() != null && !queryDTO.getStatus().isEmpty()) {
            try { statusInt = Integer.valueOf(queryDTO.getStatus()); } catch (NumberFormatException e) { }
        }
        wrapper.eq(statusInt != null, Attendance::getAttendanceType, statusInt);
        wrapper.orderByDesc(Attendance::getAttendanceDate);
        Page<Attendance> resultPage = this.page(page, wrapper);
        Page<AttendanceVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public AttendanceVO getDetailById(Long id) {
        Attendance attendance = this.getById(id);
        if (attendance == null) {
            throw new BusinessException("考勤记录不存在");
        }
        return toVO(attendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AttendanceDTO dto) {
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(dto, attendance);
        this.save(attendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AttendanceDTO dto) {
        Attendance existing = this.getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("考勤记录不存在");
        }
        BeanUtils.copyProperties(dto, existing);
        this.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!this.removeById(id)) {
            throw new BusinessException("考勤记录不存在");
        }
    }

    private AttendanceVO toVO(Attendance attendance) {
        AttendanceVO vo = new AttendanceVO();
        BeanUtils.copyProperties(attendance, vo);
        return vo;
    }
}
