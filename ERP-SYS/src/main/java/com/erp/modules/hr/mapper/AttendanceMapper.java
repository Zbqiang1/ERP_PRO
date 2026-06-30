package com.erp.modules.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.hr.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考勤 Mapper 接口
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
}
