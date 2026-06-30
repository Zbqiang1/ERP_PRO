// re-exports from @/api/modules/production with names matching Vue file usage
import request from '@/utils/request'

export const mrpResultPage = (params) => request({ url: '/production/mrp/results', method: 'get', params })
export const mrpResultGenerate = (planId) => request({ url: `/production/mrp/generate/${planId}`, method: 'post' })
