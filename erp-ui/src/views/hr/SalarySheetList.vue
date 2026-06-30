<template>
  <div class="salary-sheet-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="员工">
          <el-select v-model="searchForm.employeeId" placeholder="请选择员工" clearable filterable>
            <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="薪资月份">
          <el-date-picker v-model="searchForm.salaryMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已发放" value="PAID" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
          <el-button type="success" icon="TrendCharts" @click="handleCalculate">计算薪资</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full" show-summary :summary-method="getSummaries">
        <el-table-column prop="salaryNo" label="薪资单号" width="150" />
        <el-table-column prop="employeeName" label="员工" width="100" />
        <el-table-column prop="salaryMonth" label="薪资月份" width="120" />
        <el-table-column prop="basicSalary" label="基本工资" width="120" align="right">
          <template #default="{ row }">{{ row.basicSalary?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="performanceBonus" label="绩效奖金" width="110" align="right">
          <template #default="{ row }">{{ row.performanceBonus?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="overtimePay" label="加班费" width="100" align="right">
          <template #default="{ row }">{{ row.overtimePay?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="deduction" label="扣款" width="100" align="right">
          <template #default="{ row }">{{ row.deduction?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="socialInsurance" label="社保" width="100" align="right">
          <template #default="{ row }">{{ row.socialInsurance?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="tax" label="个税" width="100" align="right">
          <template #default="{ row }">{{ row.tax?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="netSalary" label="实发工资" width="120" align="right">
          <template #default="{ row }"><strong>{{ row.netSalary?.toFixed(2) }}</strong></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { salarySheetPage, salarySheetCalculate } from '@/api/modules/hr'
import { employeeListAll } from '@/api/modules/hr'

const loading = ref(false)
const searchForm = ref({ employeeId: null, salaryMonth: '', status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const employeeOptions = ref([])

const statusMap = { DRAFT: '草稿', CONFIRMED: '已确认', PAID: '已发放' }
const statusTagMap = { DRAFT: 'info', CONFIRMED: 'success', PAID: 'primary' }
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const getSummaries = (param) => {
  const { columns, data } = param
  const sums = []
  const sumFields = ['basicSalary', 'performanceBonus', 'overtimePay', 'deduction', 'socialInsurance', 'tax', 'netSalary']
  columns.forEach((col, index) => {
    if (index === 0) { sums[index] = '合计'; return }
    if (sumFields.includes(col.property)) {
      sums[index] = data.reduce((prev, cur) => prev + (cur[col.property] || 0), 0).toFixed(2)
    }
  })
  return sums
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await salarySheetPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取薪资单列表失败') } finally { loading.value = false }
}

const fetchEmployees = async () => {
  try { const res = await employeeListAll(); employeeOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { employeeId: null, salaryMonth: '', status: '' }; handleSearch() }

const handleCalculate = async () => {
  const month = searchForm.value.salaryMonth
  if (!month) { ElMessage.warning('请先选择薪资月份'); return }
  try {
    await ElMessageBox.confirm(`确定要计算 ${month} 的薪资吗？`, '计算薪资', { type: 'info' })
    loading.value = true
    const res = await salarySheetCalculate({ salaryMonth: month })
    if (res.code === 200) { ElMessage.success('薪资计算成功'); fetchData() } else { ElMessage.error(res.msg || '计算失败') }
  } catch { /* */ } finally { loading.value = false }
}

onMounted(() => { fetchEmployees(); fetchData() })
</script>

<style scoped>
.salary-sheet-list { padding: 16px; }
.mt-16 { margin-top: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
