package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.Subcontract;
import com.erp.modules.production.mapper.SubcontractMapper;
import com.erp.modules.production.service.ISubcontractService;
import org.springframework.stereotype.Service;

/**
 * 委外加工单 Service实现类
 */
@Service
public class SubcontractServiceImpl extends ServiceImpl<SubcontractMapper, Subcontract> implements ISubcontractService {
}
