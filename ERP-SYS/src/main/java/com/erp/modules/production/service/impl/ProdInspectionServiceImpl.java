package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.ProdInspection;
import com.erp.modules.production.mapper.ProdInspectionMapper;
import com.erp.modules.production.service.IProdInspectionService;
import org.springframework.stereotype.Service;

/**
 * 生产检验单服务实现
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Service
public class ProdInspectionServiceImpl extends ServiceImpl<ProdInspectionMapper, ProdInspection> implements IProdInspectionService {
}
