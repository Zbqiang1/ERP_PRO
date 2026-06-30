// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const inventoryCheckPage = (params) => request({ url: '/inventory/inventory-checks', method: 'get', params })
export const inventoryCheckAdd = (data) => request({ url: '/inventory/inventory-checks', method: 'post', data })
export const inventoryCheckUpdate = (data) => request({ url: `/inventory/checks/${data.id}`, method: 'put', data })
export const inventoryCheckDelete = (id) => request({ url: `/inventory/checks/${id}`, method: 'delete' })
export const inventoryCheckGetById = (id) => request({ url: `/inventory/checks/${id}`, method: 'get' })
export const inventoryCheckConfirm = (id) => request({ url: `/inventory/inventory-checks/${id}/confirm`, method: 'post' })
export const inventoryCheckAdjust = (data) => request({ url: '/inventory/inventory-checks/adjust', method: 'post', data })
