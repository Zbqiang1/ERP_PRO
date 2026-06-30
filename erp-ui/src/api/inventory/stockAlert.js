// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const stockAlertPage = (params) => request({ url: '/inventory/stock-alerts', method: 'get', params })
export const stockAlertGetById = (id) => request({ url: `/inventory/stock-alerts/${id}`, method: 'get' })
export const stockAlertHandle = (id, data) => request({ url: `/inventory/stock-alerts/${id}/handle`, method: 'put', data })
