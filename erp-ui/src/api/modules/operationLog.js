import request from '@/utils/request'

export function pageLog(params) {
  return request({ url: '/logs', method: 'get', params })
}

export function getLogById(id) {
  return request({ url: `/logs/${id}`, method: 'get' })
}
