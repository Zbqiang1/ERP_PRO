package com.erp.modules.purchase.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 采购订单新增/编辑DTO
 */
@Data
public class PurchaseOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 采购订单号 */
    private String poNo;

    /** 供应商ID */
    @NotNull(message = "供应商不能为空")
    private Long supplierId;

    /** 供应商名称 */
    private String supplierName;

    /** 采购申请ID */
    private Long prId;

    /** 下单日期 */
    @NotNull(message = "下单日期不能为空")
    private LocalDate orderDate;

    /** 预计到货日期 */
    private LocalDate deliveryDate;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 状态（0-待确认 1-已确认 2-部分到货 3-已完成 4-已取消） */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 采购订单明细列表 */
    private List<PurchaseOrderDetailDTO> details;
}
