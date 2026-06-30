// re-exports from @/api/modules/finance with names matching Vue file usage
import request from '@/utils/request'

export const payablePage = (params) => request({ url: '/finance/payables', method: 'get', params })
export const payableAdd = (data) => request({ url: '/finance/payables', method: 'post', data })
export const payableUpdate = (data) => request({ url: `/finance/payables/${data.id}`, method: 'put', data })
export const payableDelete = (id) => request({ url: `/finance/payables/${id}`, method: 'delete' })
export const payableGetById = (id) => request({ url: `/finance/payables/${id}`, method: 'get' })
export const payablePay = (data) => request({ url: '/finance/payables/pay', method: 'post', data })
