<template>
  <div class="leave-sheet-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="请假单号">
          <el-input v-model="searchForm.leaveNo" placeholder="请输入请假单号" clearable />
        </el-form-item>
        <el-form-item label="员工">
          <el-select v-model="searchForm.employeeId" placeholder="请选择员工" clearable filterable>
            <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待审批" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增请假单</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="leaveNo" label="请假单号" width="150" />
        <el-table-column prop="employeeName" label="员工姓名" width="100" />
        <el-table-column prop="leaveType" label="请假类型" width="100">
          <template #default="{ row }">
            <el-tag :type="leaveTypeTag(row.leaveType)">{{ leaveTypeLabel(row.leaveType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="170" />
        <el-table-column prop="endTime" label="结束时间" width="170" />
        <el-table-column prop="totalHours" label="总时数(h)" width="100" align="right" />
        <el-table-column prop="reason" label="请假原因" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">查看</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link size="small" @click="handleApprove(row)">审批通过</el-button>
            <el-button v-if="row.status === 'PENDING'" type="danger" link size="small" @click="handleReject(row)">驳回</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button v-if="row.status !== 'APPROVED'" type="danger" link size="small">删除</el-button>
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

    <LeaveSheetFormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :employee-options="employeeOptions" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { leaveSheetPage, leaveSheetAdd, leaveSheetUpdate, leaveSheetDelete, leaveSheetGetById, leaveSheetApprove } from '@/api/modules/hr'
import { employeeListAll } from '@/api/modules/hr'
import LeaveSheetFormDialog from './components/LeaveSheetFormDialog.vue'

const loading = ref(false)
const searchForm = ref({ leaveNo: '', employeeId: null, status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const employeeOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)

const leaveTypeMap = { PERSONAL: '事假', SICK: '病假', ANNUAL: '年假', MARRIAGE: '婚假', MATERNITY: '产假', COMPENSATORY: '调休' }
const leaveTypeTagMap = { PERSONAL: 'info', SICK: 'warning', ANNUAL: 'success', MARRIAGE: 'primary', MATERNITY: 'danger', COMPENSATORY: '' }
const statusMap = { PENDING: '待审批', APPROVED: '已通过', REJECTED: '已驳回' }
const statusTagMap = { PENDING: 'warning', APPROVED: 'success', REJECTED: 'danger' }
const leaveTypeLabel = (t) => leaveTypeMap[t] || t
const leaveTypeTag = (t) => leaveTypeTagMap[t] || ''
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await leaveSheetPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取请假单列表失败') } finally { loading.value = false }
}

const fetchEmployees = async () => {
  try { const res = await employeeListAll(); employeeOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { leaveNo: '', employeeId: null, status: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增请假单'; currentRow.value = null; dialogVisible.value = true }
const handleDetail = async (row) => {
  dialogMode.value = 'detail'; dialogTitle.value = '请假单详情'
  try { const res = await leaveSheetGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleApprove = async (row) => {
  try {
    await ElMessageBox.confirm('确定审批通过该请假单？', '审批通过', { type: 'success' })
    const res = await leaveSheetApprove({ id: row.id, status: 'APPROVED' })
    res.code === 200 ? (ElMessage.success('审批通过'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleReject = async (row) => {
  try {
    const { value } = await ElMessageBox.prompt('请输入驳回理由', '驳回请假单', { type: 'warning', inputType: 'textarea' })
    const res = await leaveSheetApprove({ id: row.id, status: 'REJECTED', rejectReason: value })
    res.code === 200 ? (ElMessage.success('已驳回'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleDelete = async (row) => {
  try {
    const res = await leaveSheetDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? leaveSheetAdd : leaveSheetUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchEmployees(); fetchData() })
</script>

<style scoped>
.leave-sheet-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
