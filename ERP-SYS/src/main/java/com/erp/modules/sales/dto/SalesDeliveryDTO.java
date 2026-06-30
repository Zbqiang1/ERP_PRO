package com.erp.modules.sales.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 销售发货新增/编辑DTO
 */
@Data
public class SalesDeliveryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 发货单号 */
    private String deliveryNo;

    /** 销售订单ID */
    @NotNull(message = "销售订单不能为空")
    private Long soId;

    /** 销售订单号 */
    private String soNo;

    /** 客户ID */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 仓库ID */
    @NotNull(message = "仓库不能为空")
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 发货日期 */
    @NotNull(message = "发货日期不能为空")
    private LocalDate deliveryDate;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 状态（0-待发货 1-已发货 2-已签收） */
    private Integer status;
}
