// re-exports from @/api/modules/hr with names matching Vue file usage
import request from '@/utils/request'

export const organizationPage = (params) => request({ url: '/hr/organizations', method: 'get', params })
export const organizationAdd = (data) => request({ url: '/hr/organizations', method: 'post', data })
export const organizationUpdate = (data) => request({ url: `/hr/organizations/${data.id}`, method: 'put', data })
export const organizationDelete = (id) => request({ url: `/hr/organizations/${id}`, method: 'delete' })
export const organizationGetById = (id) => request({ url: `/hr/organizations/${id}`, method: 'get' })
export const organizationTree = () => request({ url: '/hr/organizations/tree', method: 'get' })
export const organizationListAll = () => request({ url: '/hr/organizations', method: 'get', params: { pageSize: 9999 } })
