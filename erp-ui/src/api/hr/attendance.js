// re-exports from @/api/modules/hr with names matching Vue file usage
import request from '@/utils/request'

export const attendancePage = (params) => request({ url: '/hr/attendances', method: 'get', params })
export const attendanceAdd = (data) => request({ url: '/hr/attendances', method: 'post', data })
export const attendanceUpdate = (data) => request({ url: `/hr/attendances/${data.id}`, method: 'put', data })
export const attendanceDelete = (id) => request({ url: `/hr/attendances/${id}`, method: 'delete' })
export const attendanceGetById = (id) => request({ url: `/hr/attendances/${id}`, method: 'get' })
export const attendanceBatchImport = (data) => request({ url: '/hr/attendances/batch', method: 'post', data })
