package com.erp.modules.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.report.entity.BusinessAlert;
import com.erp.modules.report.mapper.BusinessAlertMapper;
import com.erp.modules.report.service.IBusinessAlertService;
import org.springframework.stereotype.Service;

/**
 * 业务预警 Service实现类
 */
@Service
public class BusinessAlertServiceImpl extends ServiceImpl<BusinessAlertMapper, BusinessAlert> implements IBusinessAlertService {
}
