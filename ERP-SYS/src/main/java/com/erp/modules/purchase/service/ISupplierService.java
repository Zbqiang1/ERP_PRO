package com.erp.modules.purchase.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.purchase.entity.Supplier;
import com.erp.modules.purchase.dto.SupplierDTO;
import com.erp.modules.purchase.dto.SupplierQueryDTO;
import com.erp.modules.purchase.vo.SupplierVO;

/**
 * 供应商 服务接口
 */
public interface ISupplierService extends IService<Supplier> {

    /**
     * 分页查询供应商
     */
    Page<SupplierVO> queryPage(SupplierQueryDTO queryDTO);

    /**
     * 根据ID查询供应商
     */
    SupplierVO getDetailById(Long id);

    /**
     * 新增供应商
     */
    void create(SupplierDTO dto);

    /**
     * 编辑供应商
     */
    void update(SupplierDTO dto);

    /**
     * 删除供应商
     */
    void delete(Long id);

    /**
     * 更新供应商状态
     */
    void updateStatus(Long id, Integer status);
}
