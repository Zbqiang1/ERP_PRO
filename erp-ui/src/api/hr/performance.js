// re-exports from @/api/modules/hr with names matching Vue file usage
import request from '@/utils/request'

export const performancePage = (params) => request({ url: '/hr/performances', method: 'get', params })
export const performanceAdd = (data) => request({ url: '/hr/performances', method: 'post', data })
export const performanceUpdate = (data) => request({ url: `/hr/performances/${data.id}`, method: 'put', data })
export const performanceDelete = (id) => request({ url: `/hr/performances/${id}`, method: 'delete' })
export const performanceGetById = (id) => request({ url: `/hr/performances/${id}`, method: 'get' })
export const performanceHistoryByEmployee = (employeeId) => request({ url: `/hr/performances/history/${employeeId}`, method: 'get' })
