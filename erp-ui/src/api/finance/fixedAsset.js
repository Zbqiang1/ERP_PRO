// re-exports from @/api/modules/finance with names matching Vue file usage
import request from '@/utils/request'

export const fixedAssetPage = (params) => request({ url: '/finance/fixed-assets', method: 'get', params })
export const fixedAssetAdd = (data) => request({ url: '/finance/fixed-assets', method: 'post', data })
export const fixedAssetUpdate = (data) => request({ url: `/finance/fixed-assets/${data.id}`, method: 'put', data })
export const fixedAssetDelete = (id) => request({ url: `/finance/fixed-assets/${id}`, method: 'delete' })
export const fixedAssetGetById = (id) => request({ url: `/finance/fixed-assets/${id}`, method: 'get' })
export const fixedAssetDepreciation = (data) => request({ url: '/finance/fixed-assets/depreciation', method: 'post', data })
