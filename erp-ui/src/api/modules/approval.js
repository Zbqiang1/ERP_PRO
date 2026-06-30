import request from '@/utils/request'

export function listByBusiness(businessType, businessId) {
  return request({ url: '/approval/logs', method: 'get', params: { businessType, businessId } })
}
