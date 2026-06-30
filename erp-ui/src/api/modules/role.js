import request from '@/utils/request'

export function pageRole(params) {
  return request({ url: '/roles', method: 'get', params })
}

export function getRoleById(id) {
  return request({ url: `/roles/${id}`, method: 'get' })
}

export function addRole(data) {
  return request({ url: '/roles', method: 'post', data })
}

export function updateRole(id, data) {
  return request({ url: `/roles/${id}`, method: 'put', data })
}

export function deleteRole(id) {
  return request({ url: `/roles/${id}`, method: 'delete' })
}

export function assignMenus(roleId, menuIds) {
  return request({ url: `/roles/${roleId}/menus`, method: 'put', data: menuIds })
}

export function listAllEnabled() {
  return request({ url: '/roles/all', method: 'get' })
}
