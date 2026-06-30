package com.erp.modules.hr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.hr.entity.Performance;
import com.erp.modules.hr.dto.PerformanceDTO;
import com.erp.modules.hr.dto.PerformanceQueryDTO;
import com.erp.modules.hr.vo.PerformanceVO;

/**
 * 绩效考核 服务接口
 */
public interface IPerformanceService extends IService<Performance> {

    /**
     * 分页查询绩效考核
     */
    Page<PerformanceVO> queryPage(PerformanceQueryDTO queryDTO);

    /**
     * 根据ID查询绩效考核
     */
    PerformanceVO getDetailById(Long id);

    /**
     * 新增绩效考核
     */
    void create(PerformanceDTO dto);

    /**
     * 编辑绩效考核
     */
    void update(PerformanceDTO dto);

    /**
     * 删除绩效考核
     */
    void delete(Long id);
}
