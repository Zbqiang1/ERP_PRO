import request from '@/utils/request'

export function pageCustomer(params) {
  return request({ url: '/sales/customers', method: 'get', params })
}

export function getCustomerById(id) {
  return request({ url: `/sales/customers/${id}`, method: 'get' })
}

export function addCustomer(data) {
  return request({ url: '/sales/customers', method: 'post', data })
}

export function updateCustomer(id, data) {
  return request({ url: `/sales/customers/${id}`, method: 'put', data })
}

export function deleteCustomer(id) {
  return request({ url: `/sales/customers/${id}`, method: 'delete' })
}

// aliases matching Vue file usage
export const getById = getCustomerById
export const customerListAll = () => request({ url: '/sales/customers', method: 'get', params: { pageSize: 9999, pageNo: 1 } })
