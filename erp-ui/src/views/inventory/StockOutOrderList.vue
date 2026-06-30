<template>
  <div class="stock-out-order-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="出库单号">
          <el-input v-model="searchForm.outNo" placeholder="请输入出库单号" clearable />
        </el-form-item>
        <el-form-item label="出库类型">
          <el-select v-model="searchForm.outType" placeholder="请选择出库类型" clearable>
            <el-option label="销售出库" value="SALES" />
            <el-option label="生产领料" value="PROD_PICK" />
            <el-option label="调拨出库" value="TRANSFER" />
            <el-option label="退货出库" value="RETURN" />
            <el-option label="盘亏" value="CHECK_LOSS" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="出库日期">
          <el-date-picker v-model="searchForm.outDateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增出库单</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="outNo" label="出库单号" width="150" />
        <el-table-column prop="outType" label="出库类型" width="110">
          <template #default="{ row }">
            <el-tag :type="outTypeTagType(row.outType)">{{ outTypeLabel(row.outType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warehouseName" label="仓库" width="150" />
        <el-table-column prop="totalAmount" label="总金额" width="120" align="right">
          <template #default="{ row }">{{ row.totalAmount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="outDate" label="出库日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link size="small" @click="handleConfirm(row)">确认出库</el-button>
            <el-button v-if="row.status === 'PENDING'" type="warning" link size="small" @click="handleCancelOrder(row)">取消</el-button>
            <el-popconfirm title="确定删除该出库单？" @confirm="handleDelete(row)">
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

    <el-dialog v-model="detailVisible" title="出库单详情" width="800px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="出库单号">{{ detailData.outNo }}</el-descriptions-item>
          <el-descriptions-item label="出库类型"><el-tag :type="outTypeTagType(detailData.outType)">{{ outTypeLabel(detailData.outType) }}</el-tag></el-descriptions-item>
          <el-descriptions-item label="仓库">{{ detailData.warehouseName }}</el-descriptions-item>
          <el-descriptions-item label="总金额">{{ detailData.totalAmount?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="出库日期">{{ detailData.outDate }}</el-descriptions-item>
          <el-descriptions-item label="状态"><el-tag :type="statusTagType(detailData.status)">{{ statusLabel(detailData.status) }}</el-tag></el-descriptions-item>
        </el-descriptions>
        <h4 class="mt-16 mb-8">出库明细</h4>
        <el-table :data="detailData.items || []" border stripe size="small" class="w-full">
          <el-table-column prop="materialName" label="物料" width="150" />
          <el-table-column prop="locationName" label="库位" width="120" />
          <el-table-column prop="quantity" label="数量" width="100" align="right" />
          <el-table-column prop="unitPrice" label="单价" width="100" align="right">
            <template #default="{ row }">{{ row.unitPrice?.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="{ row }">{{ row.amount?.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="batchNo" label="批次号" width="120" />
        </el-table>
      </template>
    </el-dialog>

    <StockOutOrderFormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :warehouse-options="warehouseOptions" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { stockOutOrderPage, stockOutOrderAdd, stockOutOrderUpdate, stockOutOrderDelete, stockOutOrderGetById, stockOutOrderConfirm } from '@/api/modules/inventory'
import { warehouseListAll } from '@/api/modules/inventory'
import StockOutOrderFormDialog from './components/StockOutOrderFormDialog.vue'

const loading = ref(false)
const searchForm = ref({ outNo: '', outType: '', warehouseId: null, outDateRange: [] })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const warehouseOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const outTypeMap = { SALES: '销售出库', PROD_PICK: '生产领料', TRANSFER: '调拨出库', RETURN: '退货出库', CHECK_LOSS: '盘亏', OTHER: '其他' }
const outTypeTagMap = { SALES: 'primary', PROD_PICK: 'success', TRANSFER: 'warning', RETURN: 'info', CHECK_LOSS: 'danger', OTHER: '' }
const statusMap = { PENDING: '待审核', CONFIRMED: '已出库', CANCELLED: '已取消' }
const statusTagMap = { PENDING: 'warning', CONFIRMED: 'success', CANCELLED: 'info' }

const outTypeLabel = (t) => outTypeMap[t] || t
const outTypeTagType = (t) => outTypeTagMap[t] || ''
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    if (params.outDateRange?.length === 2) { params.outDateStart = params.outDateRange[0]; params.outDateEnd = params.outDateRange[1] }
    delete params.outDateRange
    const res = await stockOutOrderPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取出库单列表失败') } finally { loading.value = false }
}

const fetchWarehouses = async () => {
  try { const res = await warehouseListAll(); warehouseOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { outNo: '', outType: '', warehouseId: null, outDateRange: [] }; handleSearch() }
const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增出库单'; currentRow.value = null; dialogVisible.value = true }

const handleDetail = async (row) => {
  try { const res = await stockOutOrderGetById(row.id); detailData.value = res.code === 200 ? res.data : row } catch { detailData.value = row }
  detailVisible.value = true
}

const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确定要确认出库该单据吗？', '确认出库', { type: 'warning' })
    const res = await stockOutOrderConfirm(row.id)
    res.code === 200 ? (ElMessage.success('出库确认成功'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* cancelled */ }
}

const handleCancelOrder = async (row) => {
  try {
    await ElMessageBox.confirm('确定要取消该出库单吗？', '取消出库单', { type: 'warning' })
    const res = await stockOutOrderUpdate({ id: row.id, status: 'CANCELLED' })
    res.code === 200 ? (ElMessage.success('已取消'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* cancelled */ }
}

const handleDelete = async (row) => {
  try {
    const res = await stockOutOrderDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}

const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? stockOutOrderAdd : stockOutOrderUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchWarehouses(); fetchData() })
</script>

<style scoped>
.stock-out-order-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; } .mb-8 { margin-bottom: 8px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
