package com.erp.modules.production.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erp.modules.production.entity.BomHeader;
import com.erp.modules.production.mapper.BomHeaderMapper;
import com.erp.modules.production.service.IBomHeaderService;
import org.springframework.stereotype.Service;

/**
 * BOM表头 Service实现类
 */
@Service
public class BomHeaderServiceImpl extends ServiceImpl<BomHeaderMapper, BomHeader> implements IBomHeaderService {
}
