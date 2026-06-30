package com.erp.modules.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.modules.finance.entity.TaxInvoice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 税务发票 Mapper 接口
 *
 * @author ERP
 */
@Mapper
public interface TaxInvoiceMapper extends BaseMapper<TaxInvoice> {
}
