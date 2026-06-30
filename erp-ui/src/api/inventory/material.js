// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const materialPage = (params) => request({ url: '/inventory/materials', method: 'get', params })
export const materialAdd = (data) => request({ url: '/inventory/materials', method: 'post', data })
export const materialUpdate = (data) => request({ url: `/inventory/materials/${data.id}`, method: 'put', data })
export const materialDelete = (id) => request({ url: `/inventory/materials/${id}`, method: 'delete' })
export const materialGetById = (id) => request({ url: `/inventory/materials/${id}`, method: 'get' })
export const materialListAll = () => request({ url: '/inventory/materials', method: 'get', params: { pageSize: 9999, pageNo: 1 } })
