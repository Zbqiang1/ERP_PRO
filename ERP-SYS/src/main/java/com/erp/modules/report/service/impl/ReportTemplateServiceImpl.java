package com.erp.modules.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.report.entity.ReportTemplate;
import com.erp.modules.report.mapper.ReportTemplateMapper;
import com.erp.modules.report.service.IReportTemplateService;
import org.springframework.stereotype.Service;

/**
 * 报表模板 Service实现类
 */
@Service
public class ReportTemplateServiceImpl extends ServiceImpl<ReportTemplateMapper, ReportTemplate> implements IReportTemplateService {
}
