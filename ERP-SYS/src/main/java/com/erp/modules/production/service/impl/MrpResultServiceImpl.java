package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.MrpResult;
import com.erp.modules.production.mapper.MrpResultMapper;
import com.erp.modules.production.service.IMrpResultService;
import org.springframework.stereotype.Service;

/**
 * MRP运算结果 Service实现类
 */
@Service
public class MrpResultServiceImpl extends ServiceImpl<MrpResultMapper, MrpResult> implements IMrpResultService {
}
