// re-exports from @/api/modules/production with names matching Vue file usage
import request from '@/utils/request'

export const bomHeaderPage = (params) => request({ url: '/production/boms', method: 'get', params })
export const bomHeaderAdd = (data) => request({ url: '/production/boms', method: 'post', data })
export const bomHeaderUpdate = (data) => request({ url: `/production/boms/${data.id}`, method: 'put', data })
export const bomHeaderDelete = (id) => request({ url: `/production/boms/${id}`, method: 'delete' })
export const bomHeaderGetById = (id) => request({ url: `/production/boms/${id}`, method: 'get' })
export const bomDetailListByHeaderId = (bomId) => request({ url: `/production/boms/detail/${bomId}`, method: 'get' })
