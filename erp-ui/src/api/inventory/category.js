// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const listCategoryOptions = () => request({ url: '/inventory/categories', method: 'get', params: { pageSize: 9999 } })
