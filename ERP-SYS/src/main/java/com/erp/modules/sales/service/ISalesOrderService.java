package com.erp.modules.sales.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.sales.entity.SalesOrder;
import com.erp.modules.sales.dto.SalesOrderDTO;
import com.erp.modules.sales.dto.SalesOrderQueryDTO;
import com.erp.modules.sales.vo.SalesOrderVO;

/**
 * 销售订单 服务接口
 */
public interface ISalesOrderService extends IService<SalesOrder> {

    /**
     * 分页查询销售订单
     */
    Page<SalesOrderVO> queryPage(SalesOrderQueryDTO queryDTO);

    /**
     * 根据ID查询销售订单（含明细）
     */
    SalesOrderVO getDetailById(Long id);

    /**
     * 新增销售订单
     */
    void create(SalesOrderDTO dto);

    /**
     * 编辑销售订单
     */
    void update(SalesOrderDTO dto);

    /**
     * 删除销售订单
     */
    void delete(Long id);

    /**
     * 确认销售订单
     */
    void confirm(Long id);
}
