package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.MpsPlan;
import com.erp.modules.production.mapper.MpsPlanMapper;
import com.erp.modules.production.service.IMpsPlanService;
import org.springframework.stereotype.Service;

/**
 * 主生产计划 Service实现类
 */
@Service
public class MpsPlanServiceImpl extends ServiceImpl<MpsPlanMapper, MpsPlan> implements IMpsPlanService {
}
