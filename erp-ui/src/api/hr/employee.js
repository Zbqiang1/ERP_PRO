// re-exports from @/api/modules/hr with names matching Vue file usage
import request from '@/utils/request'

export const employeePage = (params) => request({ url: '/hr/employees', method: 'get', params })
export const employeeAdd = (data) => request({ url: '/hr/employees', method: 'post', data })
export const employeeUpdate = (data) => request({ url: `/hr/employees/${data.id}`, method: 'put', data })
export const employeeDelete = (id) => request({ url: `/hr/employees/${id}`, method: 'delete' })
export const employeeGetById = (id) => request({ url: `/hr/employees/${id}`, method: 'get' })
export const employeeListAll = () => request({ url: '/hr/employees', method: 'get', params: { pageSize: 9999 } })
