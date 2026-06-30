// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const inventoryLogPage = (params) => request({ url: '/inventory/logs', method: 'get', params })
