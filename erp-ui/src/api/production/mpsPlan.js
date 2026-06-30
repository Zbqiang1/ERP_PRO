// re-exports from @/api/modules/production with names matching Vue file usage
import request from '@/utils/request'

export const mpsPlanPage = (params) => request({ url: '/production/mps', method: 'get', params })
export const mpsPlanAdd = (data) => request({ url: '/production/mps', method: 'post', data })
export const mpsPlanUpdate = (data) => request({ url: `/production/mps/${data.id}`, method: 'put', data })
export const mpsPlanDelete = (id) => request({ url: `/production/mps/${id}`, method: 'delete' })
export const mpsPlanGetById = (id) => request({ url: `/production/mps/${id}`, method: 'get' })
export const mpsPlanListAll = () => request({ url: '/production/mps', method: 'get', params: { pageSize: 9999, pageNo: 1 } })
