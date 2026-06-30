import request from '@/utils/request'

export function pageSupplier(params) {
  return request({ url: '/purchase/suppliers', method: 'get', params })
}

export function getSupplierById(id) {
  return request({ url: `/purchase/suppliers/${id}`, method: 'get' })
}

export function addSupplier(data) {
  return request({ url: '/purchase/suppliers', method: 'post', data })
}

export function updateSupplier(id, data) {
  return request({ url: `/purchase/suppliers/${id}`, method: 'put', data })
}

export function deleteSupplier(id) {
  return request({ url: `/purchase/suppliers/${id}`, method: 'delete' })
}

// aliases matching Vue file usage
export const getById = getSupplierById
export const supplierListAll = () => request({ url: '/purchase/suppliers', method: 'get', params: { pageSize: 9999, pageNo: 1 } })
