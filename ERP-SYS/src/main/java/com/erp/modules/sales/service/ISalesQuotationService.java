package com.erp.modules.sales.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.sales.entity.SalesQuotation;
import com.erp.modules.sales.dto.SalesQuotationDTO;
import com.erp.modules.sales.dto.SalesQuotationQueryDTO;
import com.erp.modules.sales.vo.SalesQuotationVO;

/**
 * 销售报价 服务接口
 */
public interface ISalesQuotationService extends IService<SalesQuotation> {

    /**
     * 分页查询销售报价
     */
    Page<SalesQuotationVO> queryPage(SalesQuotationQueryDTO queryDTO);

    /**
     * 根据ID查询销售报价
     */
    SalesQuotationVO getDetailById(Long id);

    /**
     * 新增销售报价
     */
    void create(SalesQuotationDTO dto);

    /**
     * 编辑销售报价
     */
    void update(SalesQuotationDTO dto);

    /**
     * 删除销售报价
     */
    void delete(Long id);

    /**
     * 确认报价
     */
    void confirm(Long id);
}
