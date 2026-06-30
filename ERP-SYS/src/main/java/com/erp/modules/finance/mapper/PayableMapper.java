package com.erp.modules.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.finance.entity.Payable;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应付款 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface PayableMapper extends BaseMapper<Payable> {
}
