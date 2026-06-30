<template>
  <div class="inventory-balance">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="物料编码">
          <el-input v-model="searchForm.materialCode" placeholder="请输入物料编码" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="Download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        empty-text="暂无数据"
        class="w-full"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="warehouseName" label="仓库" width="150" />
        <el-table-column prop="locationName" label="库位" width="120" />
        <el-table-column prop="materialCode" label="物料编码" width="120" />
        <el-table-column prop="materialName" label="物料名称" min-width="150" />
        <el-table-column prop="onHandQty" label="在手数量" width="100" align="right" />
        <el-table-column prop="lockedQty" label="锁定数量" width="100" align="right" />
        <el-table-column prop="availableQty" label="可用数量" width="100" align="right" />
        <el-table-column prop="safetyStock" label="安全库存" width="100" align="right" />
        <el-table-column prop="lastCountTime" label="最后盘点时间" width="170" />
      </el-table>
      <div class="flex justify-end mt-16">
        <el-pagination
          v-model:current-page="pagination.pageNo" v-model:page-size="pagination.pageSize"
          :total="pagination.total" :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { inventoryGetByWarehouseAndMaterial } from '@/api/modules/inventory'
import { warehouseListAll } from '@/api/modules/inventory'

const loading = ref(false)
const searchForm = ref({ warehouseId: null, materialCode: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 20, total: 0 })
const warehouseOptions = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await inventoryGetByWarehouseAndMaterial(params)
    if (res.code === 200) {
      const data = res.data.records || res.data.list || res.data || []
      tableData.value = data
      pagination.total = res.data.total || data.length || 0
    }
  } catch { ElMessage.error('获取库存余额失败') } finally { loading.value = false }
}

const fetchWarehouses = async () => {
  try { const res = await warehouseListAll(); warehouseOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const tableRowClassName = ({ row }) => {
  if (row.availableQty !== null && row.safetyStock !== null && row.availableQty < row.safetyStock) {
    return 'low-stock-row'
  }
  return ''
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { warehouseId: null, materialCode: '' }; handleSearch() }

const handleExport = () => {
  ElMessage.success('导出功能开发中')
}

onMounted(() => { fetchWarehouses(); fetchData() })
</script>

<style scoped>
.inventory-balance { padding: 16px; }
.mt-16 { margin-top: 16px; }
.w-full { width: 100%; }
.flex { display: flex; }
.justify-end { justify-content: flex-end; }
:deep(.low-stock-row) { background-color: #fef0f0 !important; }
:deep(.low-stock-row td) { color: #f56c6c !important; }
</style>
