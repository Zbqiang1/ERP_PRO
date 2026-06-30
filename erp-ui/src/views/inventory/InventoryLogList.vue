<template>
  <div class="inventory-log-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="物料">
          <el-select v-model="searchForm.materialId" placeholder="请选择物料" clearable filterable>
            <el-option v-for="m in materialOptions" :key="m.id" :label="m.materialName" :value="m.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="变更类型">
          <el-select v-model="searchForm.changeType" placeholder="请选择变更类型" clearable>
            <el-option label="入库" value="IN" />
            <el-option label="出库" value="OUT" />
            <el-option label="调拨" value="TRANSFER" />
            <el-option label="盘点" value="CHECK" />
            <el-option label="调整" value="ADJUST" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker v-model="searchForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="materialName" label="物料" width="150" />
        <el-table-column prop="changeType" label="变更类型" width="100">
          <template #default="{ row }">
            <el-tag :type="changeTypeTag(row.changeType)">{{ changeTypeLabel(row.changeType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="beforeQty" label="变更前数量" width="110" align="right" />
        <el-table-column prop="changeQty" label="变更数量" width="100" align="right">
          <template #default="{ row }">
            <span :style="{ color: row.changeQty > 0 ? '#67c23a' : row.changeQty < 0 ? '#f56c6c' : '' }">
              {{ row.changeQty > 0 ? '+' + row.changeQty : row.changeQty }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="afterQty" label="变更后数量" width="110" align="right" />
        <el-table-column prop="relatedOrderNo" label="关联单据号" width="150" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="createTime" label="操作时间" width="170" />
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
import { inventoryLogPage } from '@/api/modules/inventory'
import { warehouseListAll } from '@/api/modules/inventory'
import { materialListAll } from '@/api/modules/inventory'

const loading = ref(false)
const searchForm = ref({ materialId: null, warehouseId: null, changeType: '', dateRange: [] })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const warehouseOptions = ref([])
const materialOptions = ref([])

const changeTypeMap = { IN: '入库', OUT: '出库', TRANSFER: '调拨', CHECK: '盘点', ADJUST: '调整' }
const changeTypeTagMap = { IN: 'success', OUT: 'danger', TRANSFER: 'warning', CHECK: 'primary', ADJUST: 'info' }
const changeTypeLabel = (t) => changeTypeMap[t] || t
const changeTypeTag = (t) => changeTypeTagMap[t] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    if (params.dateRange?.length === 2) { params.startDate = params.dateRange[0]; params.endDate = params.dateRange[1] }
    delete params.dateRange
    const res = await inventoryLogPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取库存日志失败') } finally { loading.value = false }
}

const fetchOptions = async () => {
  try { const r1 = await warehouseListAll(); if (r1.code === 200) warehouseOptions.value = r1.data || [] } catch { /* */ }
  try { const r2 = await materialListAll(); if (r2.code === 200) materialOptions.value = r2.data || [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { materialId: null, warehouseId: null, changeType: '', dateRange: [] }; handleSearch() }

onMounted(() => { fetchOptions(); fetchData() })
</script>

<style scoped>
.inventory-log-list { padding: 16px; }
.mt-16 { margin-top: 16px; }
.w-full { width: 100%; }
.flex { display: flex; }
.justify-end { justify-content: flex-end; }
</style>
