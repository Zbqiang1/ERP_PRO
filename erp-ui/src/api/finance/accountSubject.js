// re-exports from @/api/modules/finance with names matching Vue file usage
import request from '@/utils/request'

export const accountSubjectTree = () => request({ url: '/finance/account-subjects/tree', method: 'get' })
export const accountSubjectAdd = (data) => request({ url: '/finance/account-subjects', method: 'post', data })
export const accountSubjectUpdate = (data) => request({ url: `/finance/account-subjects/${data.id}`, method: 'put', data })
export const accountSubjectDelete = (id) => request({ url: `/finance/account-subjects/${id}`, method: 'delete' })
export const accountSubjectGetById = (id) => request({ url: `/finance/account-subjects/${id}`, method: 'get' })
