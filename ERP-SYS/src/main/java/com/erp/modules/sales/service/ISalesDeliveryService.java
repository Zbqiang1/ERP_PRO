package com.erp.modules.sales.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.sales.entity.SalesDelivery;
import com.erp.modules.sales.dto.SalesDeliveryDTO;
import com.erp.modules.sales.dto.SalesDeliveryQueryDTO;
import com.erp.modules.sales.vo.SalesDeliveryVO;

/**
 * 销售发货 服务接口
 */
public interface ISalesDeliveryService extends IService<SalesDelivery> {

    /**
     * 分页查询销售发货
     */
    Page<SalesDeliveryVO> queryPage(SalesDeliveryQueryDTO queryDTO);

    /**
     * 根据ID查询销售发货
     */
    SalesDeliveryVO getDetailById(Long id);

    /**
     * 新增销售发货
     */
    void create(SalesDeliveryDTO dto);

    /**
     * 编辑销售发货
     */
    void update(SalesDeliveryDTO dto);

    /**
     * 删除销售发货
     */
    void delete(Long id);

    /**
     * 执行发货
     */
    void deliver(Long id);

    /**
     * 确认签收
     */
    void sign(Long id);
}
