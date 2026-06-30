// re-exports with names matching Vue file usage
import request from '@/utils/request'

export const dashboardGetStats = () => request({ url: '/report/dashboard/stats', method: 'get' })
export const dashboardGetSalesTrend = () => request({ url: '/report/dashboard/sales-trend', method: 'get' })
export const dashboardGetTopProducts = () => request({ url: '/report/dashboard/top-products', method: 'get' })
export const dashboardGetPendingOrders = () => request({ url: '/report/dashboard/pending-orders', method: 'get' })
export const dashboardGetInventoryAlert = () => request({ url: '/report/dashboard/inventory-alert', method: 'get' })
export const dashboardGetProductionRate = () => request({ url: '/report/dashboard/production-rate', method: 'get' })
