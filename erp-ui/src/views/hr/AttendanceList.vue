<template>
  <div class="attendance-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="员工">
          <el-select v-model="searchForm.employeeId" placeholder="请选择员工" clearable filterable>
            <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="考勤日期">
          <el-date-picker v-model="searchForm.attendanceDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" clearable />
        </el-form-item>
        <el-form-item label="考勤类型">
          <el-select v-model="searchForm.attendanceType" placeholder="请选择" clearable>
            <el-option label="正常" value="NORMAL" />
            <el-option label="迟到" value="LATE" />
            <el-option label="早退" value="EARLY" />
            <el-option label="旷工" value="ABSENT" />
            <el-option label="请假" value="LEAVE" />
            <el-option label="加班" value="OVERTIME" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
          <el-button type="success" icon="Upload" @click="handleBatchImport">批量导入</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="employeeName" label="员工" width="100" />
        <el-table-column prop="attendanceDate" label="考勤日期" width="120" />
        <el-table-column prop="checkInTime" label="签到时间" width="100" />
        <el-table-column prop="checkOutTime" label="签退时间" width="100" />
        <el-table-column prop="attendanceType" label="考勤类型" width="100">
          <template #default="{ row }">
            <el-tag :type="attendanceTypeTag(row.attendanceType)">{{ attendanceTypeLabel(row.attendanceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
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

    <el-dialog v-model="importVisible" title="批量导入考勤" width="500px" destroy-on-close>
      <el-upload
        ref="uploadRef"
        drag
        :action="uploadAction"
        :headers="uploadHeaders"
        :limit="1"
        accept=".xlsx,.xls"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :auto-upload="false"
      >
        <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">仅支持 .xlsx/.xls 格式的Excel文件</div>
        </template>
      </el-upload>
      <template #footer>
        <el-button @click="importVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUploadSubmit">开始上传</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { attendancePage, attendanceDelete, attendanceBatchImport } from '@/api/modules/hr'
import { employeeListAll } from '@/api/modules/hr'

const loading = ref(false)
const searchForm = ref({ employeeId: null, attendanceDate: '', attendanceType: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const employeeOptions = ref([])

const importVisible = ref(false)
const uploadRef = ref(null)
const uploadAction = ref('/api/hr/attendance/batchImport')
const uploadHeaders = ref({})

const typeMap = { NORMAL: '正常', LATE: '迟到', EARLY: '早退', ABSENT: '旷工', LEAVE: '请假', OVERTIME: '加班' }
const typeTagMap = { NORMAL: 'success', LATE: 'warning', EARLY: 'warning', ABSENT: 'danger', LEAVE: 'info', OVERTIME: 'primary' }
const attendanceTypeLabel = (t) => typeMap[t] || t
const attendanceTypeTag = (t) => typeTagMap[t] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await attendancePage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取考勤列表失败') } finally { loading.value = false }
}

const fetchEmployees = async () => {
  try { const res = await employeeListAll(); employeeOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { employeeId: null, attendanceDate: '', attendanceType: '' }; handleSearch() }

const handleBatchImport = () => { importVisible.value = true }
const handleUploadSubmit = () => { uploadRef.value?.submit() }
const handleUploadSuccess = (res) => {
  if (res.code === 200) { ElMessage.success('导入成功'); importVisible.value = false; fetchData() }
  else ElMessage.error(res.msg || '导入失败')
}
const handleUploadError = () => { ElMessage.error('上传失败') }

const handleDelete = async (row) => {
  try {
    const res = await attendanceDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}

onMounted(() => { fetchEmployees(); fetchData() })
</script>

<style scoped>
.attendance-list { padding: 16px; }
.mt-16 { margin-top: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
