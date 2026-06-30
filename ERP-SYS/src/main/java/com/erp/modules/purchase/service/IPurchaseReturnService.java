package com.erp.modules.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.purchase.entity.PurchaseReturn;
import com.erp.modules.purchase.dto.PurchaseReturnDTO;
import com.erp.modules.purchase.dto.PurchaseReturnQueryDTO;
import com.erp.modules.purchase.vo.PurchaseReturnVO;

/**
 * 采购退货 服务接口
 */
public interface IPurchaseReturnService extends IService<PurchaseReturn> {

    /**
     * 分页查询采购退货
     */
    Page<PurchaseReturnVO> queryPage(PurchaseReturnQueryDTO queryDTO);

    /**
     * 根据ID查询采购退货
     */
    PurchaseReturnVO getDetailById(Long id);

    /**
     * 新增采购退货
     */
    void create(PurchaseReturnDTO dto);

    /**
     * 编辑采购退货
     */
    void update(PurchaseReturnDTO dto);

    /**
     * 删除采购退货
     */
    void delete(Long id);
}
