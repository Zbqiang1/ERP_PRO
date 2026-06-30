import request from '@/utils/request'

// bomHeader
export function pageBom(params) {
  return request({ url: '/production/boms', method: 'get', params })
}

export function getBomById(id) {
  return request({ url: `/production/boms/${id}`, method: 'get' })
}

export function addBom(data) {
  return request({ url: '/production/boms', method: 'post', data })
}

export function updateBom(id, data) {
  return request({ url: `/production/boms/${id}`, method: 'put', data })
}

export function deleteBom(id) {
  return request({ url: `/production/boms/${id}`, method: 'delete' })
}

// bomDetail
export function listBomDetailByBomId(bomId) {
  return request({ url: `/production/boms/detail/${bomId}`, method: 'get' })
}

export function addBomDetail(data) {
  return request({ url: '/production/boms/detail', method: 'post', data })
}

export function updateBomDetail(id, data) {
  return request({ url: '/production/boms/detail', method: 'put', data })
}

export function deleteBomDetail(id) {
  return request({ url: `/production/boms/detail/${id}`, method: 'delete' })
}

// mpsPlan
export function pageMps(params) {
  return request({ url: '/production/mps', method: 'get', params })
}

export function getMpsById(id) {
  return request({ url: `/production/mps/${id}`, method: 'get' })
}

export function addMps(data) {
  return request({ url: '/production/mps', method: 'post', data })
}

export function updateMps(id, data) {
  return request({ url: `/production/mps/${id}`, method: 'put', data })
}

export function deleteMps(id) {
  return request({ url: `/production/mps/${id}`, method: 'delete' })
}

// mrpResult
export function pageMrpResult(params) {
  return request({ url: '/production/mrp/results', method: 'get', params })
}

export function generateMrp(planId) {
  return request({ url: `/production/mrp/generate/${planId}`, method: 'post' })
}

// workOrder
export function pageWorkOrder(params) {
  return request({ url: '/production/work-orders', method: 'get', params })
}

export function getWorkOrderById(id) {
  return request({ url: `/production/work-orders/${id}`, method: 'get' })
}

export function addWorkOrder(data) {
  return request({ url: '/production/work-orders', method: 'post', data })
}

export function updateWorkOrder(id, data) {
  return request({ url: `/production/work-orders/${id}`, method: 'put', data })
}

export function deleteWorkOrder(id) {
  return request({ url: `/production/work-orders/${id}`, method: 'delete' })
}

export function startWorkOrder(id) {
  return request({ url: `/production/work-orders/${id}/start`, method: 'post' })
}

export function completeWorkOrder(id) {
  return request({ url: `/production/work-orders/${id}/complete`, method: 'post' })
}

// woRouting
export function listRoutingsByWoId(woId) {
  return request({ url: `/production/work-orders/${woId}/routings`, method: 'get' })
}

export function addWoRouting(data) {
  return request({ url: '/production/work-orders/routing', method: 'post', data })
}

export function updateWoRouting(id, data) {
  return request({ url: '/production/work-orders/routing', method: 'put', data })
}

export function deleteWoRouting(id) {
  return request({ url: `/production/work-orders/routing/${id}`, method: 'delete' })
}

// subcontract
export function pageSubcontract(params) {
  return request({ url: '/production/subcontracts', method: 'get', params })
}

export function getSubcontractById(id) {
  return request({ url: `/production/subcontracts/${id}`, method: 'get' })
}

export function addSubcontract(data) {
  return request({ url: '/production/subcontracts', method: 'post', data })
}

export function updateSubcontract(id, data) {
  return request({ url: `/production/subcontracts/${id}`, method: 'put', data })
}

export function deleteSubcontract(id) {
  return request({ url: `/production/subcontracts/${id}`, method: 'delete' })
}

// prodInspection
export function pageProdInspection(params) {
  return request({ url: '/production/inspections', method: 'get', params })
}

export function getProdInspectionById(id) {
  return request({ url: `/production/inspections/${id}`, method: 'get' })
}

export function addProdInspection(data) {
  return request({ url: '/production/inspections', method: 'post', data })
}

export function updateProdInspection(id, data) {
  return request({ url: `/production/inspections/${id}`, method: 'put', data })
}

export function deleteProdInspection(id) {
  return request({ url: `/production/inspections/${id}`, method: 'delete' })
}

// ── aliases matching Vue file usage (namePattern instead of verbPattern) ──
export const bomHeaderPage = pageBom
export const bomHeaderAdd = addBom
export const bomHeaderUpdate = updateBom
export const bomHeaderDelete = deleteBom
export const bomHeaderGetById = getBomById
export const bomDetailListByHeaderId = listBomDetailByBomId

export const mpsPlanPage = pageMps
export const mpsPlanAdd = addMps
export const mpsPlanUpdate = updateMps
export const mpsPlanDelete = deleteMps
export const mpsPlanGetById = getMpsById
export const mpsPlanListAll = () => request({ url: '/production/mps', method: 'get', params: { pageSize: 9999 } })

export const mrpResultPage = pageMrpResult
export const mrpResultGenerate = generateMrp

export const workOrderPage = pageWorkOrder
export const workOrderAdd = addWorkOrder
export const workOrderUpdate = updateWorkOrder
export const workOrderDelete = deleteWorkOrder
export const workOrderGetById = getWorkOrderById
export const workOrderStart = startWorkOrder
export const workOrderComplete = completeWorkOrder

export const woRoutingPage = (params) => request({ url: '/production/work-orders/routing', method: 'get', params })
export const woRoutingAdd = addWoRouting
export const woRoutingUpdate = updateWoRouting
export const woRoutingDelete = deleteWoRouting
export const woRoutingComplete = (id) => request({ url: `/production/work-orders/routing/${id}/complete`, method: 'post' })
export const woRoutingAssignWorker = (id, workerId) => request({ url: `/production/work-orders/routing/${id}/assign`, method: 'put', data: { workerId } })

export const subcontractPage = pageSubcontract
export const subcontractAdd = addSubcontract
export const subcontractUpdate = updateSubcontract
export const subcontractDelete = deleteSubcontract
export const subcontractGetById = getSubcontractById

export const prodInspectionPage = pageProdInspection
export const prodInspectionAdd = addProdInspection
export const prodInspectionUpdate = updateProdInspection
export const prodInspectionDelete = deleteProdInspection
export const prodInspectionGetById = getProdInspectionById
