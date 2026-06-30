package com.erp.modules.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.common.result.Result;
import com.erp.modules.finance.entity.Payable;
import com.erp.modules.finance.dto.FinanceQueryDTO;

/**
 * 应付款 服务接口
 *
 * @author ERP
 */
public interface IPayableService extends IService<Payable> {

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
     * @return 应付款记录
     */
    Result getById(Long id);

    /**
     * 新增
     *
     * @param entity 应付款实体
     * @return 操作结果
     */
    Result add(Payable entity);

    /**
     * 修改
     *
     * @param entity 应付款实体
     * @return 操作结果
     */
    Result update(Payable entity);

    /**
     * 删除
     *
     * @param id 主键ID
     * @return 操作结果
     */
    Result delete(Long id);
}
