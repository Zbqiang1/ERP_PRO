import request from '@/utils/request'

// warehouse
export function pageWarehouse(params) {
  return request({ url: '/inventory/warehouses', method: 'get', params })
}

export function getWarehouseById(id) {
  return request({ url: `/inventory/warehouses/${id}`, method: 'get' })
}

export function addWarehouse(data) {
  return request({ url: '/inventory/warehouses', method: 'post', data })
}

export function updateWarehouse(id, data) {
  return request({ url: `/inventory/warehouses/${id}`, method: 'put', data })
}

export function deleteWarehouse(id) {
  return request({ url: `/inventory/warehouses/${id}`, method: 'delete' })
}

// location
export function pageLocation(params) {
  return request({ url: '/inventory/locations', method: 'get', params })
}

export function getLocationById(id) {
  return request({ url: `/inventory/locations/${id}`, method: 'get' })
}

export function addLocation(data) {
  return request({ url: '/inventory/locations', method: 'post', data })
}

export function updateLocation(id, data) {
  return request({ url: `/inventory/locations/${id}`, method: 'put', data })
}

export function deleteLocation(id) {
  return request({ url: `/inventory/locations/${id}`, method: 'delete' })
}

// material
export function pageMaterial(params) {
  return request({ url: '/inventory/materials', method: 'get', params })
}

export function getMaterialById(id) {
  return request({ url: `/inventory/materials/${id}`, method: 'get' })
}

export function addMaterial(data) {
  return request({ url: '/inventory/materials', method: 'post', data })
}

export function updateMaterial(id, data) {
  return request({ url: `/inventory/materials/${id}`, method: 'put', data })
}

export function deleteMaterial(id) {
  return request({ url: `/inventory/materials/${id}`, method: 'delete' })
}

// stockInOrder
export function pageStockInOrder(params) {
  return request({ url: '/inventory/stock-in-orders', method: 'get', params })
}

export function getStockInOrderById(id) {
  return request({ url: `/inventory/stock-in-orders/${id}`, method: 'get' })
}

export function addStockInOrder(data) {
  return request({ url: '/inventory/stock-in-orders', method: 'post', data })
}

export function updateStockInOrder(id, data) {
  return request({ url: `/inventory/stock-in-orders/${id}`, method: 'put', data })
}

export function deleteStockInOrder(id) {
  return request({ url: `/inventory/stock-in-orders/${id}`, method: 'delete' })
}

export function confirmStockIn(id) {
  return request({ url: `/inventory/stock-in-orders/${id}/confirm`, method: 'post' })
}

// stockOutOrder
export function pageStockOutOrder(params) {
  return request({ url: '/inventory/stock-out-orders', method: 'get', params })
}

export function getStockOutOrderById(id) {
  return request({ url: `/inventory/stock-out-orders/${id}`, method: 'get' })
}

export function addStockOutOrder(data) {
  return request({ url: '/inventory/stock-out-orders', method: 'post', data })
}

export function updateStockOutOrder(id, data) {
  return request({ url: `/inventory/stock-out-orders/${id}`, method: 'put', data })
}

export function deleteStockOutOrder(id) {
  return request({ url: `/inventory/stock-out-orders/${id}`, method: 'delete' })
}

export function confirmStockOut(id) {
  return request({ url: `/inventory/stock-out-orders/${id}/confirm`, method: 'post' })
}

// transferOrder
export function pageTransferOrder(params) {
  return request({ url: '/inventory/transfer-orders', method: 'get', params })
}

export function getTransferOrderById(id) {
  return request({ url: `/inventory/transfer-orders/${id}`, method: 'get' })
}

export function addTransferOrder(data) {
  return request({ url: '/inventory/transfer-orders', method: 'post', data })
}

export function updateTransferOrder(id, data) {
  return request({ url: `/inventory/transfer-orders/${id}`, method: 'put', data })
}

export function deleteTransferOrder(id) {
  return request({ url: `/inventory/transfer-orders/${id}`, method: 'delete' })
}

// inventoryCheck
export function pageInventoryCheck(params) {
  return request({ url: '/inventory/inventory-checks', method: 'get', params })
}

export function getInventoryCheckById(id) {
  return request({ url: `/inventory/checks/${id}`, method: 'get' })
}

export function addInventoryCheck(data) {
  return request({ url: '/inventory/inventory-checks', method: 'post', data })
}

export function updateInventoryCheck(id, data) {
  return request({ url: `/inventory/checks/${id}`, method: 'put', data })
}

export function deleteInventoryCheck(id) {
  return request({ url: `/inventory/checks/${id}`, method: 'delete' })
}

// stockAlert
export function pageStockAlert(params) {
  return request({ url: '/inventory/stock-alerts', method: 'get', params })
}

export function getStockAlertById(id) {
  return request({ url: `/inventory/stock-alerts/${id}`, method: 'get' })
}

export function handleStockAlert(id, data) {
  return request({ url: `/inventory/stock-alerts/${id}/handle`, method: 'put', data })
}

// inventoryLog
export function pageInventoryLog(params) {
  return request({ url: '/inventory/logs', method: 'get', params })
}

// inventory (stock balance)
export function getByWarehouseAndMaterial(warehouseId, materialId) {
  return request({ url: '/inventory/balance', method: 'get', params: { warehouseId, materialId } })
}

// ── aliases matching Vue file usage (namePattern instead of verbPattern) ──
export const warehousePage = pageWarehouse
export const warehouseAdd = addWarehouse
export const warehouseUpdate = updateWarehouse
export const warehouseDelete = deleteWarehouse
export const warehouseGetById = getWarehouseById
export const warehouseListAll = () => request({ url: '/inventory/warehouses', method: 'get', params: { pageSize: 9999 } })

export const materialPage = pageMaterial
export const materialAdd = addMaterial
export const materialUpdate = updateMaterial
export const materialDelete = deleteMaterial
export const materialGetById = getMaterialById
export const materialListAll = () => request({ url: '/inventory/materials', method: 'get', params: { pageSize: 9999 } })

export const stockInOrderPage = pageStockInOrder
export const stockInOrderAdd = addStockInOrder
export const stockInOrderUpdate = updateStockInOrder
export const stockInOrderDelete = deleteStockInOrder
export const stockInOrderGetById = getStockInOrderById
export const stockInOrderConfirm = confirmStockIn

export const stockOutOrderPage = pageStockOutOrder
export const stockOutOrderAdd = addStockOutOrder
export const stockOutOrderUpdate = updateStockOutOrder
export const stockOutOrderDelete = deleteStockOutOrder
export const stockOutOrderGetById = getStockOutOrderById
export const stockOutOrderConfirm = confirmStockOut

export const transferOrderPage = pageTransferOrder
export const transferOrderAdd = addTransferOrder
export const transferOrderUpdate = updateTransferOrder
export const transferOrderDelete = deleteTransferOrder
export const transferOrderGetById = getTransferOrderById

export const inventoryCheckPage = pageInventoryCheck
export const inventoryCheckAdd = addInventoryCheck
export const inventoryCheckUpdate = updateInventoryCheck
export const inventoryCheckDelete = deleteInventoryCheck
export const inventoryCheckGetById = getInventoryCheckById
export const inventoryCheckConfirm = (id) => request({ url: `/inventory/inventory-checks/${id}/confirm`, method: 'post' })
export const inventoryCheckAdjust = (data) => request({ url: '/inventory/inventory-checks/adjust', method: 'post', data })

export const stockAlertPage = pageStockAlert
export const stockAlertGetById = getStockAlertById
export const stockAlertHandle = handleStockAlert

export const inventoryLogPage = pageInventoryLog

export const inventoryGetByWarehouseAndMaterial = getByWarehouseAndMaterial

export const listCategoryOptions = () => request({ url: '/inventory/categories', method: 'get', params: { pageSize: 9999 } })
