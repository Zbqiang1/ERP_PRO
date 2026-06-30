package com.erp.modules.hr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.hr.dto.LeaveSheetDTO;
import com.erp.modules.hr.dto.LeaveSheetQueryDTO;
import com.erp.modules.hr.service.ILeaveSheetService;
import com.erp.modules.hr.vo.LeaveSheetVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 请假单 控制器
 */
@RestController
@RequestMapping("/api/hr/leaves")
public class LeaveSheetController {

    private final ILeaveSheetService leaveSheetService;

    public LeaveSheetController(ILeaveSheetService leaveSheetService) {
        this.leaveSheetService = leaveSheetService;
    }

    /**
     * 分页查询请假单
     */
    @GetMapping
    public Result<Page<LeaveSheetVO>> list(LeaveSheetQueryDTO queryDTO) {
        return Result.ok(leaveSheetService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询请假单
     */
    @GetMapping("/{id}")
    public Result<LeaveSheetVO> getById(@PathVariable Long id) {
        return Result.ok(leaveSheetService.getDetailById(id));
    }

    /**
     * 新增请假单
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody LeaveSheetDTO dto) {
        leaveSheetService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑请假单
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody LeaveSheetDTO dto) {
        leaveSheetService.update(dto);
        return Result.ok();
    }

    /**
     * 删除请假单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        leaveSheetService.delete(id);
        return Result.ok();
    }

    /**
     * 审批通过
     */
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id, @RequestParam String approver) {
        leaveSheetService.approve(id, approver);
        return Result.ok();
    }

    /**
     * 驳回请假申请
     */
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id, @RequestParam String approver) {
        leaveSheetService.reject(id, approver);
        return Result.ok();
    }
}
