// re-exports from @/api/modules/customer with names matching Vue file usage
import request from '@/utils/request'

export const customerListAll = () => request({ url: '/sales/customers', method: 'get', params: { pageSize: 9999, pageNo: 1 } })
