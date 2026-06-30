package com.erp.modules.sales.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.sales.entity.SalesReturn;
import com.erp.modules.sales.dto.SalesReturnDTO;
import com.erp.modules.sales.dto.SalesReturnQueryDTO;
import com.erp.modules.sales.vo.SalesReturnVO;

/**
 * 销售退货 服务接口
 */
public interface ISalesReturnService extends IService<SalesReturn> {

    /**
     * 分页查询销售退货
     */
    Page<SalesReturnVO> queryPage(SalesReturnQueryDTO queryDTO);

    /**
     * 根据ID查询销售退货
     */
    SalesReturnVO getDetailById(Long id);

    /**
     * 新增销售退货
     */
    void create(SalesReturnDTO dto);

    /**
     * 编辑销售退货
     */
    void update(SalesReturnDTO dto);

    /**
     * 删除销售退货
     */
    void delete(Long id);
}
