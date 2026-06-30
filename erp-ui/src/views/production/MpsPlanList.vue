<template>
  <div class="mps-plan-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="计划单号">
          <el-input v-model="searchForm.planNo" placeholder="请输入计划单号" clearable />
        </el-form-item>
        <el-form-item label="计划月份">
          <el-date-picker v-model="searchForm.planMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="计划" value="PLANNED" />
            <el-option label="执行中" value="EXECUTING" />
            <el-option label="已完成" value="COMPLETED" />
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增计划</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="planNo" label="计划单号" width="150" />
        <el-table-column prop="planMonth" label="计划月份" width="120" />
        <el-table-column prop="productName" label="产品" width="150" />
        <el-table-column prop="plannedQty" label="计划数量" width="110" align="right" />
        <el-table-column prop="completedQty" label="已完成数量" width="110" align="right" />
        <el-table-column label="完成率" width="100" align="right">
          <template #default="{ row }">
            <el-progress
              :percentage="row.plannedQty ? Math.round((row.completedQty || 0) / row.plannedQty * 100) : 0"
              :stroke-width="8"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 'PLANNED'" type="success" link size="small" @click="handleStart(row)">开始执行</el-button>
            <el-button v-if="row.status === 'EXECUTING'" type="warning" link size="small" @click="handleComplete(row)">完成</el-button>
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

    <MpsPlanFormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mpsPlanPage, mpsPlanAdd, mpsPlanUpdate, mpsPlanDelete, mpsPlanGetById } from '@/api/modules/production'
import MpsPlanFormDialog from './components/MpsPlanFormDialog.vue'

const loading = ref(false)
const searchForm = ref({ planNo: '', planMonth: '', status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)

const statusMap = { PLANNED: '计划', EXECUTING: '执行中', COMPLETED: '已完成' }
const statusTagMap = { PLANNED: 'info', EXECUTING: 'warning', COMPLETED: 'success' }
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await mpsPlanPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取计划列表失败') } finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { planNo: '', planMonth: '', status: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增计划'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑计划'
  try { const res = await mpsPlanGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleStart = async (row) => {
  try {
    await ElMessageBox.confirm('确定要开始执行该计划吗？', '开始执行', { type: 'info' })
    const res = await mpsPlanUpdate({ id: row.id, status: 'EXECUTING' })
    res.code === 200 ? (ElMessage.success('已开始执行'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleComplete = async (row) => {
  try {
    await ElMessageBox.confirm('确定该计划已完成吗？', '完成计划', { type: 'success' })
    const res = await mpsPlanUpdate({ id: row.id, status: 'COMPLETED' })
    res.code === 200 ? (ElMessage.success('已完成'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleDelete = async (row) => {
  try {
    const res = await mpsPlanDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? mpsPlanAdd : mpsPlanUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => fetchData())
</script>

<style scoped>
.mps-plan-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
