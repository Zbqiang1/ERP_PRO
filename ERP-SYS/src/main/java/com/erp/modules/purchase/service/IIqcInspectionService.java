package com.erp.modules.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.purchase.entity.IqcInspection;
import com.erp.modules.purchase.dto.IqcInspectionDTO;
import com.erp.modules.purchase.dto.IqcInspectionQueryDTO;
import com.erp.modules.purchase.vo.IqcInspectionVO;

/**
 * 来料检验 服务接口
 */
public interface IIqcInspectionService extends IService<IqcInspection> {

    /**
     * 分页查询来料检验
     */
    Page<IqcInspectionVO> queryPage(IqcInspectionQueryDTO queryDTO);

    /**
     * 根据ID查询来料检验
     */
    IqcInspectionVO getDetailById(Long id);

    /**
     * 新增来料检验
     */
    void create(IqcInspectionDTO dto);

    /**
     * 编辑来料检验
     */
    void update(IqcInspectionDTO dto);

    /**
     * 删除来料检验
     */
    void delete(Long id);
}
