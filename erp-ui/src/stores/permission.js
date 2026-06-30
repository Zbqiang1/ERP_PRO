import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'

// 组件路径映射表：后端组件名 → Vue 文件动态导入
const componentMap = {
  // 系统管理
  'system/UserList': () => import('@/views/system/UserList.vue'),
  'system/RoleList': () => import('@/views/system/RoleList.vue'),
  'system/MenuList': () => import('@/views/system/MenuList.vue'),
  'system/DepartmentList': () => import('@/views/system/DepartmentList.vue'),
  'system/DictionaryList': () => import('@/views/system/DictionaryList.vue'),
  'system/OperationLogList': () => import('@/views/system/OperationLogList.vue'),
  // 财务管理
  'finance/ReceivableList': () => import('@/views/finance/ReceivableList.vue'),
  'finance/PayableList': () => import('@/views/finance/PayableList.vue'),
  'finance/FixedAssetList': () => import('@/views/finance/FixedAssetList.vue'),
  'finance/CostSheetList': () => import('@/views/finance/CostSheetList.vue'),
  'finance/TaxInvoiceList': () => import('@/views/finance/TaxInvoiceList.vue'),
  // 采购管理
  'purchase/SupplierList': () => import('@/views/purchase/SupplierList.vue'),
  'purchase/PurchaseRequestList': () => import('@/views/purchase/PurchaseRequestList.vue'),
  'purchase/PurchaseOrderList': () => import('@/views/purchase/PurchaseOrderList.vue'),
  'purchase/IqcInspectionList': () => import('@/views/purchase/IqcInspectionList.vue'),
  'purchase/PurchaseReturnList': () => import('@/views/purchase/PurchaseReturnList.vue'),
  // 销售管理
  'sales/CustomerList': () => import('@/views/sales/CustomerList.vue'),
  'sales/SalesQuotationList': () => import('@/views/sales/SalesQuotationList.vue'),
  'sales/SalesOrderList': () => import('@/views/sales/SalesOrderList.vue'),
  'sales/SalesDeliveryList': () => import('@/views/sales/SalesDeliveryList.vue'),
  'sales/SalesReturnList': () => import('@/views/sales/SalesReturnList.vue'),
  // 库存仓储
  'inventory/WarehouseList': () => import('@/views/inventory/WarehouseList.vue'),
  'inventory/MaterialList': () => import('@/views/inventory/MaterialList.vue'),
  'inventory/StockInOrderList': () => import('@/views/inventory/StockInOrderList.vue'),
  'inventory/StockOutOrderList': () => import('@/views/inventory/StockOutOrderList.vue'),
  'inventory/InventoryBalance': () => import('@/views/inventory/InventoryBalance.vue'),
  // 生产管理
  'production/BomList': () => import('@/views/production/BomList.vue'),
  'production/MpsPlanList': () => import('@/views/production/MpsPlanList.vue'),
  'production/MrpResultList': () => import('@/views/production/MrpResultList.vue'),
  'production/WorkOrderList': () => import('@/views/production/WorkOrderList.vue'),
  'production/ProdInspectionList': () => import('@/views/production/ProdInspectionList.vue'),
  // 人力资源
  'hr/OrganizationTree': () => import('@/views/hr/OrganizationTree.vue'),
  'hr/EmployeeList': () => import('@/views/hr/EmployeeList.vue'),
  'hr/AttendanceList': () => import('@/views/hr/AttendanceList.vue'),
  'hr/SalarySheetList': () => import('@/views/hr/SalarySheetList.vue'),
  // 报表
  'dashboard/ErpDashboard': () => import('@/views/dashboard/ErpDashboard.vue')
}

// 根据 path 和菜单名推断组件路径
function guessComponent(path, menuName) {
  if (!path) return null
  // /system/user → system/UserList
  const parts = path.replace(/^\//, '').split('/')
  if (parts.length === 2) {
    const module = parts[0]          // system
    const page = parts[1]            // user
    const pageName = page.charAt(0).toUpperCase() + page.slice(1) + 'List'  // UserList
    return `${module}/${pageName}`
  }
  return null
}

// 将后端菜单数据转换为 Vue Router 路由格式
// 后端 MenuVO 字段: menuName, menuType, path, component, icon, sortOrder, permissionCode, visible, children
function convertMenusToRoutes(menus) {
  if (!menus || !Array.isArray(menus)) return []
  const routes = []
  for (const menu of menus) {
    const route = {
      path: menu.path || '',
      name: menu.path?.replace(/\//g, '-') || '',
      meta: {
        title: menu.menuName || '',
        icon: menu.icon || '',
        hidden: menu.visible === 0,
        perms: menu.permissionCode || ''
      }
    }
    // 目录类型(menuType=0)使用 redirect 到第一个子菜单
    if (menu.menuType === 0 && menu.children && menu.children.length > 0) {
      const firstChild = menu.children[0]
      if (firstChild.path) {
        route.redirect = firstChild.path
      }
    }
    // 菜单类型(menuType=1)使用懒加载组件
    if (menu.menuType === 1) {
      if (menu.component && componentMap[menu.component]) {
        route.component = componentMap[menu.component]
      } else if (menu.component) {
        // 不在映射表中，尝试动态加载
        route.component = () => import(`@/views/${menu.component}.vue`).catch((e) => {
          console.warn('组件加载失败:', menu.component, e)
          return { default: { template: '<div class="error-page">页面加载失败</div>' } }
        })
      } else if (menu.path) {
        const guessedComponent = guessComponent(menu.path, menu.menuName)
        if (guessedComponent && componentMap[guessedComponent]) {
          route.component = componentMap[guessedComponent]
        } else {
          console.warn('无法匹配组件:', menu.path, menu.menuName)
        }
      }
    }
    if (menu.children && menu.children.length > 0) {
      route.children = convertMenusToRoutes(menu.children)
    }
    routes.push(route)
  }
  // 按 sortOrder 排序
  routes.sort((a, b) => (a.meta?.orderNo || 0) - (b.meta?.orderNo || 0))
  return routes
}

export const usePermissionStore = defineStore('permission', () => {
  // State
  const routes = ref([])
  const dynamicRoutes = ref([])
  const menus = ref([])

  // Actions
  function setRoutes(newRoutes) {
    dynamicRoutes.value = newRoutes
    routes.value = [
      ...newRoutes,
      // 404 必须放在最后
      {
        path: '/:pathMatch(.*)*',
        redirect: '/404',
        meta: { hidden: true }
      }
    ]
  }

  async function generateRoutes() {
    try {
      const res = await request({
        url: '/menu/tree',
        method: 'get'
      })
      const menuList = res.data || []
      menus.value = menuList

      const convertedRoutes = convertMenusToRoutes(menuList)
      setRoutes(convertedRoutes)
      return convertedRoutes
    } catch (error) {
      console.error('获取菜单失败', error)
      return []
    }
  }

  function resetRoutes() {
    routes.value = []
    dynamicRoutes.value = []
    menus.value = []
  }

  return {
    routes,
    dynamicRoutes,
    menus,
    generateRoutes,
    setRoutes,
    resetRoutes
  }
})
