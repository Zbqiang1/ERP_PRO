import request from '@/utils/request'

export function pageDepartment(params) {
  return request({ url: '/departments', method: 'get', params })
}

export function getDepartmentById(id) {
  return request({ url: `/departments/${id}`, method: 'get' })
}

export function addDepartment(data) {
  return request({ url: '/departments', method: 'post', data })
}

export function updateDepartment(id, data) {
  return request({ url: `/departments/${id}`, method: 'put', data })
}

export function deleteDepartment(id) {
  return request({ url: `/departments/${id}`, method: 'delete' })
}

export function getDeptTree() {
  return request({ url: '/departments/tree', method: 'get' })
}

// aliases matching Vue file usage
export const getById = getDepartmentById
export const add = addDepartment
export const update = updateDepartment
export const deleteDept = deleteDepartment
