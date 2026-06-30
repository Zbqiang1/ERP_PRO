// re-exports from @/api/modules/finance with names matching Vue file usage
import request from '@/utils/request'

export const receivablePage = (params) => request({ url: '/finance/receivables', method: 'get', params })
export const receivableAdd = (data) => request({ url: '/finance/receivables', method: 'post', data })
export const receivableUpdate = (data) => request({ url: `/finance/receivables/${data.id}`, method: 'put', data })
export const receivableDelete = (id) => request({ url: `/finance/receivables/${id}`, method: 'delete' })
export const receivableGetById = (id) => request({ url: `/finance/receivables/${id}`, method: 'get' })
export const receivableCollect = (data) => request({ url: '/finance/receivables/collect', method: 'post', data })
