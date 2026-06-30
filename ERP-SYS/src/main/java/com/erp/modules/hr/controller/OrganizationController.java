package com.erp.modules.hr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.result.Result;
import com.erp.modules.hr.dto.OrganizationDTO;
import com.erp.modules.hr.dto.OrganizationQueryDTO;
import com.erp.modules.hr.service.IOrganizationService;
import com.erp.modules.hr.vo.OrganizationVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 组织架构 控制器
 */
@RestController
@RequestMapping("/api/hr/organizations")
public class OrganizationController {

    private final IOrganizationService organizationService;

    public OrganizationController(IOrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * 分页查询组织架构
     */
    @GetMapping
    public Result<Page<OrganizationVO>> list(OrganizationQueryDTO queryDTO) {
        return Result.ok(organizationService.queryPage(queryDTO));
    }

    /**
     * 根据ID查询组织架构
     */
    @GetMapping("/{id}")
    public Result<OrganizationVO> getById(@PathVariable Long id) {
        return Result.ok(organizationService.getDetailById(id));
    }

    /**
     * 新增组织架构
     */
    @PostMapping
    public Result<Void> create(@Valid @RequestBody OrganizationDTO dto) {
        organizationService.create(dto);
        return Result.ok();
    }

    /**
     * 编辑组织架构
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody OrganizationDTO dto) {
        organizationService.update(dto);
        return Result.ok();
    }

    /**
     * 删除组织架构
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return Result.ok();
    }
}
