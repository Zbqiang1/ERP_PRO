package com.erp.modules.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.purchase.entity.PurchasePrice;
import com.erp.modules.purchase.dto.PurchasePriceDTO;
import com.erp.modules.purchase.dto.PurchasePriceQueryDTO;
import com.erp.modules.purchase.vo.PurchasePriceVO;

/**
 * 采购价格 服务接口
 */
public interface IPurchasePriceService extends IService<PurchasePrice> {

    /**
     * 分页查询采购价格
     */
    Page<PurchasePriceVO> queryPage(PurchasePriceQueryDTO queryDTO);

    /**
     * 根据ID查询采购价格
     */
    PurchasePriceVO getDetailById(Long id);

    /**
     * 新增采购价格
     */
    void create(PurchasePriceDTO dto);

    /**
     * 编辑采购价格
     */
    void update(PurchasePriceDTO dto);

    /**
     * 删除采购价格
     */
    void delete(Long id);
}
