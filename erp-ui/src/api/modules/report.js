import request from '@/utils/request'

// dashboardWidget
export function listDashboardWidgets() {
  return request({ url: '/report/dashboard', method: 'get' })
}

export function updateDashboardLayout(data) {
  return request({ url: '/report/dashboard', method: 'put', data })
}

// reportTemplate
export function pageReportTemplate(params) {
  return request({ url: '/report/reports', method: 'get', params })
}

export function getReportTemplateById(id) {
  return request({ url: `/report/reports/${id}`, method: 'get' })
}

export function addReportTemplate(data) {
  return request({ url: '/report/reports', method: 'post', data })
}

export function updateReportTemplate(id, data) {
  return request({ url: `/report/reports/${id}`, method: 'put', data })
}

export function deleteReportTemplate(id) {
  return request({ url: `/report/reports/${id}`, method: 'delete' })
}

export function executeReport(id, params) {
  return request({ url: `/report/reports/${id}/execute`, method: 'post', data: params })
}

// businessAlert
export function listBusinessAlerts() {
  return request({ url: '/report/alerts', method: 'get' })
}

export function getBusinessAlertById(id) {
  return request({ url: `/report/alerts/${id}`, method: 'get' })
}

export function addBusinessAlert(data) {
  return request({ url: '/report/alerts', method: 'post', data })
}

export function updateBusinessAlert(id, data) {
  return request({ url: `/report/alerts/${id}`, method: 'put', data })
}

export function deleteBusinessAlert(id) {
  return request({ url: `/report/alerts/${id}`, method: 'delete' })
}

// ── dashboard aliases matching Vue file usage ──
export const dashboardGetStats = () => request({ url: '/report/dashboard/stats', method: 'get' })
export const dashboardGetSalesTrend = () => request({ url: '/report/dashboard/sales-trend', method: 'get' })
export const dashboardGetTopProducts = () => request({ url: '/report/dashboard/top-products', method: 'get' })
export const dashboardGetPendingOrders = () => request({ url: '/report/dashboard/pending-orders', method: 'get' })
export const dashboardGetInventoryAlert = () => request({ url: '/report/dashboard/inventory-alert', method: 'get' })
export const dashboardGetProductionRate = () => request({ url: '/report/dashboard/production-rate', method: 'get' })
