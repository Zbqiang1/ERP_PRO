package com.erp.modules.hr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.hr.entity.SalarySheet;
import com.erp.modules.hr.dto.SalarySheetDTO;
import com.erp.modules.hr.dto.SalarySheetQueryDTO;
import com.erp.modules.hr.vo.SalarySheetVO;

/**
 * 工资单 服务接口
 */
public interface ISalarySheetService extends IService<SalarySheet> {

    /**
     * 分页查询工资单
     */
    Page<SalarySheetVO> queryPage(SalarySheetQueryDTO queryDTO);

    /**
     * 根据ID查询工资单
     */
    SalarySheetVO getDetailById(Long id);

    /**
     * 新增工资单
     */
    void create(SalarySheetDTO dto);

    /**
     * 编辑工资单
     */
    void update(SalarySheetDTO dto);

    /**
     * 删除工资单
     */
    void delete(Long id);
}
