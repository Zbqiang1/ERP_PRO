-- ============================================================
-- ERP System - Complete Database Initialization Script
-- Version: 1.0.0
-- Engine: InnoDB / Charset: utf8mb4
-- Date: 2026-06-29
-- ============================================================

-- 数据库 erp_db 由 JDBC URL 参数 createDatabaseIfNotExist=true 自动创建


-- -----------------------------------------------------------
-- 一、系统基础档案 (System Master Data)
-- -----------------------------------------------------------

-- 用户表
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID（业务标识）',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(256) NOT NULL COMMENT '密码（BCrypt加密）',
    `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    `dept_id` VARCHAR(64) DEFAULT NULL COMMENT '所属部门ID',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_status_deleted` (`status`, `deleted`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 角色表
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `role_name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(256) DEFAULT NULL COMMENT '角色描述',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_role_code` (`role_code`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 用户角色关联表
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 菜单表
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID（0为根菜单）',
    `menu_name` VARCHAR(64) NOT NULL COMMENT '菜单名称',
    `menu_type` TINYINT NOT NULL COMMENT '菜单类型（0-目录，1-菜单，2-按钮）',
    `path` VARCHAR(256) DEFAULT NULL COMMENT '路由路径',
    `component` VARCHAR(256) DEFAULT NULL COMMENT '前端组件路径',
    `perms` VARCHAR(256) DEFAULT NULL COMMENT '权限标识',
    `icon` VARCHAR(64) DEFAULT NULL COMMENT '图标',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `visible` TINYINT DEFAULT 1 COMMENT '是否可见（0-隐藏，1-显示）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 角色菜单关联表
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 部门表
DROP TABLE IF EXISTS `t_department`;
CREATE TABLE `t_department` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '上级部门ID（0为顶级部门）',
    `dept_name` VARCHAR(128) NOT NULL COMMENT '部门名称',
    `dept_code` VARCHAR(64) NOT NULL COMMENT '部门编码',
    `leader` VARCHAR(64) DEFAULT NULL COMMENT '负责人',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_dept_code` (`dept_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 数据字典表
DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `dict_type` VARCHAR(64) NOT NULL COMMENT '字典类型',
    `dict_label` VARCHAR(128) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(256) NOT NULL COMMENT '字典值',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    KEY `idx_dict_type` (`dict_type`),
    KEY `idx_dict_type_status` (`dict_type`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';


-- -----------------------------------------------------------
-- 二、财务基础与业务 (Finance)
-- -----------------------------------------------------------

-- 会计科目表（基础档案）
DROP TABLE IF EXISTS `t_account_subject`;
CREATE TABLE `t_account_subject` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `subject_code` VARCHAR(32) NOT NULL COMMENT '科目编码',
    `subject_name` VARCHAR(128) NOT NULL COMMENT '科目名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '上级科目ID（0为一级科目）',
    `subject_type` TINYINT NOT NULL COMMENT '科目类型（0-资产，1-负债，2-权益，3-成本，4-损益）',
    `balance_direction` TINYINT NOT NULL COMMENT '余额方向（0-借，1-贷）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_subject_code` (`subject_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会计科目表';

-- 记账凭证主表（业务主表）
DROP TABLE IF EXISTS `t_voucher`;
CREATE TABLE `t_voucher` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `voucher_no` VARCHAR(32) NOT NULL COMMENT '凭证号',
    `voucher_date` DATE NOT NULL COMMENT '凭证日期',
    `voucher_type` TINYINT NOT NULL COMMENT '凭证类型（0-记账凭证，1-转账凭证，2-调整凭证）',
    `total_debit` DECIMAL(18,2) DEFAULT 0.00 COMMENT '借方合计金额',
    `total_credit` DECIMAL(18,2) DEFAULT 0.00 COMMENT '贷方合计金额',
    `auditor_id` VARCHAR(64) DEFAULT NULL COMMENT '审核人ID',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已审核，2-已过账）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_voucher_no` (`voucher_no`),
    KEY `idx_voucher_date` (`voucher_date`),
    KEY `idx_status_deleted` (`status`, `deleted`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记账凭证主表';

-- 凭证分录明细表（单据明细）
DROP TABLE IF EXISTS `t_voucher_entry`;
CREATE TABLE `t_voucher_entry` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `voucher_id` BIGINT NOT NULL COMMENT '凭证主表ID',
    `subject_id` BIGINT NOT NULL COMMENT '会计科目ID',
    `summary` VARCHAR(256) DEFAULT NULL COMMENT '摘要',
    `debit_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '借方金额',
    `credit_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '贷方金额',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_voucher_id` (`voucher_id`),
    KEY `idx_subject_id` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='凭证分录明细表';

-- 应收账款主表（业务主表）
DROP TABLE IF EXISTS `t_receivable`;
CREATE TABLE `t_receivable` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `receivable_no` VARCHAR(32) NOT NULL COMMENT '应收单号',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `customer_name` VARCHAR(128) NOT NULL COMMENT '客户名称（历史快照）',
    `amount` DECIMAL(18,2) NOT NULL COMMENT '应收金额',
    `received_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '已收金额',
    `balance` DECIMAL(18,2) DEFAULT 0.00 COMMENT '余额',
    `due_date` DATE DEFAULT NULL COMMENT '到期日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-未收款，1-部分收款，2-已收款，3-已核销，4-坏账）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_receivable_no` (`receivable_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_due_date` (`due_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应收账款主表';

-- 应付账款主表（业务主表）
DROP TABLE IF EXISTS `t_payable`;
CREATE TABLE `t_payable` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `payable_no` VARCHAR(32) NOT NULL COMMENT '应付单号',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `supplier_name` VARCHAR(128) NOT NULL COMMENT '供应商名称（历史快照）',
    `amount` DECIMAL(18,2) NOT NULL COMMENT '应付金额',
    `paid_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '已付金额',
    `balance` DECIMAL(18,2) DEFAULT 0.00 COMMENT '余额',
    `due_date` DATE DEFAULT NULL COMMENT '到期日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-未付款，1-部分付款，2-已付款，3-已核销）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_payable_no` (`payable_no`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_due_date` (`due_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应付账款主表';

-- 固定资产主表（业务主表）
DROP TABLE IF EXISTS `t_fixed_asset`;
CREATE TABLE `t_fixed_asset` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `asset_no` VARCHAR(32) NOT NULL COMMENT '资产编号',
    `asset_name` VARCHAR(128) NOT NULL COMMENT '资产名称',
    `category` VARCHAR(64) DEFAULT NULL COMMENT '资产类别',
    `dept_id` VARCHAR(64) DEFAULT NULL COMMENT '使用部门ID',
    `original_value` DECIMAL(18,2) NOT NULL COMMENT '原值',
    `current_value` DECIMAL(18,2) NOT NULL COMMENT '净值',
    `depreciation_rate` DECIMAL(5,4) DEFAULT NULL COMMENT '折旧率',
    `purchase_date` DATE DEFAULT NULL COMMENT '购买日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-使用中，1-闲置，2-报废，3-处置）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_asset_no` (`asset_no`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_category` (`category`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='固定资产表';

-- 成本核算主表（业务主表）
DROP TABLE IF EXISTS `t_cost_sheet`;
CREATE TABLE `t_cost_sheet` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `cost_no` VARCHAR(32) NOT NULL COMMENT '成本核算单号',
    `product_id` BIGINT NOT NULL COMMENT '产品ID',
    `product_name` VARCHAR(128) NOT NULL COMMENT '产品名称（历史快照）',
    `material_cost` DECIMAL(18,2) DEFAULT 0.00 COMMENT '材料成本',
    `labor_cost` DECIMAL(18,2) DEFAULT 0.00 COMMENT '人工成本',
    `overhead_cost` DECIMAL(18,2) DEFAULT 0.00 COMMENT '制造费用',
    `total_cost` DECIMAL(18,2) DEFAULT 0.00 COMMENT '总成本',
    `cost_period` VARCHAR(7) DEFAULT NULL COMMENT '成本期间（YYYY-MM）',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已确认，2-已审核）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_cost_no` (`cost_no`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_cost_period` (`cost_period`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成本核算主表';

-- 税务发票主表（业务主表）
DROP TABLE IF EXISTS `t_tax_invoice`;
CREATE TABLE `t_tax_invoice` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `invoice_no` VARCHAR(32) NOT NULL COMMENT '发票号',
    `invoice_type` TINYINT NOT NULL COMMENT '发票类型（0-增值税专用发票，1-增值税普通发票）',
    `invoice_date` DATE NOT NULL COMMENT '开票日期',
    `buyer_name` VARCHAR(256) NOT NULL COMMENT '购方名称',
    `seller_name` VARCHAR(256) NOT NULL COMMENT '销方名称',
    `amount` DECIMAL(18,2) NOT NULL COMMENT '金额（不含税）',
    `tax_rate` DECIMAL(5,2) DEFAULT NULL COMMENT '税率（%）',
    `tax_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '税额',
    `total_amount` DECIMAL(18,2) NOT NULL COMMENT '价税合计',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-未认证，1-已认证，2-已抵扣）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_invoice_no` (`invoice_no`),
    KEY `idx_invoice_date` (`invoice_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='税务发票表';


-- -----------------------------------------------------------
-- 三、采购管理 (Purchase)
-- -----------------------------------------------------------

-- 供应商表（基础档案）
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `supplier_code` VARCHAR(32) NOT NULL COMMENT '供应商编码',
    `supplier_name` VARCHAR(128) NOT NULL COMMENT '供应商名称',
    `contact_person` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `address` VARCHAR(256) DEFAULT NULL COMMENT '地址',
    `bank_name` VARCHAR(128) DEFAULT NULL COMMENT '开户银行',
    `bank_account` VARCHAR(64) DEFAULT NULL COMMENT '银行账号',
    `tax_no` VARCHAR(32) DEFAULT NULL COMMENT '税号',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_supplier_code` (`supplier_code`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 采购申请主表（业务主表）
DROP TABLE IF EXISTS `t_purchase_request`;
CREATE TABLE `t_purchase_request` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `pr_no` VARCHAR(32) NOT NULL COMMENT '采购申请单号',
    `dept_id` VARCHAR(64) NOT NULL COMMENT '申请部门ID',
    `applicant_id` VARCHAR(64) NOT NULL COMMENT '申请人ID',
    `apply_date` DATE NOT NULL COMMENT '申请日期',
    `expected_delivery_date` DATE DEFAULT NULL COMMENT '期望交付日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-审批中，2-已审批，3-已转PO）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_pr_no` (`pr_no`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_applicant_id` (`applicant_id`),
    KEY `idx_apply_date` (`apply_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购申请主表';

-- 采购申请明细表（单据明细）
DROP TABLE IF EXISTS `t_purchase_request_detail`;
CREATE TABLE `t_purchase_request_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `pr_id` BIGINT NOT NULL COMMENT '采购申请主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_pr_id` (`pr_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购申请明细表';

-- 采购订单主表（业务主表）
DROP TABLE IF EXISTS `t_purchase_order`;
CREATE TABLE `t_purchase_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `po_no` VARCHAR(32) NOT NULL COMMENT '采购订单号',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `supplier_name` VARCHAR(128) NOT NULL COMMENT '供应商名称（历史快照）',
    `pr_id` BIGINT DEFAULT NULL COMMENT '关联采购申请ID',
    `order_date` DATE NOT NULL COMMENT '订单日期',
    `delivery_date` DATE DEFAULT NULL COMMENT '交货日期',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '订单总金额',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已下达，2-部分收货，3-已收货，4-已关闭）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_po_no` (`po_no`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_pr_id` (`pr_id`),
    KEY `idx_order_date` (`order_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单主表';

-- 采购订单明细表（单据明细）
DROP TABLE IF EXISTS `t_purchase_order_detail`;
CREATE TABLE `t_purchase_order_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `po_id` BIGINT NOT NULL COMMENT '采购订单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '订单数量',
    `received_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '已收货数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_po_id` (`po_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单明细表';

-- 来料检验主表（业务主表）
DROP TABLE IF EXISTS `t_iqc_inspection`;
CREATE TABLE `t_iqc_inspection` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `inspection_no` VARCHAR(32) NOT NULL COMMENT '检验单号',
    `po_id` BIGINT NOT NULL COMMENT '采购订单ID',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `inspector_id` VARCHAR(64) NOT NULL COMMENT '检验员ID',
    `inspection_date` DATE NOT NULL COMMENT '检验日期',
    `inspection_result` TINYINT NOT NULL COMMENT '检验结果（0-合格，1-不合格，2-让步接收）',
    `qualified_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '合格数量',
    `defect_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '不合格数量',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-待检验，1-检验中，2-已完成）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_iqc_inspection_no` (`inspection_no`),
    KEY `idx_po_id` (`po_id`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='来料检验主表';

-- 采购退货主表（业务主表）
DROP TABLE IF EXISTS `t_purchase_return`;
CREATE TABLE `t_purchase_return` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `return_no` VARCHAR(32) NOT NULL COMMENT '退货单号',
    `po_id` BIGINT NOT NULL COMMENT '关联采购订单ID',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `supplier_name` VARCHAR(128) NOT NULL COMMENT '供应商名称（历史快照）',
    `return_date` DATE NOT NULL COMMENT '退货日期',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '退货总金额',
    `return_reason` VARCHAR(500) DEFAULT NULL COMMENT '退货原因',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已退货，2-已完成）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_pr_return_no` (`return_no`),
    KEY `idx_po_id` (`po_id`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购退货主表';

-- 采购退货明细表（单据明细）
DROP TABLE IF EXISTS `t_purchase_return_detail`;
CREATE TABLE `t_purchase_return_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `return_id` BIGINT NOT NULL COMMENT '退货主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '退货数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    KEY `idx_return_id` (`return_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购退货明细表';

-- 采购价格表（基础档案）
DROP TABLE IF EXISTS `t_purchase_price`;
CREATE TABLE `t_purchase_price` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `unit_price` DECIMAL(18,2) NOT NULL COMMENT '单价',
    `currency` VARCHAR(8) DEFAULT 'CNY' COMMENT '币种',
    `effective_date` DATE DEFAULT NULL COMMENT '生效日期',
    `expire_date` DATE DEFAULT NULL COMMENT '失效日期',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_supplier_material_date` (`supplier_id`, `material_id`, `effective_date`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购价格表';


-- -----------------------------------------------------------
-- 四、销售管理 (Sales)
-- -----------------------------------------------------------

-- 客户表（基础档案）
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `customer_code` VARCHAR(32) NOT NULL COMMENT '客户编码',
    `customer_name` VARCHAR(128) NOT NULL COMMENT '客户名称',
    `contact_person` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `address` VARCHAR(256) DEFAULT NULL COMMENT '地址',
    `credit_limit` DECIMAL(18,2) DEFAULT 0.00 COMMENT '信用额度',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_customer_code` (`customer_code`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 销售报价单主表（业务主表）
DROP TABLE IF EXISTS `t_sales_quotation`;
CREATE TABLE `t_sales_quotation` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `quotation_no` VARCHAR(32) NOT NULL COMMENT '报价单号',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `customer_name` VARCHAR(128) NOT NULL COMMENT '客户名称（历史快照）',
    `quotation_date` DATE NOT NULL COMMENT '报价日期',
    `valid_until` DATE DEFAULT NULL COMMENT '有效期至',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '报价总金额',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已发送，2-已确认，3-已关闭）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_quotation_no` (`quotation_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售报价单主表';

-- 销售报价单明细表（单据明细）
DROP TABLE IF EXISTS `t_sales_quotation_detail`;
CREATE TABLE `t_sales_quotation_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `quotation_id` BIGINT NOT NULL COMMENT '报价单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_quotation_id` (`quotation_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售报价单明细表';

-- 销售订单主表（业务主表）
DROP TABLE IF EXISTS `t_sales_order`;
CREATE TABLE `t_sales_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `so_no` VARCHAR(32) NOT NULL COMMENT '销售订单号',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `customer_name` VARCHAR(128) NOT NULL COMMENT '客户名称（历史快照）',
    `order_date` DATE NOT NULL COMMENT '订单日期',
    `delivery_date` DATE DEFAULT NULL COMMENT '交付日期',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '订单总金额',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已确认，2-部分发货，3-已发货，4-已关闭）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_so_no` (`so_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_order_date` (`order_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单主表';

-- 销售订单明细表（单据明细）
DROP TABLE IF EXISTS `t_sales_order_detail`;
CREATE TABLE `t_sales_order_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `so_id` BIGINT NOT NULL COMMENT '销售订单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '订单数量',
    `shipped_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '已发货数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_so_id` (`so_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单明细表';

-- 销售发货单主表（业务主表）
DROP TABLE IF EXISTS `t_sales_delivery`;
CREATE TABLE `t_sales_delivery` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `delivery_no` VARCHAR(32) NOT NULL COMMENT '发货单号',
    `so_id` BIGINT NOT NULL COMMENT '关联销售订单ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `customer_name` VARCHAR(128) NOT NULL COMMENT '客户名称（历史快照）',
    `warehouse_id` BIGINT NOT NULL COMMENT '发货仓库ID',
    `delivery_date` DATE NOT NULL COMMENT '发货日期',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '发货总金额',
    `shipping_address` VARCHAR(256) DEFAULT NULL COMMENT '收货地址',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-待发货，1-已发货，2-已签收）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_delivery_no` (`delivery_no`),
    KEY `idx_so_id` (`so_id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售发货单主表';

-- 销售发货单明细表（单据明细）
DROP TABLE IF EXISTS `t_sales_delivery_detail`;
CREATE TABLE `t_sales_delivery_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `delivery_id` BIGINT NOT NULL COMMENT '发货单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '发货数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    KEY `idx_delivery_id` (`delivery_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售发货单明细表';

-- 销售退货主表（业务主表）
DROP TABLE IF EXISTS `t_sales_return`;
CREATE TABLE `t_sales_return` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `return_no` VARCHAR(32) NOT NULL COMMENT '退货单号',
    `so_id` BIGINT NOT NULL COMMENT '关联销售订单ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `customer_name` VARCHAR(128) NOT NULL COMMENT '客户名称（历史快照）',
    `return_date` DATE NOT NULL COMMENT '退货日期',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '退货总金额',
    `return_reason` VARCHAR(500) DEFAULT NULL COMMENT '退货原因',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已退货，2-已完成）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_sr_return_no` (`return_no`),
    KEY `idx_so_id` (`so_id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售退货主表';

-- 销售退货明细表（单据明细）
DROP TABLE IF EXISTS `t_sales_return_detail`;
CREATE TABLE `t_sales_return_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `return_id` BIGINT NOT NULL COMMENT '退货主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '退货数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    KEY `idx_return_id` (`return_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售退货明细表';


-- -----------------------------------------------------------
-- 五、库存仓储 (Inventory)
-- -----------------------------------------------------------

-- 仓库表（基础档案）
DROP TABLE IF EXISTS `t_warehouse`;
CREATE TABLE `t_warehouse` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `warehouse_code` VARCHAR(32) NOT NULL COMMENT '仓库编码',
    `warehouse_name` VARCHAR(128) NOT NULL COMMENT '仓库名称',
    `address` VARCHAR(256) DEFAULT NULL COMMENT '仓库地址',
    `manager_id` VARCHAR(64) DEFAULT NULL COMMENT '负责人ID',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_warehouse_code` (`warehouse_code`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- 库位表（基础档案）
DROP TABLE IF EXISTS `t_location`;
CREATE TABLE `t_location` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `location_code` VARCHAR(32) NOT NULL COMMENT '库位编码',
    `location_name` VARCHAR(128) DEFAULT NULL COMMENT '库位名称',
    `capacity` DECIMAL(18,4) DEFAULT NULL COMMENT '容量',
    `volume` DECIMAL(18,4) DEFAULT NULL COMMENT '体积',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_warehouse_location` (`warehouse_id`, `location_code`),
    KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位表';

-- 物料表（基础档案）
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `material_code` VARCHAR(32) NOT NULL COMMENT '物料编码',
    `material_name` VARCHAR(128) NOT NULL COMMENT '物料名称',
    `category_id` BIGINT DEFAULT NULL COMMENT '物料类别ID',
    `category_name` VARCHAR(64) DEFAULT NULL COMMENT '物料类别名称',
    `spec` VARCHAR(128) DEFAULT NULL COMMENT '规格型号',
    `unit` VARCHAR(16) DEFAULT NULL COMMENT '单位',
    `safety_stock` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '安全库存',
    `max_stock` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '最大库存',
    `min_stock` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '最小库存',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '参考单价',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_material_code` (`material_code`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物料表';

-- 库存余额表（库存台账）
DROP TABLE IF EXISTS `t_inventory`;
CREATE TABLE `t_inventory` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `location_id` BIGINT NOT NULL COMMENT '库位ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `on_hand_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '现有库存量',
    `locked_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '锁定库存量',
    `available_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '可用库存量',
    `last_count_time` DATETIME DEFAULT NULL COMMENT '最后盘点时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    UNIQUE KEY `uk_warehouse_location_material` (`warehouse_id`, `location_id`, `material_id`),
    KEY `idx_material_id` (`material_id`),
    KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存余额表';

-- 入库单主表（业务主表）
DROP TABLE IF EXISTS `t_stock_in_order`;
CREATE TABLE `t_stock_in_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `in_no` VARCHAR(32) NOT NULL COMMENT '入库单号',
    `in_type` TINYINT NOT NULL COMMENT '入库类型（0-采购入库，1-生产入库，2-调拨入库，3-退货入库，4-盘点盘盈，5-其他入库）',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `related_order_id` BIGINT DEFAULT NULL COMMENT '关联单据ID',
    `related_order_no` VARCHAR(32) DEFAULT NULL COMMENT '关联单据号',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '入库总金额',
    `in_date` DATE NOT NULL COMMENT '入库日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已入库，2-已记账）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_in_no` (`in_no`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_in_type` (`in_type`),
    KEY `idx_in_date` (`in_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单主表';

-- 入库单明细表（单据明细）
DROP TABLE IF EXISTS `t_stock_in_detail`;
CREATE TABLE `t_stock_in_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `in_id` BIGINT NOT NULL COMMENT '入库单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `location_id` BIGINT DEFAULT NULL COMMENT '库位ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '入库数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    `batch_no` VARCHAR(64) DEFAULT NULL COMMENT '批次号',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_in_id` (`in_id`),
    KEY `idx_material_id` (`material_id`),
    KEY `idx_batch_no` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单明细表';

-- 出库单主表（业务主表）
DROP TABLE IF EXISTS `t_stock_out_order`;
CREATE TABLE `t_stock_out_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `out_no` VARCHAR(32) NOT NULL COMMENT '出库单号',
    `out_type` TINYINT NOT NULL COMMENT '出库类型（0-销售出库，1-生产领料，2-调拨出库，3-退货出库，4-盘点盘亏，5-其他出库）',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `related_order_id` BIGINT DEFAULT NULL COMMENT '关联单据ID',
    `related_order_no` VARCHAR(32) DEFAULT NULL COMMENT '关联单据号',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '出库总金额',
    `out_date` DATE NOT NULL COMMENT '出库日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已出库，2-已记账）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_out_no` (`out_no`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_out_type` (`out_type`),
    KEY `idx_out_date` (`out_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单主表';

-- 出库单明细表（单据明细）
DROP TABLE IF EXISTS `t_stock_out_detail`;
CREATE TABLE `t_stock_out_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `out_id` BIGINT NOT NULL COMMENT '出库单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `location_id` BIGINT DEFAULT NULL COMMENT '库位ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '出库数量',
    `unit_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '单价',
    `amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '金额',
    `batch_no` VARCHAR(64) DEFAULT NULL COMMENT '批次号',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_out_id` (`out_id`),
    KEY `idx_material_id` (`material_id`),
    KEY `idx_batch_no` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单明细表';

-- 调拨单主表（业务主表）
DROP TABLE IF EXISTS `t_transfer_order`;
CREATE TABLE `t_transfer_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `transfer_no` VARCHAR(32) NOT NULL COMMENT '调拨单号',
    `from_warehouse_id` BIGINT NOT NULL COMMENT '调出仓库ID',
    `to_warehouse_id` BIGINT NOT NULL COMMENT '调入仓库ID',
    `total_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '调拨总金额',
    `transfer_date` DATE NOT NULL COMMENT '调拨日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已调拨，2-已确认）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_transfer_no` (`transfer_no`),
    KEY `idx_from_warehouse_id` (`from_warehouse_id`),
    KEY `idx_to_warehouse_id` (`to_warehouse_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调拨单主表';

-- 调拨单明细表（单据明细）
DROP TABLE IF EXISTS `t_transfer_detail`;
CREATE TABLE `t_transfer_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `transfer_id` BIGINT NOT NULL COMMENT '调拨单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `from_location_id` BIGINT DEFAULT NULL COMMENT '调出库位ID',
    `to_location_id` BIGINT DEFAULT NULL COMMENT '调入库位ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '调拨数量',
    `batch_no` VARCHAR(64) DEFAULT NULL COMMENT '批次号',
    KEY `idx_transfer_id` (`transfer_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调拨单明细表';

-- 盘点单主表（业务主表）
DROP TABLE IF EXISTS `t_inventory_check`;
CREATE TABLE `t_inventory_check` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `check_no` VARCHAR(32) NOT NULL COMMENT '盘点单号',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `check_type` TINYINT NOT NULL COMMENT '盘点类型（0-周期盘点，1-随机盘点，2-全面盘点）',
    `check_date` DATE NOT NULL COMMENT '盘点日期',
    `check_status` TINYINT DEFAULT 0 COMMENT '盘点状态（0-盘点中，1-已确认，2-已调整）',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_check_no` (`check_no`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_check_date` (`check_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点单主表';

-- 盘点单明细表（单据明细）
DROP TABLE IF EXISTS `t_inventory_check_detail`;
CREATE TABLE `t_inventory_check_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `check_id` BIGINT NOT NULL COMMENT '盘点单主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `location_id` BIGINT DEFAULT NULL COMMENT '库位ID',
    `book_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '账面数量',
    `actual_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '实盘数量',
    `diff_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '差异数量',
    `diff_reason` VARCHAR(500) DEFAULT NULL COMMENT '差异原因',
    KEY `idx_check_id` (`check_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点单明细表';

-- 库存预警主表（业务主表）
DROP TABLE IF EXISTS `t_stock_alert`;
CREATE TABLE `t_stock_alert` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `alert_no` VARCHAR(32) NOT NULL COMMENT '预警单号',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `warehouse_id` BIGINT DEFAULT NULL COMMENT '仓库ID',
    `current_stock` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '当前库存',
    `safety_stock` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '安全库存',
    `alert_type` TINYINT NOT NULL COMMENT '预警类型（0-低于安全库存，1-高于最大库存，2-呆滞物料）',
    `alert_date` DATE NOT NULL COMMENT '预警日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-未处理，1-已处理，2-已忽略）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_alert_no` (`alert_no`),
    KEY `idx_material_id` (`material_id`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_status_alert_type` (`status`, `alert_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警主表';


-- -----------------------------------------------------------
-- 六、生产制造 (Production)
-- -----------------------------------------------------------

-- BOM主表（基础档案）
DROP TABLE IF EXISTS `t_bom_header`;
CREATE TABLE `t_bom_header` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `bom_no` VARCHAR(32) NOT NULL COMMENT 'BOM编号',
    `product_id` BIGINT NOT NULL COMMENT '产品ID（成品物料ID）',
    `product_name` VARCHAR(128) NOT NULL COMMENT '产品名称',
    `version` VARCHAR(16) DEFAULT NULL COMMENT '版本号',
    `effective_date` DATE DEFAULT NULL COMMENT '生效日期',
    `expire_date` DATE DEFAULT NULL COMMENT '失效日期',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_bom_no` (`bom_no`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BOM主表';

-- BOM明细表（单据明细）
DROP TABLE IF EXISTS `t_bom_detail`;
CREATE TABLE `t_bom_detail` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `bom_id` BIGINT NOT NULL COMMENT 'BOM主表ID',
    `parent_material_id` BIGINT DEFAULT NULL COMMENT '父物料ID',
    `material_id` BIGINT NOT NULL COMMENT '子物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '用量',
    `unit` VARCHAR(16) DEFAULT NULL COMMENT '单位',
    `scrap_rate` DECIMAL(5,4) DEFAULT 0.0000 COMMENT '损耗率',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    KEY `idx_bom_id` (`bom_id`),
    KEY `idx_material_id` (`material_id`),
    KEY `idx_parent_material_id` (`parent_material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='BOM明细表';

-- 主生产计划主表（业务主表）
DROP TABLE IF EXISTS `t_mps_plan`;
CREATE TABLE `t_mps_plan` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `plan_no` VARCHAR(32) NOT NULL COMMENT '计划单号',
    `plan_month` VARCHAR(7) NOT NULL COMMENT '计划月份（YYYY-MM）',
    `product_id` BIGINT NOT NULL COMMENT '产品ID',
    `planned_qty` DECIMAL(18,4) NOT NULL COMMENT '计划数量',
    `completed_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '已完成数量',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已下达，2-执行中，3-已完成，4-已关闭）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_plan_no` (`plan_no`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_plan_month` (`plan_month`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主生产计划主表';

-- MRP运算结果明细表（单据明细）
DROP TABLE IF EXISTS `t_mrp_result`;
CREATE TABLE `t_mrp_result` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `plan_id` BIGINT NOT NULL COMMENT '计划主表ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `gross_requirement` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '毛需求',
    `on_hand_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '现有库存量',
    `on_order_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '在途量',
    `net_requirement` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '净需求',
    `planned_order_release` VARCHAR(64) DEFAULT NULL COMMENT '计划订单下达',
    `planned_order_receipt` VARCHAR(64) DEFAULT NULL COMMENT '计划订单接收',
    KEY `idx_plan_id` (`plan_id`),
    KEY `idx_material_id` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='MRP运算结果明细表';

-- 工单主表（业务主表）
DROP TABLE IF EXISTS `t_work_order`;
CREATE TABLE `t_work_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `wo_no` VARCHAR(32) NOT NULL COMMENT '工单号',
    `product_id` BIGINT NOT NULL COMMENT '产品ID',
    `bom_id` BIGINT DEFAULT NULL COMMENT 'BOM ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '工单数量',
    `completed_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '已完成数量',
    `start_date` DATE DEFAULT NULL COMMENT '计划开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '计划结束日期',
    `workshop` VARCHAR(64) DEFAULT NULL COMMENT '生产车间',
    `priority` TINYINT DEFAULT 0 COMMENT '优先级（0-普通，1-紧急，2-非常紧急）',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已下达，2-执行中，3-已完成，4-已关闭）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_wo_no` (`wo_no`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_workshop` (`workshop`),
    KEY `idx_start_date` (`start_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单主表';

-- 工单工艺路线明细表（单据明细）
DROP TABLE IF EXISTS `t_wo_routing`;
CREATE TABLE `t_wo_routing` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `wo_id` BIGINT NOT NULL COMMENT '工单主表ID',
    `operation_seq` INT NOT NULL COMMENT '工序序号',
    `operation_name` VARCHAR(128) NOT NULL COMMENT '工序名称',
    `workcenter` VARCHAR(64) DEFAULT NULL COMMENT '工作中心',
    `planned_hours` DECIMAL(10,2) DEFAULT 0.00 COMMENT '计划工时',
    `actual_hours` DECIMAL(10,2) DEFAULT 0.00 COMMENT '实际工时',
    `worker_id` VARCHAR(64) DEFAULT NULL COMMENT '操作工ID',
    `routing_status` TINYINT DEFAULT 0 COMMENT '状态（0-待加工，1-加工中，2-已完成）',
    KEY `idx_wo_id` (`wo_id`),
    KEY `idx_worker_id` (`worker_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单工艺路线明细表';

-- 委外加工主表（业务主表）
DROP TABLE IF EXISTS `t_subcontract`;
CREATE TABLE `t_subcontract` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `contract_no` VARCHAR(32) NOT NULL COMMENT '委外合同号',
    `supplier_id` BIGINT NOT NULL COMMENT '供应商ID',
    `supplier_name` VARCHAR(128) NOT NULL COMMENT '供应商名称（历史快照）',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `quantity` DECIMAL(18,4) NOT NULL COMMENT '数量',
    `contract_price` DECIMAL(18,2) DEFAULT 0.00 COMMENT '合同单价',
    `send_date` DATE DEFAULT NULL COMMENT '发出日期',
    `expected_return_date` DATE DEFAULT NULL COMMENT '预计返回日期',
    `actual_return_date` DATE DEFAULT NULL COMMENT '实际返回日期',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已发出，2-部分返回，3-已返回，4-已结算）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_contract_no` (`contract_no`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_material_id` (`material_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='委外加工主表';

-- 生产检验主表（业务主表）
DROP TABLE IF EXISTS `t_prod_inspection`;
CREATE TABLE `t_prod_inspection` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `inspection_no` VARCHAR(32) NOT NULL COMMENT '检验单号',
    `wo_id` BIGINT NOT NULL COMMENT '生产工单ID',
    `inspector_id` VARCHAR(64) NOT NULL COMMENT '检验员ID',
    `inspection_date` DATE NOT NULL COMMENT '检验日期',
    `inspection_type` TINYINT NOT NULL COMMENT '检验类型（0-首检，1-巡检，2-终检）',
    `inspection_result` TINYINT NOT NULL COMMENT '检验结果（0-合格，1-不合格，2-让步接收）',
    `qualified_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '合格数量',
    `defect_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '不合格数量',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-待检验，1-检验中，2-已完成）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_prod_inspection_no` (`inspection_no`),
    KEY `idx_wo_id` (`wo_id`),
    KEY `idx_inspector_id` (`inspector_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产检验主表';


-- -----------------------------------------------------------
-- 七、人力资源管理 (HR)
-- -----------------------------------------------------------

-- 组织架构表（基础档案）
DROP TABLE IF EXISTS `t_organization`;
CREATE TABLE `t_organization` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `parent_id` BIGINT DEFAULT 0 COMMENT '上级组织ID（0为顶级组织）',
    `org_name` VARCHAR(128) NOT NULL COMMENT '组织名称',
    `org_code` VARCHAR(64) NOT NULL COMMENT '组织编码',
    `org_type` TINYINT NOT NULL COMMENT '组织类型（0-公司，1-部门，2-小组）',
    `leader_id` VARCHAR(64) DEFAULT NULL COMMENT '负责人ID',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_org_code` (`org_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构表';

-- 员工表（基础档案）
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `employee_no` VARCHAR(32) NOT NULL COMMENT '工号',
    `real_name` VARCHAR(64) NOT NULL COMMENT '姓名',
    `gender` TINYINT DEFAULT NULL COMMENT '性别（0-女，1-男）',
    `birthday` DATE DEFAULT NULL COMMENT '出生日期',
    `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `org_id` BIGINT DEFAULT NULL COMMENT '所属组织ID',
    `position` VARCHAR(64) DEFAULT NULL COMMENT '职位',
    `hire_date` DATE DEFAULT NULL COMMENT '入职日期',
    `leave_date` DATE DEFAULT NULL COMMENT '离职日期',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-离职，1-在职，2-实习）',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_employee_no` (`employee_no`),
    KEY `idx_org_id` (`org_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 考勤表（业务主表）
DROP TABLE IF EXISTS `t_attendance`;
CREATE TABLE `t_attendance` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `employee_id` BIGINT NOT NULL COMMENT '员工ID',
    `attendance_date` DATE NOT NULL COMMENT '考勤日期',
    `check_in_time` DATETIME DEFAULT NULL COMMENT '签到时间',
    `check_out_time` DATETIME DEFAULT NULL COMMENT '签退时间',
    `attendance_type` TINYINT NOT NULL COMMENT '考勤类型（0-正常，1-迟到，2-早退，3-旷工，4-请假，5-出差，6-加班）',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-已记录，1-已确认）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    KEY `idx_employee_id` (`employee_id`),
    KEY `idx_attendance_date` (`attendance_date`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤表';

-- 请假表单主表（业务主表）
DROP TABLE IF EXISTS `t_leave_sheet`;
CREATE TABLE `t_leave_sheet` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `leave_no` VARCHAR(32) NOT NULL COMMENT '请假单号',
    `employee_id` BIGINT NOT NULL COMMENT '员工ID',
    `leave_type` TINYINT NOT NULL COMMENT '请假类型（0-事假，1-病假，2-年假，3-婚假，4-产假，5-调休）',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `total_hours` DECIMAL(10,2) DEFAULT 0.00 COMMENT '请假总时长',
    `reason` VARCHAR(500) DEFAULT NULL COMMENT '请假原因',
    `approver_id` VARCHAR(64) DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-审批中，2-已批准，3-已驳回）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_leave_no` (`leave_no`),
    KEY `idx_employee_id` (`employee_id`),
    KEY `idx_approver_id` (`approver_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假表单主表';

-- 工资单主表（业务主表）
DROP TABLE IF EXISTS `t_salary_sheet`;
CREATE TABLE `t_salary_sheet` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `salary_no` VARCHAR(32) NOT NULL COMMENT '工资单号',
    `employee_id` BIGINT NOT NULL COMMENT '员工ID',
    `salary_month` VARCHAR(7) NOT NULL COMMENT '工资月份（YYYY-MM）',
    `basic_salary` DECIMAL(18,2) DEFAULT 0.00 COMMENT '基本工资',
    `performance_bonus` DECIMAL(18,2) DEFAULT 0.00 COMMENT '绩效奖金',
    `overtime_pay` DECIMAL(18,2) DEFAULT 0.00 COMMENT '加班费',
    `deduction` DECIMAL(18,2) DEFAULT 0.00 COMMENT '扣款',
    `social_insurance` DECIMAL(18,2) DEFAULT 0.00 COMMENT '社保扣款',
    `tax` DECIMAL(18,2) DEFAULT 0.00 COMMENT '个税',
    `net_salary` DECIMAL(18,2) DEFAULT 0.00 COMMENT '实发工资',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已确认，2-已发放）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    UNIQUE KEY `uk_salary_no` (`salary_no`),
    KEY `idx_employee_id` (`employee_id`),
    KEY `idx_salary_month` (`salary_month`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工资单主表';

-- 绩效考核主表（业务主表）
DROP TABLE IF EXISTS `t_performance`;
CREATE TABLE `t_performance` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `employee_id` BIGINT NOT NULL COMMENT '员工ID',
    `assessment_period` VARCHAR(7) NOT NULL COMMENT '考核周期（YYYY-MM）',
    `assessor_id` VARCHAR(64) DEFAULT NULL COMMENT '考核人ID',
    `kpi_score` DECIMAL(5,2) DEFAULT 0.00 COMMENT 'KPI得分',
    `evaluation` VARCHAR(500) DEFAULT NULL COMMENT '考核评价',
    `grade` CHAR(1) DEFAULT NULL COMMENT '考核等级（A/B/C/D）',
    `status` TINYINT DEFAULT 0 COMMENT '单据状态（0-草稿，1-已确认，2-已归档）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    KEY `idx_employee_id` (`employee_id`),
    KEY `idx_assessment_period` (`assessment_period`),
    KEY `idx_assessor_id` (`assessor_id`),
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='绩效考核主表';


-- -----------------------------------------------------------
-- 九、报表与看板 (Reports & Dashboard)
-- -----------------------------------------------------------

-- 仪表盘组件表
DROP TABLE IF EXISTS `t_dashboard_widget`;
CREATE TABLE `t_dashboard_widget` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `widget_name` VARCHAR(128) NOT NULL COMMENT '组件名称',
    `widget_type` TINYINT NOT NULL COMMENT '组件类型（0-数字卡片，1-柱状图，2-折线图，3-饼图）',
    `sql_query` TEXT DEFAULT NULL COMMENT 'SQL查询语句',
    `refresh_interval` INT DEFAULT 60 COMMENT '刷新间隔（秒）',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仪表盘组件表';

-- 报表模板表
DROP TABLE IF EXISTS `t_report_template`;
CREATE TABLE `t_report_template` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `report_name` VARCHAR(128) NOT NULL COMMENT '报表名称',
    `report_type` TINYINT NOT NULL COMMENT '报表类型（0-日报，1-周报，2-月报，3-年报）',
    `module` VARCHAR(64) DEFAULT NULL COMMENT '所属模块',
    `sql_template` TEXT DEFAULT NULL COMMENT 'SQL模板',
    `parameters` VARCHAR(500) DEFAULT NULL COMMENT '参数配置（JSON格式）',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表模板表';

-- 业务预警配置表
DROP TABLE IF EXISTS `t_business_alert`;
CREATE TABLE `t_business_alert` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `alert_name` VARCHAR(128) NOT NULL COMMENT '预警名称',
    `alert_type` TINYINT NOT NULL COMMENT '预警类型（0-库存预警，1-应收账款预警，2-质量异常预警，3-交期预警）',
    `alert_rule` VARCHAR(500) DEFAULT NULL COMMENT '预警规则',
    `alert_level` TINYINT NOT NULL COMMENT '预警级别（0-低，1-中，2-高）',
    `last_trigger_time` DATETIME DEFAULT NULL COMMENT '上次触发时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人用户ID',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
    KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务预警配置表';


-- -----------------------------------------------------------
-- 八、流水日志表 (Transaction Logs) -- ERP审计关键
-- -----------------------------------------------------------

-- 操作日志表（流水日志）
DROP TABLE IF EXISTS `t_operation_log`;
CREATE TABLE `t_operation_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id` VARCHAR(64) DEFAULT NULL COMMENT '操作用户ID',
    `username` VARCHAR(64) DEFAULT NULL COMMENT '操作用户名',
    `module` VARCHAR(64) DEFAULT NULL COMMENT '操作模块',
    `operation` VARCHAR(128) DEFAULT NULL COMMENT '操作描述',
    `method` VARCHAR(256) DEFAULT NULL COMMENT '请求方法',
    `request_url` VARCHAR(512) DEFAULT NULL COMMENT '请求URL',
    `request_method` VARCHAR(16) DEFAULT NULL COMMENT '请求方式（GET/POST/PUT/DELETE）',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
    `response_result` TEXT DEFAULT NULL COMMENT '响应结果',
    `ip` VARCHAR(64) DEFAULT NULL COMMENT '客户端IP',
    `duration` BIGINT DEFAULT NULL COMMENT '执行耗时（毫秒）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_user_id` (`user_id`),
    KEY `idx_module` (`module`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 库存流水日志表（流水日志）
DROP TABLE IF EXISTS `t_inventory_log`;
CREATE TABLE `t_inventory_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `warehouse_id` BIGINT DEFAULT NULL COMMENT '仓库ID',
    `location_id` BIGINT DEFAULT NULL COMMENT '库位ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `change_type` TINYINT NOT NULL COMMENT '变动类型（0-入库，1-出库，2-调拨，3-盘点调整）',
    `before_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '变动前数量',
    `change_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '变动数量',
    `after_qty` DECIMAL(18,4) DEFAULT 0.0000 COMMENT '变动后数量',
    `related_order_type` VARCHAR(32) DEFAULT NULL COMMENT '关联单据类型',
    `related_order_id` BIGINT DEFAULT NULL COMMENT '关联单据ID',
    `related_order_no` VARCHAR(32) DEFAULT NULL COMMENT '关联单据号',
    `batch_no` VARCHAR(64) DEFAULT NULL COMMENT '批次号',
    `operator_id` VARCHAR(64) DEFAULT NULL COMMENT '操作人ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_material_id` (`material_id`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_related_order_no` (`related_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存流水日志表';

-- 应收账款流水日志表（流水日志）
DROP TABLE IF EXISTS `t_receivable_log`;
CREATE TABLE `t_receivable_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `receivable_id` BIGINT NOT NULL COMMENT '应收账款ID',
    `change_type` TINYINT NOT NULL COMMENT '变动类型（0-创建，1-收款，2-核销，3-坏账）',
    `before_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '变动前金额',
    `change_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '变动金额',
    `after_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '变动后金额',
    `payment_method` VARCHAR(32) DEFAULT NULL COMMENT '付款方式',
    `reference_no` VARCHAR(64) DEFAULT NULL COMMENT '参考号',
    `operator_id` VARCHAR(64) DEFAULT NULL COMMENT '操作人ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_receivable_id` (`receivable_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应收账款流水日志表';

-- 应付账款流水日志表（流水日志）
DROP TABLE IF EXISTS `t_payable_log`;
CREATE TABLE `t_payable_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `payable_id` BIGINT NOT NULL COMMENT '应付账款ID',
    `change_type` TINYINT NOT NULL COMMENT '变动类型（0-创建，1-付款，2-核销）',
    `before_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '变动前金额',
    `change_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '变动金额',
    `after_amount` DECIMAL(18,2) DEFAULT 0.00 COMMENT '变动后金额',
    `payment_method` VARCHAR(32) DEFAULT NULL COMMENT '付款方式',
    `reference_no` VARCHAR(64) DEFAULT NULL COMMENT '参考号',
    `operator_id` VARCHAR(64) DEFAULT NULL COMMENT '操作人ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_payable_id` (`payable_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应付账款流水日志表';

-- 审批日志表（流水日志）
DROP TABLE IF EXISTS `t_approval_log`;
CREATE TABLE `t_approval_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `business_type` VARCHAR(32) NOT NULL COMMENT '业务类型',
    `business_id` BIGINT NOT NULL COMMENT '业务ID',
    `business_no` VARCHAR(32) DEFAULT NULL COMMENT '业务单号',
    `approval_node` VARCHAR(128) DEFAULT NULL COMMENT '审批节点',
    `approver_id` VARCHAR(64) DEFAULT NULL COMMENT '审批人ID',
    `approval_result` TINYINT DEFAULT NULL COMMENT '审批结果（0-驳回，1-通过，2-转审）',
    `approval_comment` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_business_type` (`business_type`),
    KEY `idx_business_id` (`business_id`),
    KEY `idx_approver_id` (`approver_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批日志表';

-- 库存预警日志表（流水日志）
DROP TABLE IF EXISTS `t_stock_alert_log`;
CREATE TABLE `t_stock_alert_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `alert_id` BIGINT DEFAULT NULL COMMENT '预警ID',
    `material_id` BIGINT NOT NULL COMMENT '物料ID',
    `warehouse_id` BIGINT DEFAULT NULL COMMENT '仓库ID',
    `alert_type` TINYINT DEFAULT NULL COMMENT '预警类型',
    `alert_level` TINYINT DEFAULT NULL COMMENT '预警级别',
    `alert_message` VARCHAR(500) DEFAULT NULL COMMENT '预警信息',
    `handled_by` VARCHAR(64) DEFAULT NULL COMMENT '处理人ID',
    `handled_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handle_result` VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_alert_id` (`alert_id`),
    KEY `idx_material_id` (`material_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警日志表';


-- ============================================================
-- 种子数据 (Seed Data)
-- ============================================================

-- -----------------------------------------------------------
-- 默认角色
-- -----------------------------------------------------------
INSERT INTO `t_role` (`role_name`, `role_code`, `description`, `sort_order`, `status`) VALUES
('超级管理员', 'ROLE_ADMIN', '系统超级管理员，拥有所有权限', 1, 1),
('普通用户', 'ROLE_USER', '普通用户，拥有基础权限', 2, 1),
('财务主管', 'ROLE_FINANCE', '财务管理权限', 3, 1),
('采购主管', 'ROLE_PURCHASE', '采购管理权限', 4, 1),
('销售主管', 'ROLE_SALES', '销售管理权限', 5, 1),
('仓库主管', 'ROLE_WAREHOUSE', '仓储管理权限', 6, 1),
('生产主管', 'ROLE_PRODUCTION', '生产管理权限', 7, 1),
('HR主管', 'ROLE_HR', '人力资源管理权限', 8, 1);

-- -----------------------------------------------------------
-- 默认管理员用户由 DataInitializer 自动创建，密码使用 BCrypt 实时加密

-- -----------------------------------------------------------
-- 默认部门
-- -----------------------------------------------------------
INSERT INTO `t_department` (`parent_id`, `dept_name`, `dept_code`, `sort_order`, `status`) VALUES
(0, '总经办', 'ZJB', 1, 1),
(0, '财务部', 'CWB', 2, 1),
(0, '采购部', 'CGB', 3, 1),
(0, '销售部', 'XSB', 4, 1),
(0, '仓储部', 'CCB', 5, 1),
(0, '生产部', 'SCB', 6, 1),
(0, '人力资源部', 'RLZYB', 7, 1);

-- -----------------------------------------------------------
-- 默认菜单（多级菜单树，覆盖8大模块）
-- -----------------------------------------------------------
-- 一级目录
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(1, 0, '系统管理', 0, '/system', NULL, NULL, 'system', 1, 1, 1),
(2, 0, '财务管理', 0, '/finance', NULL, NULL, 'finance', 2, 1, 1),
(3, 0, '采购管理', 0, '/purchase', NULL, NULL, 'purchase', 3, 1, 1),
(4, 0, '销售管理', 0, '/sales', NULL, NULL, 'sales', 4, 1, 1),
(5, 0, '库存管理', 0, '/inventory', NULL, NULL, 'inventory', 5, 1, 1),
(6, 0, '生产管理', 0, '/production', NULL, NULL, 'production', 6, 1, 1),
(7, 0, '人力资源', 0, '/hr', NULL, NULL, 'hr', 7, 1, 1),
(8, 0, '日志审计', 0, '/audit', NULL, NULL, 'audit', 8, 1, 1);

-- 系统管理子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(101, 1, '用户管理', 1, 'user', 'system/user/index', 'system:user:list', 'user', 1, 1, 1),
(102, 1, '角色管理', 1, 'role', 'system/role/index', 'system:role:list', 'role', 2, 1, 1),
(103, 1, '菜单管理', 1, 'menu', 'system/menu/index', 'system:menu:list', 'menu', 3, 1, 1),
(104, 1, '部门管理', 1, 'dept', 'system/dept/index', 'system:dept:list', 'dept', 4, 1, 1),
(105, 1, '数据字典', 1, 'dict', 'system/dict/index', 'system:dict:list', 'dict', 5, 1, 1);

-- 系统管理按钮
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(10101, 101, '用户新增', 2, NULL, NULL, 'system:user:add', NULL, 1, 1, 1),
(10102, 101, '用户编辑', 2, NULL, NULL, 'system:user:edit', NULL, 2, 1, 1),
(10103, 101, '用户删除', 2, NULL, NULL, 'system:user:delete', NULL, 3, 1, 1);

-- 财务管理子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(201, 2, '会计科目', 1, 'account-subject', 'finance/account-subject/index', 'finance:subject:list', 'subject', 1, 1, 1),
(202, 2, '记账凭证', 1, 'voucher', 'finance/voucher/index', 'finance:voucher:list', 'voucher', 2, 1, 1),
(203, 2, '应收账款', 1, 'receivable', 'finance/receivable/index', 'finance:receivable:list', 'receivable', 3, 1, 1),
(204, 2, '应付账款', 1, 'payable', 'finance/payable/index', 'finance:payable:list', 'payable', 4, 1, 1),
(205, 2, '固定资产', 1, 'fixed-asset', 'finance/fixed-asset/index', 'finance:asset:list', 'asset', 5, 1, 1),
(206, 2, '成本核算', 1, 'cost-sheet', 'finance/cost-sheet/index', 'finance:cost:list', 'cost', 6, 1, 1),
(207, 2, '税务发票', 1, 'tax-invoice', 'finance/tax-invoice/index', 'finance:tax:list', 'tax', 7, 1, 1);

-- 采购管理子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(301, 3, '供应商管理', 1, 'supplier', 'purchase/supplier/index', 'purchase:supplier:list', 'supplier', 1, 1, 1),
(302, 3, '采购申请', 1, 'purchase-request', 'purchase/request/index', 'purchase:request:list', 'request', 2, 1, 1),
(303, 3, '采购订单', 1, 'purchase-order', 'purchase/order/index', 'purchase:order:list', 'order', 3, 1, 1),
(304, 3, '来料检验', 1, 'iqc-inspection', 'purchase/inspection/index', 'purchase:inspection:list', 'inspection', 4, 1, 1),
(305, 3, '采购退货', 1, 'purchase-return', 'purchase/return/index', 'purchase:return:list', 'return', 5, 1, 1),
(306, 3, '采购价格', 1, 'purchase-price', 'purchase/price/index', 'purchase:price:list', 'price', 6, 1, 1);

-- 销售管理子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(401, 4, '客户管理', 1, 'customer', 'sales/customer/index', 'sales:customer:list', 'customer', 1, 1, 1),
(402, 4, '销售报价', 1, 'sales-quotation', 'sales/quotation/index', 'sales:quotation:list', 'quotation', 2, 1, 1),
(403, 4, '销售订单', 1, 'sales-order', 'sales/order/index', 'sales:order:list', 'order', 3, 1, 1),
(404, 4, '销售发货', 1, 'sales-delivery', 'sales/delivery/index', 'sales:delivery:list', 'delivery', 4, 1, 1),
(405, 4, '销售退货', 1, 'sales-return', 'sales/return/index', 'sales:return:list', 'return', 5, 1, 1);

-- 库存管理子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(501, 5, '仓库管理', 1, 'warehouse', 'inventory/warehouse/index', 'inventory:warehouse:list', 'warehouse', 1, 1, 1),
(502, 5, '库位管理', 1, 'location', 'inventory/location/index', 'inventory:location:list', 'location', 2, 1, 1),
(503, 5, '物料管理', 1, 'material', 'inventory/material/index', 'inventory:material:list', 'material', 3, 1, 1),
(504, 5, '库存查询', 1, 'inventory-query', 'inventory/query/index', 'inventory:query:list', 'query', 4, 1, 1),
(505, 5, '入库管理', 1, 'stock-in', 'inventory/stock-in/index', 'inventory:stockIn:list', 'stock-in', 5, 1, 1),
(506, 5, '出库管理', 1, 'stock-out', 'inventory/stock-out/index', 'inventory:stockOut:list', 'stock-out', 6, 1, 1),
(507, 5, '调拨管理', 1, 'transfer', 'inventory/transfer/index', 'inventory:transfer:list', 'transfer', 7, 1, 1),
(508, 5, '盘点管理', 1, 'check', 'inventory/check/index', 'inventory:check:list', 'check', 8, 1, 1),
(509, 5, '库存预警', 1, 'stock-alert', 'inventory/alert/index', 'inventory:alert:list', 'alert', 9, 1, 1);

-- 生产管理子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(601, 6, 'BOM管理', 1, 'bom', 'production/bom/index', 'production:bom:list', 'bom', 1, 1, 1),
(602, 6, '主生产计划', 1, 'mps', 'production/mps/index', 'production:mps:list', 'mps', 2, 1, 1),
(603, 6, 'MRP结果', 1, 'mrp', 'production/mrp/index', 'production:mrp:list', 'mrp', 3, 1, 1),
(604, 6, '工单管理', 1, 'work-order', 'production/work-order/index', 'production:workOrder:list', 'work-order', 4, 1, 1),
(605, 6, '委外加工', 1, 'subcontract', 'production/subcontract/index', 'production:subcontract:list', 'subcontract', 5, 1, 1),
(606, 6, '生产检验', 1, 'prod-inspection', 'production/inspection/index', 'production:inspection:list', 'inspection', 6, 1, 1);

-- 人力资源管理子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(701, 7, '组织架构', 1, 'organization', 'hr/organization/index', 'hr:org:list', 'org', 1, 1, 1),
(702, 7, '员工管理', 1, 'employee', 'hr/employee/index', 'hr:employee:list', 'employee', 2, 1, 1),
(703, 7, '考勤管理', 1, 'attendance', 'hr/attendance/index', 'hr:attendance:list', 'attendance', 3, 1, 1),
(704, 7, '请假管理', 1, 'leave', 'hr/leave/index', 'hr:leave:list', 'leave', 4, 1, 1),
(705, 7, '工资管理', 1, 'salary', 'hr/salary/index', 'hr:salary:list', 'salary', 5, 1, 1),
(706, 7, '绩效考核', 1, 'performance', 'hr/performance/index', 'hr:performance:list', 'performance', 6, 1, 1);

-- 日志审计子菜单
INSERT INTO `t_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `status`) VALUES
(801, 8, '操作日志', 1, 'operation-log', 'audit/operation-log/index', 'audit:operationLog:list', 'log', 1, 1, 1),
(802, 8, '库存流水', 1, 'inventory-log', 'audit/inventory-log/index', 'audit:inventoryLog:list', 'inventory-log', 2, 1, 1),
(803, 8, '应收流水', 1, 'receivable-log', 'audit/receivable-log/index', 'audit:receivableLog:list', 'receivable-log', 3, 1, 1),
(804, 8, '应付流水', 1, 'payable-log', 'audit/payable-log/index', 'audit:payableLog:list', 'payable-log', 4, 1, 1),
(805, 8, '审批日志', 1, 'approval-log', 'audit/approval-log/index', 'audit:approvalLog:list', 'approval-log', 5, 1, 1);

-- -----------------------------------------------------------
-- 管理员角色分配
-- -----------------------------------------------------------
INSERT INTO `t_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- -----------------------------------------------------------
-- 默认数据字典
-- -----------------------------------------------------------
INSERT INTO `t_dictionary` (`dict_type`, `dict_label`, `dict_value`, `sort_order`, `status`) VALUES
-- 供应商级别
('supplier_level', 'A级（战略供应商）', 'A', 1, 1),
('supplier_level', 'B级（重要供应商）', 'B', 2, 1),
('supplier_level', 'C级（一般供应商）', 'C', 3, 1),
('supplier_level', 'D级（考察供应商）', 'D', 4, 1),
-- 客户级别
('customer_level', 'VIP客户', 'VIP', 1, 1),
('customer_level', 'A级客户', 'A', 2, 1),
('customer_level', 'B级客户', 'B', 3, 1),
('customer_level', 'C级客户', 'C', 4, 1),
-- 物料类别
('material_category', '原材料', 'RAW', 1, 1),
('material_category', '半成品', 'SEMI', 2, 1),
('material_category', '成品', 'FINISHED', 3, 1),
('material_category', '包材', 'PACKING', 4, 1),
('material_category', '备品备件', 'SPARE', 5, 1),
-- 付款方式
('payment_method', '银行转账', 'BANK_TRANSFER', 1, 1),
('payment_method', '现金', 'CASH', 2, 1),
('payment_method', '支票', 'CHEQUE', 3, 1),
('payment_method', '承兑汇票', 'ACCEPTANCE', 4, 1),
('payment_method', '信用证', 'LETTER_OF_CREDIT', 5, 1);
