package com.erp.modules.hr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.hr.entity.Attendance;
import com.erp.modules.hr.dto.AttendanceDTO;
import com.erp.modules.hr.dto.AttendanceQueryDTO;
import com.erp.modules.hr.vo.AttendanceVO;

/**
 * 考勤 服务接口
 */
public interface IAttendanceService extends IService<Attendance> {

    /**
     * 分页查询考勤
     */
    Page<AttendanceVO> queryPage(AttendanceQueryDTO queryDTO);

    /**
     * 根据ID查询考勤
     */
    AttendanceVO getDetailById(Long id);

    /**
     * 新增考勤
     */
    void create(AttendanceDTO dto);

    /**
     * 编辑考勤
     */
    void update(AttendanceDTO dto);

    /**
     * 删除考勤
     */
    void delete(Long id);
}
