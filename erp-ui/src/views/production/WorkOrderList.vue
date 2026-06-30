<template>
  <div class="work-order-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="工单号">
          <el-input v-model="searchForm.woNo" placeholder="请输入工单号" clearable />
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="searchForm.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待开工" value="PENDING" />
            <el-option label="生产中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已关闭" value="CLOSED" />
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增工单</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="woNo" label="工单号" width="150" />
        <el-table-column prop="productName" label="产品名称" width="150" />
        <el-table-column prop="quantity" label="计划数量" width="100" align="right" />
        <el-table-column prop="completedQty" label="已完成数量" width="110" align="right">
          <template #default="{ row }">
            <el-progress :percentage="row.quantity ? Math.round((row.completedQty || 0) / row.quantity * 100) : 0" :stroke-width="6" :show-text="false" />
            <span class="ml-8">{{ row.completedQty || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="计划开始" width="120" />
        <el-table-column prop="endDate" label="计划结束" width="120" />
        <el-table-column prop="workshop" label="车间" width="100" />
        <el-table-column prop="priority" label="优先级" width="80">
          <template #default="{ row }">
            <el-tag :type="priorityTag(row.priority)" size="small">{{ priorityLabel(row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link size="small" @click="handleViewRouting(row)">工艺路线</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link size="small" @click="handleStart(row)">开工</el-button>
            <el-button v-if="row.status === 'IN_PROGRESS'" type="warning" link size="small" @click="handleComplete(row)">完工</el-button>
            <el-button v-if="row.status !== 'CLOSED'" type="info" link size="small" @click="handleClose(row)">关闭</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row)">
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

    <WorkOrderFormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { workOrderPage, workOrderAdd, workOrderUpdate, workOrderDelete, workOrderGetById, workOrderStart, workOrderComplete } from '@/api/modules/production'
import WorkOrderFormDialog from './components/WorkOrderFormDialog.vue'

const router = useRouter()
const loading = ref(false)
const searchForm = ref({ woNo: '', productName: '', status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)

const priorityMap = { HIGH: '高', MEDIUM: '中', LOW: '低' }
const priorityTagMap = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }
const statusMap = { PENDING: '待开工', IN_PROGRESS: '生产中', COMPLETED: '已完成', CLOSED: '已关闭' }
const statusTagMap = { PENDING: 'info', IN_PROGRESS: 'primary', COMPLETED: 'success', CLOSED: 'default' }

const priorityLabel = (p) => priorityMap[p] || p
const priorityTag = (p) => priorityTagMap[p] || ''
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await workOrderPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取工单列表失败') } finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { woNo: '', productName: '', status: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增工单'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑工单'
  try { const res = await workOrderGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleViewRouting = (row) => router.push({ name: 'WoRouting', query: { workOrderId: row.id } })

const handleStart = async (row) => {
  try {
    await ElMessageBox.confirm('确定要开工该工单吗？', '开工确认', { type: 'info' })
    const res = await workOrderStart(row.id)
    res.code === 200 ? (ElMessage.success('已开工'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleComplete = async (row) => {
  try {
    await ElMessageBox.confirm('确定该工单已完工吗？', '完工确认', { type: 'success' })
    const res = await workOrderComplete(row.id)
    res.code === 200 ? (ElMessage.success('已完工'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleClose = async (row) => {
  try {
    await ElMessageBox.confirm('确定要关闭该工单吗？', '关闭工单', { type: 'warning' })
    const res = await workOrderUpdate({ id: row.id, status: 'CLOSED' })
    res.code === 200 ? (ElMessage.success('已关闭'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleDelete = async (row) => {
  try {
    const res = await workOrderDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? workOrderAdd : workOrderUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => fetchData())
</script>

<style scoped>
.work-order-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.ml-8 { margin-left: 8px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
