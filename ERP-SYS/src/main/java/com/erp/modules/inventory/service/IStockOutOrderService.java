package com.erp.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.result.Result;
import com.erp.modules.inventory.StockOutOrder;
import com.erp.modules.inventory.dto.InventoryQueryDTO;

/**
 * 出库单 服务接口
 *
 * @author ERP
 */
public interface IStockOutOrderService extends IService<StockOutOrder> {

    /**
     * 分页查询
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    Result pageQuery(InventoryQueryDTO dto);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 出库单记录
     */
    Result getById(Long id);

    /**
     * 新增
     *
     * @param entity 出库单实体
     * @return 操作结果
     */
    Result add(StockOutOrder entity);

    /**
     * 修改
     *
     * @param entity 出库单实体
     * @return 操作结果
     */
    Result update(StockOutOrder entity);

    /**
     * 删除
     *
     * @param id 主键ID
     * @return 操作结果
     */
    Result delete(Long id);
}
