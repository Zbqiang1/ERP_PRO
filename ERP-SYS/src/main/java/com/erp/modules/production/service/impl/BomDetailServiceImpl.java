package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.BomDetail;
import com.erp.modules.production.mapper.BomDetailMapper;
import com.erp.modules.production.service.IBomDetailService;
import org.springframework.stereotype.Service;

/**
 * BOM明细 Service实现类
 */
@Service
public class BomDetailServiceImpl extends ServiceImpl<BomDetailMapper, BomDetail> implements IBomDetailService {
}
