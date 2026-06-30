// re-exports from @/api/modules/finance with names matching Vue file usage
import request from '@/utils/request'

export const voucherPage = (params) => request({ url: '/finance/ledger/vouchers', method: 'get', params })
export const voucherAdd = (data) => request({ url: '/finance/ledger/vouchers', method: 'post', data })
export const voucherUpdate = (data) => request({ url: `/finance/ledger/vouchers/${data.id}`, method: 'put', data })
export const voucherDelete = (id) => request({ url: `/finance/ledger/vouchers/${id}`, method: 'delete' })
export const voucherGetById = (id) => request({ url: `/finance/ledger/vouchers/${id}`, method: 'get' })
export const voucherAudit = (id) => request({ url: `/finance/ledger/vouchers/${id}/audit`, method: 'post' })
export const voucherPost = (id) => request({ url: `/finance/ledger/vouchers/${id}/post`, method: 'post' })
