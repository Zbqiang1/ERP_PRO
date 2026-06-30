// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const transferOrderPage = (params) => request({ url: '/inventory/transfer-orders', method: 'get', params })
export const transferOrderAdd = (data) => request({ url: '/inventory/transfer-orders', method: 'post', data })
export const transferOrderUpdate = (data) => request({ url: `/inventory/transfer-orders/${data.id}`, method: 'put', data })
export const transferOrderDelete = (id) => request({ url: `/inventory/transfer-orders/${id}`, method: 'delete' })
export const transferOrderGetById = (id) => request({ url: `/inventory/transfer-orders/${id}`, method: 'get' })
