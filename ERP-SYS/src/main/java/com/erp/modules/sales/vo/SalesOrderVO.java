package com.erp.modules.sales.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售订单视图对象
 */
@Data
public class SalesOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 销售订单号 */
    private String soNo;

    /** 客户ID */
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 下单日期 */
    private LocalDate orderDate;

    /** 预计发货日期 */
    private LocalDate deliveryDate;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 状态（0-待审批 1-已确认 2-部分发货 3-已完成 4-已取消） */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 销售订单明细列表 */
    private List<SalesOrderDetailVO> details;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
