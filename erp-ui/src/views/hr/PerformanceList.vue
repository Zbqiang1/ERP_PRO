<template>
  <div class="performance-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="员工">
          <el-select v-model="searchForm.employeeId" placeholder="请选择员工" clearable filterable>
            <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="考核期间">
          <el-date-picker v-model="searchForm.assessmentPeriod" type="month" placeholder="选择月份" value-format="YYYY-MM" clearable />
        </el-form-item>
        <el-form-item label="等级">
          <el-select v-model="searchForm.grade" placeholder="请选择等级" clearable>
            <el-option label="A" value="A" />
            <el-option label="B" value="B" />
            <el-option label="C" value="C" />
            <el-option label="D" value="D" />
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增考核</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="employeeName" label="员工" width="100" />
        <el-table-column prop="assessmentPeriod" label="考核期间" width="120" />
        <el-table-column prop="assessorName" label="考核人" width="100" />
        <el-table-column prop="kpiScore" label="KPI得分" width="100" align="right" />
        <el-table-column prop="evaluation" label="评价" min-width="150" show-overflow-tooltip />
        <el-table-column prop="grade" label="等级" width="80">
          <template #default="{ row }">
            <el-tag :type="gradeTag(row.grade)">{{ row.grade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link size="small" @click="handleViewHistory(row)">历史</el-button>
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

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :employee-options="employeeOptions" @confirm="handleSave"
    />

    <el-dialog v-model="historyVisible" title="考核历史" width="700px" destroy-on-close>
      <el-table :data="historyData" border stripe size="small" class="w-full" v-loading="historyLoading">
        <el-table-column prop="assessmentPeriod" label="考核期间" width="120" />
        <el-table-column prop="assessorName" label="考核人" width="100" />
        <el-table-column prop="kpiScore" label="KPI得分" width="100" align="right" />
        <el-table-column prop="evaluation" label="评价" min-width="150" />
        <el-table-column prop="grade" label="等级" width="80">
          <template #default="{ row: r }">
            <el-tag :type="gradeTag(r.grade)">{{ r.grade }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { performancePage, performanceAdd, performanceUpdate, performanceDelete, performanceGetById, performanceHistoryByEmployee } from '@/api/modules/hr'
import { employeeListAll } from '@/api/modules/hr'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ employeeId: null, assessmentPeriod: '', grade: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const employeeOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const historyVisible = ref(false); const historyData = ref([]); const historyLoading = ref(false)

const gradeTag = (g) => ({ A: 'success', B: 'primary', C: 'warning', D: 'danger' }[g] || '')

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await performancePage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取绩效列表失败') } finally { loading.value = false }
}

const fetchEmployees = async () => {
  try { const res = await employeeListAll(); employeeOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { employeeId: null, assessmentPeriod: '', grade: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增考核'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑考核'
  try { const res = await performanceGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleViewHistory = async (row) => {
  historyVisible.value = true; historyLoading.value = true
  try {
    const res = await performanceHistoryByEmployee(row.employeeId || row.id)
    historyData.value = res.code === 200 ? (res.data || []) : []
  } catch { historyData.value = [] } finally { historyLoading.value = false }
}
const handleDelete = async (row) => {
  try {
    const res = await performanceDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? performanceAdd : performanceUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchEmployees(); fetchData() })
</script>

<style scoped>
.performance-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
