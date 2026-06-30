<template>
  <div class="inventory-check-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="盘点单号">
          <el-input v-model="searchForm.checkNo" placeholder="请输入盘点单号" clearable />
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="盘点类型">
          <el-select v-model="searchForm.checkType" placeholder="请选择盘点类型" clearable>
            <el-option label="周期盘点" value="PERIODIC" />
            <el-option label="随机盘点" value="RANDOM" />
            <el-option label="全面盘点" value="FULL" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增盘点单</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="checkNo" label="盘点单号" width="150" />
        <el-table-column prop="warehouseName" label="仓库" width="150" />
        <el-table-column prop="checkType" label="盘点类型" width="110">
          <template #default="{ row }">{{ checkTypeLabel(row.checkType) }}</template>
        </el-table-column>
        <el-table-column prop="checkDate" label="盘点日期" width="120" />
        <el-table-column prop="checkStatus" label="盘点状态" width="110">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.checkStatus)">{{ statusLabel(row.checkStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.checkStatus === 'CHECKING'" type="success" link size="small" @click="handleConfirm(row)">确认</el-button>
            <el-button v-if="row.checkStatus === 'CHECKING'" type="warning" link size="small" @click="handleAdjust(row)">调整</el-button>
            <el-popconfirm title="确定删除该盘点单？" @confirm="handleDelete(row)">
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

    <el-dialog v-model="detailVisible" title="盘点单详情" width="900px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="盘点单号">{{ detailData.checkNo }}</el-descriptions-item>
          <el-descriptions-item label="仓库">{{ detailData.warehouseName }}</el-descriptions-item>
          <el-descriptions-item label="盘点类型">{{ checkTypeLabel(detailData.checkType) }}</el-descriptions-item>
          <el-descriptions-item label="盘点日期">{{ detailData.checkDate }}</el-descriptions-item>
          <el-descriptions-item label="状态"><el-tag :type="statusTagType(detailData.checkStatus)">{{ statusLabel(detailData.checkStatus) }}</el-tag></el-descriptions-item>
          <el-descriptions-item label="盘点人">{{ detailData.checkerName }}</el-descriptions-item>
        </el-descriptions>
        <h4 class="mt-16 mb-8">盘点明细</h4>
        <el-table :data="detailData.items || []" border stripe size="small" class="w-full">
          <el-table-column prop="materialName" label="物料" width="150" />
          <el-table-column prop="bookQty" label="账面数量" width="100" align="right" />
          <el-table-column prop="actualQty" label="实际数量" width="100" align="right" />
          <el-table-column label="差异数量" width="100" align="right">
            <template #default="{ row }">
              <span :style="{ color: (row.actualQty - row.bookQty) !== 0 ? 'red' : '' }">
                {{ ((row.actualQty || 0) - (row.bookQty || 0)) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="diffReason" label="差异原因" min-width="150" />
        </el-table>
      </template>
    </el-dialog>

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :warehouse-options="warehouseOptions" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { inventoryCheckPage, inventoryCheckAdd, inventoryCheckUpdate, inventoryCheckDelete, inventoryCheckGetById, inventoryCheckConfirm, inventoryCheckAdjust } from '@/api/modules/inventory'
import { warehouseListAll } from '@/api/modules/inventory'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ checkNo: '', warehouseId: null, checkType: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const warehouseOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const checkTypeMap = { PERIODIC: '周期盘点', RANDOM: '随机盘点', FULL: '全面盘点' }
const statusMap = { CHECKING: '盘点中', CONFIRMED: '已确认', ADJUSTED: '已调整' }
const statusTagMap = { CHECKING: 'warning', CONFIRMED: 'success', ADJUSTED: 'primary' }

const checkTypeLabel = (t) => checkTypeMap[t] || t
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await inventoryCheckPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取盘点单列表失败') } finally { loading.value = false }
}

const fetchWarehouses = async () => {
  try { const res = await warehouseListAll(); warehouseOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { checkNo: '', warehouseId: null, checkType: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增盘点单'; currentRow.value = null; dialogVisible.value = true }
const handleDetail = async (row) => {
  try { const res = await inventoryCheckGetById(row.id); detailData.value = res.code === 200 ? res.data : row } catch { detailData.value = row }
  detailVisible.value = true
}
const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确认盘点结果吗？', '确认盘点', { type: 'warning' })
    const res = await inventoryCheckConfirm(row.id)
    res.code === 200 ? (ElMessage.success('盘点已确认'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleAdjust = async (row) => {
  try {
    await ElMessageBox.confirm('确定要调整库存吗？', '库存调整', { type: 'warning' })
    const res = await inventoryCheckAdjust(row.id)
    res.code === 200 ? (ElMessage.success('库存已调整'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleDelete = async (row) => {
  try {
    const res = await inventoryCheckDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? inventoryCheckAdd : inventoryCheckUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchWarehouses(); fetchData() })
</script>

<style scoped>
.inventory-check-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; } .mb-8 { margin-bottom: 8px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
