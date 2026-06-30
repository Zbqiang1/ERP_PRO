package com.erp.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.FixedAsset;
import com.erp.modules.finance.dto.FinanceQueryDTO;

/**
 * 固定资产 服务接口
 *
 * @author ERP
 */
public interface IFixedAssetService extends IService<FixedAsset> {

    /**
     * 分页查询
     *
     * @param dto 查询条件
     * @return 分页结果
     */
    Result pageQuery(FinanceQueryDTO dto);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 固定资产记录
     */
    Result getById(Long id);

    /**
     * 新增
     *
     * @param entity 固定资产实体
     * @return 操作结果
     */
    Result add(FixedAsset entity);

    /**
     * 修改
     *
     * @param entity 固定资产实体
     * @return 操作结果
     */
    Result update(FixedAsset entity);

    /**
     * 删除
     *
     * @param id 主键ID
     * @return 操作结果
     */
    Result delete(Long id);
}
