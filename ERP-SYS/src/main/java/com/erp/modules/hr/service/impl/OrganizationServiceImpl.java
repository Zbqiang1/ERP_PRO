package com.erp.modules.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.common.exception.BusinessException;
import com.erp.modules.hr.dto.OrganizationDTO;
import com.erp.modules.hr.dto.OrganizationQueryDTO;
import com.erp.modules.hr.entity.Organization;
import com.erp.modules.hr.mapper.OrganizationMapper;
import com.erp.modules.hr.service.IOrganizationService;
import com.erp.modules.hr.vo.OrganizationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * 组织架构 服务实现类
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Override
    public Page<OrganizationVO> queryPage(OrganizationQueryDTO queryDTO) {
        Page<Organization> page = new Page<>(queryDTO.getPage() != null ? queryDTO.getPage() : 1,
                queryDTO.getSize() != null ? queryDTO.getSize() : 10);
        LambdaQueryWrapper<Organization> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getOrgName()), Organization::getOrgName, queryDTO.getOrgName());
        wrapper.like(StringUtils.hasText(queryDTO.getOrgCode()), Organization::getOrgCode, queryDTO.getOrgCode());
        wrapper.eq(queryDTO.getOrgType() != null, Organization::getOrgType, queryDTO.getOrgType());
        wrapper.orderByAsc(Organization::getSortOrder);
        Page<Organization> resultPage = this.page(page, wrapper);
        Page<OrganizationVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream().map(this::toVO).collect(Collectors.toList()));
        return voPage;
    }

    @Override
    public OrganizationVO getDetailById(Long id) {
        Organization org = this.getById(id);
        if (org == null) {
            throw new BusinessException("组织架构不存在");
        }
        return toVO(org);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(OrganizationDTO dto) {
        Organization org = new Organization();
        BeanUtils.copyProperties(dto, org);
        this.save(org);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrganizationDTO dto) {
        Organization org = new Organization();
        BeanUtils.copyProperties(dto, org);
        this.updateById(org);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
    }

    private OrganizationVO toVO(Organization org) {
        OrganizationVO vo = new OrganizationVO();
        BeanUtils.copyProperties(org, vo);
        return vo;
    }
}
