// re-exports from @/api/modules/supplier with names matching Vue file usage
import request from '@/utils/request'

export const supplierListAll = () => request({ url: '/purchase/suppliers', method: 'get', params: { pageSize: 9999, pageNo: 1 } })
