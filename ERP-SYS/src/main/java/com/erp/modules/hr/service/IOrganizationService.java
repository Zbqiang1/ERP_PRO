package com.erp.modules.hr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erp.modules.hr.entity.Organization;
import com.erp.modules.hr.dto.OrganizationDTO;
import com.erp.modules.hr.dto.OrganizationQueryDTO;
import com.erp.modules.hr.vo.OrganizationVO;

/**
 * 组织架构 服务接口
 */
public interface IOrganizationService extends IService<Organization> {

    /**
     * 分页查询组织架构
     */
    Page<OrganizationVO> queryPage(OrganizationQueryDTO queryDTO);

    /**
     * 根据ID查询组织架构
     */
    OrganizationVO getDetailById(Long id);

    /**
     * 新增组织架构
     */
    void create(OrganizationDTO dto);

    /**
     * 编辑组织架构
     */
    void update(OrganizationDTO dto);

    /**
     * 删除组织架构
     */
    void delete(Long id);
}
