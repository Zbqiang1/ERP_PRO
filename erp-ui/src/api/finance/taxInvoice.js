// re-exports from @/api/modules/finance with names matching Vue file usage
import request from '@/utils/request'

export const taxInvoicePage = (params) => request({ url: '/finance/tax-invoices', method: 'get', params })
export const taxInvoiceAdd = (data) => request({ url: '/finance/tax-invoices', method: 'post', data })
export const taxInvoiceUpdate = (data) => request({ url: `/finance/tax-invoices/${data.id}`, method: 'put', data })
export const taxInvoiceDelete = (id) => request({ url: `/finance/tax-invoices/${id}`, method: 'delete' })
export const taxInvoiceGetById = (id) => request({ url: `/finance/tax-invoices/${id}`, method: 'get' })
