<template>
  <div class="transfer-order-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="调拨单号">
          <el-input v-model="searchForm.transferNo" placeholder="请输入调拨单号" clearable />
        </el-form-item>
        <el-form-item label="调出仓库">
          <el-select v-model="searchForm.fromWarehouseId" placeholder="请选择" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="调入仓库">
          <el-select v-model="searchForm.toWarehouseId" placeholder="请选择" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="调拨日期">
          <el-date-picker v-model="searchForm.transferDateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增调拨单</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="transferNo" label="调拨单号" width="150" />
        <el-table-column label="调出/调入仓库" min-width="200">
          <template #default="{ row }">
            <span>{{ row.fromWarehouseName }}</span>
            <el-icon class="mx-8"><Right /></el-icon>
            <span>{{ row.toWarehouseName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120" align="right">
          <template #default="{ row }">{{ row.totalAmount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="transferDate" label="调拨日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'IN_TRANSIT' ? 'warning' : 'success'">
              {{ row.status === 'IN_TRANSIT' ? '在途' : '已完成' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定删除该调拨单？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="flex justify-end mt-16">
        <el-pagination
          v-model:current-page="pagination.pageNo" v-model:page-size="pagination.pageSize"
          :total="pagination.total" :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData"
        />
      </div>
    </el-card>

    <el-dialog v-model="detailVisible" title="调拨单详情" width="850px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="调拨单号">{{ detailData.transferNo }}</el-descriptions-item>
          <el-descriptions-item label="调出仓库">{{ detailData.fromWarehouseName }}</el-descriptions-item>
          <el-descriptions-item label="调入仓库">{{ detailData.toWarehouseName }}</el-descriptions-item>
          <el-descriptions-item label="总金额">{{ detailData.totalAmount?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="调拨日期">{{ detailData.transferDate }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="detailData.status === 'IN_TRANSIT' ? 'warning' : 'success'">
              {{ detailData.status === 'IN_TRANSIT' ? '在途' : '已完成' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        <h4 class="mt-16 mb-8">调拨明细</h4>
        <el-table :data="detailData.items || []" border stripe size="small" class="w-full">
          <el-table-column prop="materialName" label="物料" width="150" />
          <el-table-column label="调出库位/调入库位" min-width="200">
            <template #default="{ row }">
              <span>{{ row.fromLocationName }}</span>
              <el-icon class="mx-8"><Right /></el-icon>
              <span>{{ row.toLocationName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" align="right" />
          <el-table-column prop="batchNo" label="批次号" width="120" />
        </el-table>
      </template>
    </el-dialog>

    <TransferOrderFormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :warehouse-options="warehouseOptions" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { transferOrderPage, transferOrderAdd, transferOrderUpdate, transferOrderDelete, transferOrderGetById } from '@/api/modules/inventory'
import { warehouseListAll } from '@/api/modules/inventory'
import TransferOrderFormDialog from './components/TransferOrderFormDialog.vue'

const loading = ref(false)
const searchForm = ref({ transferNo: '', fromWarehouseId: null, toWarehouseId: null, transferDateRange: [] })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const warehouseOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    if (params.transferDateRange?.length === 2) { params.transferDateStart = params.transferDateRange[0]; params.transferDateEnd = params.transferDateRange[1] }
    delete params.transferDateRange
    const res = await transferOrderPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取调拨单列表失败') } finally { loading.value = false }
}

const fetchWarehouses = async () => {
  try { const res = await warehouseListAll(); warehouseOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { transferNo: '', fromWarehouseId: null, toWarehouseId: null, transferDateRange: [] }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增调拨单'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑调拨单'
  try { const res = await transferOrderGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleDetail = async (row) => {
  try { const res = await transferOrderGetById(row.id); detailData.value = res.code === 200 ? res.data : row } catch { detailData.value = row }
  detailVisible.value = true
}
const handleDelete = async (row) => {
  try {
    const res = await transferOrderDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? transferOrderAdd : transferOrderUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchWarehouses(); fetchData() })
</script>

<style scoped>
.transfer-order-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; } .mb-8 { margin-bottom: 8px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
.mx-8 { margin-left: 8px; margin-right: 8px; }
</style>
