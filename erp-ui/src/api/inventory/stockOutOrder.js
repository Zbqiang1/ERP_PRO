// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const stockOutOrderPage = (params) => request({ url: '/inventory/stock-out-orders', method: 'get', params })
export const stockOutOrderAdd = (data) => request({ url: '/inventory/stock-out-orders', method: 'post', data })
export const stockOutOrderUpdate = (data) => request({ url: `/inventory/stock-out-orders/${data.id}`, method: 'put', data })
export const stockOutOrderDelete = (id) => request({ url: `/inventory/stock-out-orders/${id}`, method: 'delete' })
export const stockOutOrderGetById = (id) => request({ url: `/inventory/stock-out-orders/${id}`, method: 'get' })
export const stockOutOrderConfirm = (id) => request({ url: `/inventory/stock-out-orders/${id}/confirm`, method: 'post' })
