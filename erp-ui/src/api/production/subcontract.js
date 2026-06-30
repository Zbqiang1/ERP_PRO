// re-exports from @/api/modules/production with names matching Vue file usage
import request from '@/utils/request'

export const subcontractPage = (params) => request({ url: '/production/subcontracts', method: 'get', params })
export const subcontractAdd = (data) => request({ url: '/production/subcontracts', method: 'post', data })
export const subcontractUpdate = (data) => request({ url: `/production/subcontracts/${data.id}`, method: 'put', data })
export const subcontractDelete = (id) => request({ url: `/production/subcontracts/${id}`, method: 'delete' })
export const subcontractGetById = (id) => request({ url: `/production/subcontracts/${id}`, method: 'get' })
