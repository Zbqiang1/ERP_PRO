import request from '@/utils/request'

// purchaseRequest
export function pageRequest(params) {
  return request({ url: '/purchase/requests', method: 'get', params })
}

export function getRequestById(id) {
  return request({ url: `/purchase/requests/${id}`, method: 'get' })
}

export function addRequest(data) {
  return request({ url: '/purchase/requests', method: 'post', data })
}

export function updateRequest(id, data) {
  return request({ url: `/purchase/requests/${id}`, method: 'put', data })
}

export function deleteRequest(id) {
  return request({ url: `/purchase/requests/${id}`, method: 'delete' })
}

export function submitApproval(id) {
  return request({ url: `/purchase/requests/${id}/approve`, method: 'put' })
}

export function rejectRequest(id) {
  return request({ url: `/purchase/requests/${id}/reject`, method: 'put' })
}

// purchaseOrder
export function pageOrder(params) {
  return request({ url: '/purchase/orders', method: 'get', params })
}

export function getOrderById(id) {
  return request({ url: `/purchase/orders/${id}`, method: 'get' })
}

export function addOrder(data) {
  return request({ url: '/purchase/orders', method: 'post', data })
}

export function updateOrder(id, data) {
  return request({ url: `/purchase/orders/${id}`, method: 'put', data })
}

export function deleteOrder(id) {
  return request({ url: `/purchase/orders/${id}`, method: 'delete' })
}

export function confirmOrder(id) {
  return request({ url: `/purchase/orders/${id}/confirm`, method: 'put' })
}

// iqcInspection
export function pageInspection(params) {
  return request({ url: '/purchase/inspections', method: 'get', params })
}

export function getInspectionById(id) {
  return request({ url: `/purchase/inspections/${id}`, method: 'get' })
}

export function addInspection(data) {
  return request({ url: '/purchase/inspections', method: 'post', data })
}

export function updateInspection(id, data) {
  return request({ url: `/purchase/inspections/${id}`, method: 'put', data })
}

// purchaseReturn
export function pageReturn(params) {
  return request({ url: '/purchase/returns', method: 'get', params })
}

export function getReturnById(id) {
  return request({ url: `/purchase/returns/${id}`, method: 'get' })
}

export function addReturn(data) {
  return request({ url: '/purchase/returns', method: 'post', data })
}

export function updateReturn(id, data) {
  return request({ url: `/purchase/returns/${id}`, method: 'put', data })
}

export function deleteReturn(id) {
  return request({ url: `/purchase/returns/${id}`, method: 'delete' })
}

// ── namespace-export compatibility objects for Vue files ──
export const purchaseRequest = {
  page: (params) => request({ url: '/purchase/requests', method: 'get', params }),
  getById: (id) => request({ url: `/purchase/requests/${id}`, method: 'get' }),
  add: (data) => request({ url: '/purchase/requests', method: 'post', data }),
  update: (data) => request({ url: `/purchase/requests/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/purchase/requests/${id}`, method: 'delete' }),
  submitApproval: (id) => request({ url: `/purchase/requests/${id}/approve`, method: 'put' }),
  reject: (id) => request({ url: `/purchase/requests/${id}/reject`, method: 'put' })
}

export const purchaseOrder = {
  page: (params) => request({ url: '/purchase/orders', method: 'get', params }),
  getById: (id) => request({ url: `/purchase/orders/${id}`, method: 'get' }),
  add: (data) => request({ url: '/purchase/orders', method: 'post', data }),
  update: (data) => request({ url: `/purchase/orders/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/purchase/orders/${id}`, method: 'delete' }),
  confirm: (id) => request({ url: `/purchase/orders/${id}/confirm`, method: 'put' })
}

export const purchaseReturn = {
  page: (params) => request({ url: '/purchase/returns', method: 'get', params }),
  getById: (id) => request({ url: `/purchase/returns/${id}`, method: 'get' }),
  add: (data) => request({ url: '/purchase/returns', method: 'post', data }),
  update: (data) => request({ url: `/purchase/returns/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/purchase/returns/${id}`, method: 'delete' })
}

export const iqcInspection = {
  page: (params) => request({ url: '/purchase/inspections', method: 'get', params }),
  getById: (id) => request({ url: `/purchase/inspections/${id}`, method: 'get' }),
  add: (data) => request({ url: '/purchase/inspections', method: 'post', data }),
  update: (data) => request({ url: `/purchase/inspections/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/purchase/inspections/${id}`, method: 'delete' })
}
