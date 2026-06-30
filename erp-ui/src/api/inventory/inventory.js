// re-exports from @/api/modules/inventory with names matching Vue file usage
import request from '@/utils/request'

export const inventoryGetByWarehouseAndMaterial = (warehouseId, materialId) =>
  request({ url: '/inventory/balance', method: 'get', params: { warehouseId, materialId } })
