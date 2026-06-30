package com.erp.modules.finance.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 财务模块通用查询DTO
 *
 * @author ERP
 */
@Data
@Accessors(chain = true)
public class FinanceQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer size;
}
