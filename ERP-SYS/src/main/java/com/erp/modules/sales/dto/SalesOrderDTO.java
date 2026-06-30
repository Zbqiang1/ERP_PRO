package com.erp.modules.sales.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 销售订单新增/编辑DTO
 */
@Data
public class SalesOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 销售订单号 */
    private String soNo;

    /** 客户ID */
    @NotNull(message = "客户不能为空")
    private Long customerId;

    /** 客户名称 */
    private String customerName;

    /** 下单日期 */
    @NotNull(message = "下单日期不能为空")
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
    private List<SalesOrderDetailDTO> details;
}
