package com.erp.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.CostSheet;
import com.erp.modules.finance.dto.FinanceQueryDTO;

/**
 * 成本核算单 服务接口
 *
 * @author ERP
 */
public interface ICostSheetService extends IService<CostSheet> {

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
     * @return 成本核算单记录
     */
    Result getById(Long id);

    /**
     * 新增
     *
     * @param entity 成本核算单实体
     * @return 操作结果
     */
    Result add(CostSheet entity);

    /**
     * 修改
     *
     * @param entity 成本核算单实体
     * @return 操作结果
     */
    Result update(CostSheet entity);

    /**
     * 删除
     *
     * @param id 主键ID
     * @return 操作结果
     */
    Result delete(Long id);
}
