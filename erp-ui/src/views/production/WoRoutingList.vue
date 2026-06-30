<template>
  <div class="wo-routing-list">
    <el-card shadow="never">
      <div class="flex justify-between items-center mb-16">
        <h3>工艺路线 - 工单号: {{ route.query.workOrderId || '-' }}</h3>
        <el-button icon="ArrowLeft" @click="$router.back()">返回工单列表</el-button>
      </div>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">添加工序</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="operationSeq" label="工序号" width="80" align="center" />
        <el-table-column prop="operationName" label="工序名称" width="150" />
        <el-table-column prop="workcenter" label="工作中心" width="120" />
        <el-table-column prop="plannedHours" label="计划工时(h)" width="110" align="right" />
        <el-table-column prop="actualHours" label="实际工时(h)" width="110" align="right" />
        <el-table-column prop="workerName" label="作业员" width="100" />
        <el-table-column prop="routingStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.routingStatus === 'COMPLETED' ? 'success' : row.routingStatus === 'IN_PROGRESS' ? 'primary' : 'info'">
              {{ routingStatusLabel(row.routingStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.routingStatus === 'PENDING'" type="success" link size="small" @click="handleCompleteStep(row)">完成</el-button>
            <el-button type="primary" link size="small" @click="handleAssignWorker(row)">分配人员</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="workerDialogVisible" title="分配作业员" width="450px" destroy-on-close>
      <el-form ref="workerFormRef" :model="workerForm" :rules="workerRules" label-width="100px">
        <el-form-item label="作业员" prop="workerId">
          <el-select v-model="workerForm.workerId" placeholder="请选择作业员" filterable class="w-full">
            <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="实际工时(h)" prop="actualHours">
          <el-input-number v-model="workerForm.actualHours" :min="0" :precision="1" class="w-full" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="workerDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleWorkerSubmit">确定</el-button>
      </template>
    </el-dialog>

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :work-order-id="route.query.workOrderId" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { woRoutingPage, woRoutingAdd, woRoutingUpdate, woRoutingDelete, woRoutingComplete, woRoutingAssignWorker } from '@/api/modules/production'
import { employeeListAll } from '@/api/modules/hr'
import FormDialog from '@/components/common/FormDialog.vue'

const route = useRoute()
const loading = ref(false)
const tableData = ref([])
const employeeOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const workerDialogVisible = ref(false); const workerRow = ref(null)
const workerFormRef = ref(null)
const workerForm = ref({ workerId: null, actualHours: 0 })
const workerRules = { workerId: [{ required: true, message: '请选择作业员', trigger: 'change' }] }

const routingStatusLabel = (s) => ({ PENDING: '待加工', IN_PROGRESS: '加工中', COMPLETED: '已完成' }[s] || s)

const fetchData = async () => {
  loading.value = true
  try {
    const params = { workOrderId: route.query.workOrderId }
    const res = await woRoutingPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || res.data || [] }
  } catch { ElMessage.error('获取工艺路线失败') } finally { loading.value = false }
}

const fetchEmployees = async () => {
  try { const res = await employeeListAll(); employeeOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增工序'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = (row) => { dialogMode.value = 'edit'; dialogTitle.value = '编辑工序'; currentRow.value = row; dialogVisible.value = true }

const handleCompleteStep = async (row) => {
  try {
    const res = await woRoutingComplete(row.id)
    res.code === 200 ? (ElMessage.success('工序已完成'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

const handleAssignWorker = (row) => {
  workerRow.value = row
  workerForm.value = { workerId: row.workerId || null, actualHours: row.actualHours || 0 }
  workerDialogVisible.value = true
}

const handleWorkerSubmit = async () => {
  const valid = await workerFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    const res = await woRoutingAssignWorker({ id: workerRow.value.id, ...workerForm.value })
    res.code === 200 ? (ElMessage.success('分配成功'), workerDialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

const handleDelete = async (row) => {
  try {
    const res = await woRoutingDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}

const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? woRoutingAdd : woRoutingUpdate
  try {
    const res = await apiMethod({ ...formData, workOrderId: route.query.workOrderId })
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchData(); fetchEmployees() })
</script>

<style scoped>
.wo-routing-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-between { justify-content: space-between; } .items-center { align-items: center; }
</style>
