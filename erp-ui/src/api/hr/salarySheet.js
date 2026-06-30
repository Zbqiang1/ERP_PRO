// re-exports from @/api/modules/hr with names matching Vue file usage
import request from '@/utils/request'

export const salarySheetPage = (params) => request({ url: '/hr/salaries', method: 'get', params })
export const salarySheetAdd = (data) => request({ url: '/hr/salaries', method: 'post', data })
export const salarySheetUpdate = (data) => request({ url: `/hr/salaries/${data.id}`, method: 'put', data })
export const salarySheetDelete = (id) => request({ url: `/hr/salaries/${id}`, method: 'delete' })
export const salarySheetGetById = (id) => request({ url: `/hr/salaries/${id}`, method: 'get' })
export const salarySheetCalculate = (data) => request({ url: '/hr/salaries/calculate', method: 'post', data })
