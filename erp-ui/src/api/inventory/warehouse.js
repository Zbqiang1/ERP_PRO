// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const warehousePage = (params) => request({ url: '/inventory/warehouses', method: 'get', params })
export const warehouseAdd = (data) => request({ url: '/inventory/warehouses', method: 'post', data })
export const warehouseUpdate = (data) => request({ url: `/inventory/warehouses/${data.id}`, method: 'put', data })
export const warehouseDelete = (id) => request({ url: `/inventory/warehouses/${id}`, method: 'delete' })
export const warehouseGetById = (id) => request({ url: `/inventory/warehouses/${id}`, method: 'get' })
export const warehouseListAll = () => request({ url: '/inventory/warehouses', method: 'get', params: { pageSize: 9999, pageNo: 1 } })
