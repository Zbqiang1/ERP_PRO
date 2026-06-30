package com.erp.modules.hr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.hr.entity.LeaveSheet;
import com.erp.modules.hr.dto.LeaveSheetDTO;
import com.erp.modules.hr.dto.LeaveSheetQueryDTO;
import com.erp.modules.hr.vo.LeaveSheetVO;

/**
 * 请假单 服务接口
 */
public interface ILeaveSheetService extends IService<LeaveSheet> {

    /**
     * 分页查询请假单
     */
    Page<LeaveSheetVO> queryPage(LeaveSheetQueryDTO queryDTO);

    /**
     * 根据ID查询请假单
     */
    LeaveSheetVO getDetailById(Long id);

    /**
     * 新增请假单
     */
    void create(LeaveSheetDTO dto);

    /**
     * 编辑请假单
     */
    void update(LeaveSheetDTO dto);

    /**
     * 删除请假单
     */
    void delete(Long id);

    /**
     * 审批通过
     */
    void approve(Long id, String approver);

    /**
     * 驳回请假申请
     */
    void reject(Long id, String approver);
}
