package com.erp.modules.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.purchase.entity.PurchaseOrder;
import com.erp.modules.purchase.dto.PurchaseOrderDTO;
import com.erp.modules.purchase.dto.PurchaseOrderQueryDTO;
import com.erp.modules.purchase.vo.PurchaseOrderVO;

/**
 * 采购订单 服务接口
 */
public interface IPurchaseOrderService extends IService<PurchaseOrder> {

    /**
     * 分页查询采购订单
     */
    Page<PurchaseOrderVO> queryPage(PurchaseOrderQueryDTO queryDTO);

    /**
     * 根据ID查询采购订单（含明细）
     */
    PurchaseOrderVO getDetailById(Long id);

    /**
     * 新增采购订单
     */
    void create(PurchaseOrderDTO dto);

    /**
     * 编辑采购订单
     */
    void update(PurchaseOrderDTO dto);

    /**
     * 删除采购订单
     */
    void delete(Long id);

    /**
     * 确认采购订单
     */
    void confirm(Long id);
}
