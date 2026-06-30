<template>
  <div class="stock-in-order-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="入库单号">
          <el-input v-model="searchForm.inNo" placeholder="请输入入库单号" clearable />
        </el-form-item>
        <el-form-item label="入库类型">
          <el-select v-model="searchForm.inType" placeholder="请选择入库类型" clearable>
            <el-option label="采购入库" value="PURCHASE" />
            <el-option label="生产入库" value="PRODUCTION" />
            <el-option label="调拨入库" value="TRANSFER" />
            <el-option label="退货入库" value="RETURN" />
            <el-option label="盘盈" value="CHECK_SURPLUS" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="入库日期">
          <el-date-picker v-model="searchForm.inDateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增入库单</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="inNo" label="入库单号" width="150" />
        <el-table-column prop="inType" label="入库类型" width="110">
          <template #default="{ row }">
            <el-tag :type="inTypeTagType(row.inType)">{{ inTypeLabel(row.inType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warehouseName" label="仓库" width="150" />
        <el-table-column prop="totalAmount" label="总金额" width="120" align="right">
          <template #default="{ row }">{{ row.totalAmount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="inDate" label="入库日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link size="small" @click="handleConfirm(row)">确认入库</el-button>
            <el-button v-if="row.status === 'PENDING'" type="warning" link size="small" @click="handleCancel(row)">取消</el-button>
            <el-popconfirm title="确定删除该入库单？" @confirm="handleDelete(row)">
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
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchData" @current-change="fetchData"
        />
      </div>
    </el-card>

    <el-dialog v-model="detailVisible" title="入库单详情" width="800px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="入库单号">{{ detailData.inNo }}</el-descriptions-item>
          <el-descriptions-item label="入库类型"><el-tag :type="inTypeTagType(detailData.inType)">{{ inTypeLabel(detailData.inType) }}</el-tag></el-descriptions-item>
          <el-descriptions-item label="仓库">{{ detailData.warehouseName }}</el-descriptions-item>
          <el-descriptions-item label="总金额">{{ detailData.totalAmount?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="入库日期">{{ detailData.inDate }}</el-descriptions-item>
          <el-descriptions-item label="状态"><el-tag :type="statusTagType(detailData.status)">{{ statusLabel(detailData.status) }}</el-tag></el-descriptions-item>
        </el-descriptions>
        <h4 class="mt-16 mb-8">入库明细</h4>
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

    <FormDialog
      v-model="dialogVisible"
      :title="dialogTitle"
      :mode="dialogMode"
      :data="currentRow"
      :warehouse-options="warehouseOptions"
      @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { stockInOrderPage, stockInOrderAdd, stockInOrderUpdate, stockInOrderDelete, stockInOrderGetById, stockInOrderConfirm } from '@/api/modules/inventory'
import { warehouseListAll } from '@/api/modules/inventory'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ inNo: '', inType: '', warehouseId: null, inDateRange: [] })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const warehouseOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const inTypeMap = { PURCHASE: '采购入库', PRODUCTION: '生产入库', TRANSFER: '调拨入库', RETURN: '退货入库', CHECK_SURPLUS: '盘盈', OTHER: '其他' }
const inTypeTagMap = { PURCHASE: 'primary', PRODUCTION: 'success', TRANSFER: 'warning', RETURN: 'info', CHECK_SURPLUS: 'danger', OTHER: '' }
const statusMap = { PENDING: '待审核', CONFIRMED: '已入库', CANCELLED: '已取消' }
const statusTagMap = { PENDING: 'warning', CONFIRMED: 'success', CANCELLED: 'info' }

const inTypeLabel = (t) => inTypeMap[t] || t
const inTypeTagType = (t) => inTypeTagMap[t] || ''
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    if (params.inDateRange?.length === 2) {
      params.inDateStart = params.inDateRange[0]; params.inDateEnd = params.inDateRange[1]
    }
    delete params.inDateRange
    const res = await stockInOrderPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取入库单列表失败') } finally { loading.value = false }
}

const fetchWarehouses = async () => {
  try { const res = await warehouseListAll(); warehouseOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* ignore */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { inNo: '', inType: '', warehouseId: null, inDateRange: [] }; handleSearch() }
const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增入库单'; currentRow.value = null; dialogVisible.value = true }

const handleDetail = async (row) => {
  try { const res = await stockInOrderGetById(row.id); detailData.value = res.code === 200 ? res.data : row } catch { detailData.value = row }
  detailVisible.value = true
}

const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确定要确认入库该单据吗？', '确认入库', { type: 'warning' })
    const res = await stockInOrderConfirm(row.id)
    res.code === 200 ? (ElMessage.success('入库确认成功'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* cancelled */ }
}

const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('确定要取消该入库单吗？', '取消入库单', { type: 'warning' })
    const res = await stockInOrderUpdate({ id: row.id, status: 'CANCELLED' })
    res.code === 200 ? (ElMessage.success('已取消'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* cancelled */ }
}

const handleDelete = async (row) => {
  try {
    const res = await stockInOrderDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}

const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? stockInOrderAdd : stockInOrderUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchWarehouses(); fetchData() })
</script>

<style scoped>
.stock-in-order-list { padding: 16px; }
.mt-16 { margin-top: 16px; }
.mb-16 { margin-bottom: 16px; }
.mb-8 { margin-bottom: 8px; }
.w-full { width: 100%; }
.flex { display: flex; }
.justify-end { justify-content: flex-end; }
</style>
