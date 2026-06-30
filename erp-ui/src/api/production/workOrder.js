// re-exports from @/api/modules/production with names matching Vue file usage
import request from '@/utils/request'

export const workOrderPage = (params) => request({ url: '/production/work-orders', method: 'get', params })
export const workOrderAdd = (data) => request({ url: '/production/work-orders', method: 'post', data })
export const workOrderUpdate = (data) => request({ url: `/production/work-orders/${data.id}`, method: 'put', data })
export const workOrderDelete = (id) => request({ url: `/production/work-orders/${id}`, method: 'delete' })
export const workOrderGetById = (id) => request({ url: `/production/work-orders/${id}`, method: 'get' })
export const workOrderStart = (id) => request({ url: `/production/work-orders/${id}/start`, method: 'post' })
export const workOrderComplete = (id) => request({ url: `/production/work-orders/${id}/complete`, method: 'post' })
