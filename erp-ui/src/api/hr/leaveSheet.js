// re-exports from @/api/modules/hr with names matching Vue file usage
import request from '@/utils/request'

export const leaveSheetPage = (params) => request({ url: '/hr/leaves', method: 'get', params })
export const leaveSheetAdd = (data) => request({ url: '/hr/leaves', method: 'post', data })
export const leaveSheetUpdate = (data) => request({ url: `/hr/leaves/${data.id}`, method: 'put', data })
export const leaveSheetDelete = (id) => request({ url: `/hr/leaves/${id}`, method: 'delete' })
export const leaveSheetGetById = (id) => request({ url: `/hr/leaves/${id}`, method: 'get' })
export const leaveSheetApprove = (id, approver) => request({ url: `/hr/leaves/${id}/approve`, method: 'put', params: { approver } })
