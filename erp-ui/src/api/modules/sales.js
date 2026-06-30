import request from '@/utils/request'

// salesQuotation
export function pageQuotation(params) {
  return request({ url: '/sales/quotations', method: 'get', params })
}

export function getQuotationById(id) {
  return request({ url: `/sales/quotations/${id}`, method: 'get' })
}

export function addQuotation(data) {
  return request({ url: '/sales/quotations', method: 'post', data })
}

export function updateQuotation(id, data) {
  return request({ url: `/sales/quotations/${id}`, method: 'put', data })
}

export function deleteQuotation(id) {
  return request({ url: `/sales/quotations/${id}`, method: 'delete' })
}

// salesOrder
export function pageOrder(params) {
  return request({ url: '/sales/orders', method: 'get', params })
}

export function getOrderById(id) {
  return request({ url: `/sales/orders/${id}`, method: 'get' })
}

export function addOrder(data) {
  return request({ url: '/sales/orders', method: 'post', data })
}

export function updateOrder(id, data) {
  return request({ url: `/sales/orders/${id}`, method: 'put', data })
}

export function deleteOrder(id) {
  return request({ url: `/sales/orders/${id}`, method: 'delete' })
}

export function confirmOrder(id) {
  return request({ url: `/sales/orders/${id}/confirm`, method: 'put' })
}

export function closeOrder(id) {
  return request({ url: `/sales/orders/${id}/close`, method: 'post' })
}

// salesDelivery
export function pageDelivery(params) {
  return request({ url: '/sales/deliveries', method: 'get', params })
}

export function getDeliveryById(id) {
  return request({ url: `/sales/deliveries/${id}`, method: 'get' })
}

export function addDelivery(data) {
  return request({ url: '/sales/deliveries', method: 'post', data })
}

export function updateDelivery(id, data) {
  return request({ url: `/sales/deliveries/${id}`, method: 'put', data })
}

export function deleteDelivery(id) {
  return request({ url: `/sales/deliveries/${id}`, method: 'delete' })
}

export function confirmDelivery(id) {
  return request({ url: `/sales/deliveries/${id}/deliver`, method: 'put' })
}

export function signDelivery(id) {
  return request({ url: `/sales/deliveries/${id}/sign`, method: 'put' })
}

// salesReturn
export function pageReturn(params) {
  return request({ url: '/sales/returns', method: 'get', params })
}

export function getReturnById(id) {
  return request({ url: `/sales/returns/${id}`, method: 'get' })
}

export function addReturn(data) {
  return request({ url: '/sales/returns', method: 'post', data })
}

export function updateReturn(id, data) {
  return request({ url: `/sales/returns/${id}`, method: 'put', data })
}

export function deleteReturn(id) {
  return request({ url: `/sales/returns/${id}`, method: 'delete' })
}

// ── namespace-export compatibility objects for Vue files ──
export const salesQuotation = {
  page: (params) => request({ url: '/sales/quotations', method: 'get', params }),
  getById: (id) => request({ url: `/sales/quotations/${id}`, method: 'get' }),
  add: (data) => request({ url: '/sales/quotations', method: 'post', data }),
  update: (data) => request({ url: `/sales/quotations/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/sales/quotations/${id}`, method: 'delete' }),
  confirm: (id) => request({ url: `/sales/quotations/${id}/confirm`, method: 'put' })
}

export const salesOrder = {
  page: (params) => request({ url: '/sales/orders', method: 'get', params }),
  getById: (id) => request({ url: `/sales/orders/${id}`, method: 'get' }),
  add: (data) => request({ url: '/sales/orders', method: 'post', data }),
  update: (data) => request({ url: `/sales/orders/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/sales/orders/${id}`, method: 'delete' }),
  confirm: (id) => request({ url: `/sales/orders/${id}/confirm`, method: 'put' }),
  close: (id) => request({ url: `/sales/orders/${id}/close`, method: 'post' }),
  report: (params) => request({ url: '/sales/orders/report', method: 'get', params })
}

export const salesDelivery = {
  page: (params) => request({ url: '/sales/deliveries', method: 'get', params }),
  getById: (id) => request({ url: `/sales/deliveries/${id}`, method: 'get' }),
  add: (data) => request({ url: '/sales/deliveries', method: 'post', data }),
  update: (data) => request({ url: `/sales/deliveries/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/sales/deliveries/${id}`, method: 'delete' }),
  confirm: (id) => request({ url: `/sales/deliveries/${id}/deliver`, method: 'put' }),
  sign: (id) => request({ url: `/sales/deliveries/${id}/sign`, method: 'put' })
}

export const salesReturn = {
  page: (params) => request({ url: '/sales/returns', method: 'get', params }),
  getById: (id) => request({ url: `/sales/returns/${id}`, method: 'get' }),
  add: (data) => request({ url: '/sales/returns', method: 'post', data }),
  update: (data) => request({ url: `/sales/returns/${data.id}`, method: 'put', data }),
  remove: (id) => request({ url: `/sales/returns/${id}`, method: 'delete' })
}
