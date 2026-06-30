package com.erp.modules.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.purchase.entity.PurchaseRequest;
import com.erp.modules.purchase.dto.PurchaseRequestDTO;
import com.erp.modules.purchase.dto.PurchaseRequestQueryDTO;
import com.erp.modules.purchase.vo.PurchaseRequestVO;

/**
 * 采购申请 服务接口
 */
public interface IPurchaseRequestService extends IService<PurchaseRequest> {

    /**
     * 分页查询采购申请
     */
    Page<PurchaseRequestVO> queryPage(PurchaseRequestQueryDTO queryDTO);

    /**
     * 根据ID查询采购申请（含明细）
     */
    PurchaseRequestVO getDetailById(Long id);

    /**
     * 新增采购申请
     */
    void create(PurchaseRequestDTO dto);

    /**
     * 编辑采购申请
     */
    void update(PurchaseRequestDTO dto);

    /**
     * 删除采购申请
     */
    void delete(Long id);

    /**
     * 审批采购申请
     */
    void approve(Long id);

    /**
     * 驳回采购申请
     */
    void reject(Long id);
}
