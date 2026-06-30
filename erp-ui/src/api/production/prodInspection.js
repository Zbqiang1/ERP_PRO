// re-exports from @/api/modules/production with names matching Vue file usage
import request from '@/utils/request'

export const prodInspectionPage = (params) => request({ url: '/production/inspections', method: 'get', params })
export const prodInspectionAdd = (data) => request({ url: '/production/inspections', method: 'post', data })
export const prodInspectionUpdate = (data) => request({ url: `/production/inspections/${data.id}`, method: 'put', data })
export const prodInspectionDelete = (id) => request({ url: `/production/inspections/${id}`, method: 'delete' })
export const prodInspectionGetById = (id) => request({ url: `/production/inspections/${id}`, method: 'get' })
