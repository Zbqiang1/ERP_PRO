// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const stockInOrderPage = (params) => request({ url: '/inventory/stock-in-orders', method: 'get', params })
export const stockInOrderAdd = (data) => request({ url: '/inventory/stock-in-orders', method: 'post', data })
export const stockInOrderUpdate = (data) => request({ url: `/inventory/stock-in-orders/${data.id}`, method: 'put', data })
export const stockInOrderDelete = (id) => request({ url: `/inventory/stock-in-orders/${id}`, method: 'delete' })
export const stockInOrderGetById = (id) => request({ url: `/inventory/stock-in-orders/${id}`, method: 'get' })
export const stockInOrderConfirm = (id) => request({ url: `/inventory/stock-in-orders/${id}/confirm`, method: 'post' })
