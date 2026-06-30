package com.erp.common.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.erp.modules.system.entity.*;
import com.erp.modules.system.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据初始化器
 *
 * 应用启动时自动检查并创建默认管理员账号、角色和菜单，
 * 无需手动执行 SQL 初始化脚本即可登录系统。
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            initRoles();
            initMenus();
            initAdminUser();
            assignAdminPermissions();
        } catch (Exception e) {
            log.warn("自动初始化数据失败（可能数据库表尚未创建，将由 Spring SQL Init 自动处理）: {}", e.getMessage());
            log.info("请确保 MySQL 已启动，应用将自动创建数据库表和默认账号");
        }
    }

    /**
     * 初始化默认角色
     */
    private void initRoles() {
        createRoleIfAbsent("超级管理员", "ROLE_ADMIN", "系统超级管理员，拥有所有权限", 1);
        createRoleIfAbsent("普通用户", "ROLE_USER", "系统普通用户", 2);
        log.info("默认角色初始化完成");
    }

    /**
     * 初始化默认管理员用户
     */
    private void initAdminUser() {
        // 检查管理员是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, "admin");
        User existingUser = userMapper.selectOne(wrapper);

        if (existingUser != null) {
            // 已存在则更新密码（保证密码一定是 123456 的正确 BCrypt 加密值）
            existingUser.setPassword(passwordEncoder.encode("123456"));
            existingUser.setStatus(1);
            userMapper.updateById(existingUser);
            log.info("管理员账号已存在，密码已重置为: 123456");
            return;
        }

        // 创建默认管理员
        User admin = new User();
        admin.setUserId("admin");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRealName("系统管理员");
        admin.setEmail("admin@erp.com");
        admin.setPhone("13800000000");
        admin.setStatus(1);

        userMapper.insert(admin);
        log.info("========================================");
        log.info("  默认管理员账号已创建");
        log.info("  用户名: admin");
        log.info("  密码: 123456");
        log.info("========================================");
    }

    /**
     * 初始化默认菜单
     */
    private void initMenus() {
        if (menuMapper.selectCount(null) > 0) {
            log.info("菜单数据已存在，跳过初始化");
            return;
        }

        // 一级菜单
        Long systemMgr = createMenu(0L, "系统管理", 0, "/system", "system", "system", 1, 1);
        Long financeMgr = createMenu(0L, "财务管理", 0, "/finance", "finance", "finance", 2, 1);
        Long purchaseMgr = createMenu(0L, "采购管理", 0, "/purchase", "purchase", "purchase", 3, 1);
        Long salesMgr = createMenu(0L, "销售管理", 0, "/sales", "sales", "sales", 4, 1);
        Long inventoryMgr = createMenu(0L, "库存仓储", 0, "/inventory", "inventory", "inventory", 5, 1);
        Long productionMgr = createMenu(0L, "生产管理", 0, "/production", "production", "production", 6, 1);
        Long hrMgr = createMenu(0L, "人力资源", 0, "/hr", "hr", "hr", 7, 1);
        Long reportMgr = createMenu(0L, "数据报表", 0, "/report", "report", "report", 8, 1);

        // 系统管理子菜单
        createMenu(systemMgr, "用户管理", 1, "/system/user", "system/UserList", "system:user:list", 1, 1);
        createMenu(systemMgr, "角色管理", 1, "/system/role", "system/RoleList", "system:role:list", 2, 1);
        createMenu(systemMgr, "菜单管理", 1, "/system/menu", "system/MenuList", "system:menu:list", 3, 1);
        createMenu(systemMgr, "部门管理", 1, "/system/department", "system/DepartmentList", "system:dept:list", 4, 1);
        createMenu(systemMgr, "字典管理", 1, "/system/dictionary", "system/DictionaryList", "system:dict:list", 5, 1);
        createMenu(systemMgr, "操作日志", 1, "/system/log", "system/OperationLogList", "system:log:list", 6, 1);

        // 财务管理子菜单
        createMenu(financeMgr, "应收管理", 1, "/finance/receivable", "finance/ReceivableList", "finance:receivable:list", 1, 1);
        createMenu(financeMgr, "应付管理", 1, "/finance/payable", "finance/PayableList", "finance:payable:list", 2, 1);
        createMenu(financeMgr, "固定资产", 1, "/finance/asset", "finance/FixedAssetList", "finance:asset:list", 3, 1);
        createMenu(financeMgr, "成本核算", 1, "/finance/cost", "finance/CostSheetList", "finance:cost:list", 4, 1);
        createMenu(financeMgr, "税务发票", 1, "/finance/invoice", "finance/TaxInvoiceList", "finance:invoice:list", 5, 1);

        // 采购管理子菜单
        createMenu(purchaseMgr, "供应商管理", 1, "/purchase/supplier", "purchase/SupplierList", "purchase:supplier:list", 1, 1);
        createMenu(purchaseMgr, "采购申请", 1, "/purchase/request", "purchase/PurchaseRequestList", "purchase:request:list", 2, 1);
        createMenu(purchaseMgr, "采购订单", 1, "/purchase/order", "purchase/PurchaseOrderList", "purchase:order:list", 3, 1);
        createMenu(purchaseMgr, "来料质检", 1, "/purchase/inspection", "purchase/IqcInspectionList", "purchase:iqc:list", 4, 1);
        createMenu(purchaseMgr, "采购退货", 1, "/purchase/return", "purchase/PurchaseReturnList", "purchase:return:list", 5, 1);

        // 销售管理子菜单
        createMenu(salesMgr, "客户管理", 1, "/sales/customer", "sales/CustomerList", "sales:customer:list", 1, 1);
        createMenu(salesMgr, "销售报价", 1, "/sales/quotation", "sales/SalesQuotationList", "sales:quotation:list", 2, 1);
        createMenu(salesMgr, "销售订单", 1, "/sales/order", "sales/SalesOrderList", "sales:order:list", 3, 1);
        createMenu(salesMgr, "发货管理", 1, "/sales/delivery", "sales/SalesDeliveryList", "sales:delivery:list", 4, 1);
        createMenu(salesMgr, "销售退货", 1, "/sales/return", "sales/SalesReturnList", "sales:return:list", 5, 1);

        // 库存仓储子菜单
        createMenu(inventoryMgr, "仓库管理", 1, "/inventory/warehouse", "inventory/WarehouseList", "inventory:warehouse:list", 1, 1);
        createMenu(inventoryMgr, "物料档案", 1, "/inventory/material", "inventory/MaterialList", "inventory:material:list", 2, 1);
        createMenu(inventoryMgr, "入库管理", 1, "/inventory/stock-in", "inventory/StockInOrderList", "inventory:stockin:list", 3, 1);
        createMenu(inventoryMgr, "出库管理", 1, "/inventory/stock-out", "inventory/StockOutOrderList", "inventory:stockout:list", 4, 1);
        createMenu(inventoryMgr, "库存查询", 1, "/inventory/balance", "inventory/InventoryBalance", "inventory:balance:list", 5, 1);

        // 生产管理子菜单
        createMenu(productionMgr, "BOM管理", 1, "/production/bom", "production/BomList", "production:bom:list", 1, 1);
        createMenu(productionMgr, "生产计划", 1, "/production/plan", "production/MpsPlanList", "production:plan:list", 2, 1);
        createMenu(productionMgr, "MRP运算", 1, "/production/mrp", "production/MrpResultList", "production:mrp:list", 3, 1);
        createMenu(productionMgr, "生产工单", 1, "/production/work-order", "production/WorkOrderList", "production:wo:list", 4, 1);
        createMenu(productionMgr, "生产质检", 1, "/production/inspection", "production/ProdInspectionList", "production:inspection:list", 5, 1);

        // HR子菜单
        createMenu(hrMgr, "组织架构", 1, "/hr/organization", "hr/OrganizationTree", "hr:org:list", 1, 1);
        createMenu(hrMgr, "员工管理", 1, "/hr/employee", "hr/EmployeeList", "hr:emp:list", 2, 1);
        createMenu(hrMgr, "考勤管理", 1, "/hr/attendance", "hr/AttendanceList", "hr:att:list", 3, 1);
        createMenu(hrMgr, "薪资管理", 1, "/hr/salary", "hr/SalarySheetList", "hr:salary:list", 4, 1);

        // 报表子菜单
        createMenu(reportMgr, "数据看板", 1, "/report/dashboard-page", "dashboard/ErpDashboard", "report:dashboard:view", 1, 1);

        log.info("默认菜单初始化完成（8个一级菜单，35个子菜单）");
    }

    private Long createMenu(Long parentId, String menuName, int menuType, String path, String component, String perms, int sortOrder, int visible) {
        // 检查是否已存在
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getMenuName, menuName).eq(Menu::getParentId, parentId);
        Menu existing = menuMapper.selectOne(wrapper);
        if (existing != null) {
            return existing.getId();
        }
        Menu menu = new Menu();
        menu.setParentId(parentId);
        menu.setMenuName(menuName);
        menu.setMenuType(menuType);
        menu.setPath(path);
        menu.setComponent(component);
        menu.setPerms(perms);
        menu.setSortOrder(sortOrder);
        menu.setVisible(visible);
        menu.setStatus(1);
        menuMapper.insert(menu);
        return menu.getId();
    }

    /**
     * 为管理员分配所有权限（角色 + 菜单）
     */
    private void assignAdminPermissions() {
        // 查找 admin 用户和 ROLE_ADMIN 角色
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUsername, "admin");
        User admin = userMapper.selectOne(userWrapper);

        LambdaQueryWrapper<Role> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(Role::getRoleCode, "ROLE_ADMIN");
        Role adminRole = roleMapper.selectOne(roleWrapper);

        if (admin == null || adminRole == null) {
            log.warn("无法分配权限：admin用户或ROLE_ADMIN角色不存在");
            return;
        }

        // 1. 分配 admin → ROLE_ADMIN 角色关联
        LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(UserRole::getUserId, String.valueOf(admin.getId()));
        if (userRoleMapper.selectCount(userRoleWrapper) == 0) {
            UserRole userRole = new UserRole();
            userRole.setUserId(String.valueOf(admin.getId()));
            userRole.setRoleId(adminRole.getId());
            userRoleMapper.insert(userRole);
            log.info("已为 admin 分配超级管理员角色");
        }

        // 2. 为 ROLE_ADMIN 分配所有菜单权限
        List<Menu> allMenus = menuMapper.selectList(null);
        for (Menu menu : allMenus) {
            LambdaQueryWrapper<RoleMenu> rmWrapper = new LambdaQueryWrapper<>();
            rmWrapper.eq(RoleMenu::getRoleId, adminRole.getId())
                    .eq(RoleMenu::getMenuId, menu.getId());
            if (roleMenuMapper.selectCount(rmWrapper) == 0) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(adminRole.getId());
                roleMenu.setMenuId(menu.getId());
                roleMenuMapper.insert(roleMenu);
            }
        }
        log.info("已为超级管理员分配 {} 个菜单权限", allMenus.size());
    }

    /**
     * 创建角色（如果不存在）
     */
    private void createRoleIfAbsent(String roleName, String roleCode, String description, int sortOrder) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, roleCode);
        if (roleMapper.selectCount(wrapper) == 0) {
            Role role = new Role();
            role.setRoleName(roleName);
            role.setRoleCode(roleCode);
            role.setDescription(description);
            role.setSortOrder(sortOrder);
            role.setStatus(1);
            roleMapper.insert(role);
        }
    }
}
