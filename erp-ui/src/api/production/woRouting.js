// re-exports from @/api/modules/production with names matching Vue file usage
import request from '@/utils/request'

export const woRoutingPage = (params) => request({ url: '/production/work-orders/routing', method: 'get', params })
export const woRoutingAdd = (data) => request({ url: '/production/work-orders/routing', method: 'post', data })
export const woRoutingUpdate = (data) => request({ url: '/production/work-orders/routing', method: 'put', data })
export const woRoutingDelete = (id) => request({ url: `/production/work-orders/routing/${id}`, method: 'delete' })
export const woRoutingComplete = (id) => request({ url: `/production/work-orders/routing/${id}/complete`, method: 'post' })
export const woRoutingAssignWorker = (id, workerId) => request({ url: `/production/work-orders/routing/${id}/assign`, method: 'put', data: { workerId } })
