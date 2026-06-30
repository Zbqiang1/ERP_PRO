// re-exports from @/api/modules/finance with names matching Vue file usage
import request from '@/utils/request'

export const costSheetPage = (params) => request({ url: '/finance/cost-sheets', method: 'get', params })
export const costSheetAdd = (data) => request({ url: '/finance/cost-sheets', method: 'post', data })
export const costSheetUpdate = (data) => request({ url: `/finance/cost-sheets/${data.id}`, method: 'put', data })
export const costSheetDelete = (id) => request({ url: `/finance/cost-sheets/${id}`, method: 'delete' })
export const costSheetGetById = (id) => request({ url: `/finance/cost-sheets/${id}`, method: 'get' })
