import request from '@/utils/request'

export function pageMenu(params) {
  return request({ url: '/menu', method: 'get', params })
}

export function getMenuById(id) {
  return request({ url: `/menu/${id}`, method: 'get' })
}

export function addMenu(data) {
  return request({ url: '/menu', method: 'post', data })
}

export function updateMenu(id, data) {
  return request({ url: `/menu/${id}`, method: 'put', data })
}

export function deleteMenu(id) {
  return request({ url: `/menu/${id}`, method: 'delete' })
}

export function getMenuTree() {
  return request({ url: '/menu/tree', method: 'get' })
}

export function getMenuTreeByUserId(userId) {
  return request({ url: `/menu/tree/${userId}`, method: 'get' })
}
