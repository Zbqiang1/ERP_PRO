package com.erp.modules.hr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.hr.dto.AttendanceDTO;
import com.erp.modules.hr.dto.AttendanceQueryDTO;
import com.erp.modules.hr.service.IAttendanceService;
import com.erp.modules.hr.vo.AttendanceVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 考勤 控制器
 */
@RestController
@RequestMapping("/api/hr/attendances")
public class AttendanceController {

    private final IAttendanceService attendanceService;

    public AttendanceController(IAttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * 分页查询考勤
     */
    @GetMapping
    public Result<Page<AttendanceVO>> list(AttendanceQueryDTO queryDTO) {
        return Result.ok(attendanceService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询考勤
     */
    @GetMapping("/{id}")
    public Result<AttendanceVO> getById(@PathVariable Long id) {
        return Result.ok(attendanceService.getDetailById(id));
    }

    /**
     * 新增考勤
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody AttendanceDTO dto) {
        attendanceService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑考勤
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody AttendanceDTO dto) {
        dto.setId(id);
        attendanceService.update(dto);
        return Result.ok();
    }

    /**
     * 删除考勤
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        attendanceService.delete(id);
        return Result.ok();
    }
}
