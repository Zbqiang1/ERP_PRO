import request from '@/utils/request'

export function pageDictionary(params) {
  return request({ url: '/dictionaries', method: 'get', params })
}

export function getDictionaryById(id) {
  return request({ url: `/dictionaries/${id}`, method: 'get' })
}

export function addDictionary(data) {
  return request({ url: '/dictionaries', method: 'post', data })
}

export function updateDictionary(id, data) {
  return request({ url: `/dictionaries/${id}`, method: 'put', data })
}

export function deleteDictionary(id) {
  return request({ url: `/dictionaries/${id}`, method: 'delete' })
}

export function getByDictType(type) {
  return request({ url: `/dictionaries`, method: 'get', params: { dictType: type } })
}

// aliases matching Vue file usage
export const page = pageDictionary
export const getById = getDictionaryById
export const add = addDictionary
export const update = updateDictionary
export const deleteDict = deleteDictionary
