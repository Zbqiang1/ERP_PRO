<template>
  <div class="process-definition-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="流程名称">
          <el-input v-model="searchForm.name" placeholder="请输入流程名称" clearable />
        </el-form-item>
        <el-form-item label="流程标识">
          <el-input v-model="searchForm.key" placeholder="请输入流程标识" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格卡片 -->
    <el-card class="table-card" shadow="never">
      <div class="action-bar">
        <el-button type="primary" :icon="Upload" @click="handleDeployNew">部署新流程</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="processId" label="流程ID" min-width="120" show-overflow-tooltip />
        <el-table-column prop="name" label="流程名称" min-width="160" />
        <el-table-column prop="key" label="流程标识" min-width="140" />
        <el-table-column prop="version" label="版本号" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">
              {{ row.status === 1 ? '已发布' : '已挂起' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deploymentTime" label="部署时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 1"
              type="warning"
              link
              size="small"
              :icon="VideoPause"
              @click="handleSuspend(row)"
            >
              挂起
            </el-button>
            <el-button
              v-else
              type="success"
              link
              size="small"
              :icon="VideoPlay"
              @click="handleActivate(row)"
            >
              激活
            </el-button>
            <el-button type="primary" link size="small" :icon="View" @click="handleViewDetail(row)">详情</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无流程定义" />
        </template>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </el-card>

    <!-- 部署新流程对话框 -->
    <el-dialog v-model="deployDialogVisible" title="部署新流程" width="550px" destroy-on-close>
      <el-form ref="deployFormRef" :model="deployForm" :rules="deployFormRules" label-width="100px">
        <el-form-item label="流程名称" prop="name">
          <el-input v-model="deployForm.name" placeholder="请输入流程名称" />
        </el-form-item>
        <el-form-item label="BPMN文件" prop="file">
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :limit="1"
            accept=".bpmn,.xml"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
          >
            <el-button type="primary" :icon="Upload">选择BPMN文件</el-button>
            <template #tip>
              <div class="el-upload__tip">仅支持 .bpmn 或 .xml 格式的流程定义文件</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="deployDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="deployLoading" @click="handleDeployConfirm">部署</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="流程定义详情" width="600px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="流程ID">{{ detailData.processId }}</el-descriptions-item>
        <el-descriptions-item label="流程名称">{{ detailData.name }}</el-descriptions-item>
        <el-descriptions-item label="流程标识">{{ detailData.key }}</el-descriptions-item>
        <el-descriptions-item label="版本号">{{ detailData.version }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 1 ? 'success' : 'warning'" size="small">
            {{ detailData.status === 1 ? '已发布' : '已挂起' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="部署时间">{{ detailData.deploymentTime }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description || '无' }}</el-descriptions-item>
        <el-descriptions-item label="BPMN文件" :span="2">{{ detailData.resourceName || '无' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Upload, VideoPause, VideoPlay, View, Delete } from '@element-plus/icons-vue'
// import { list, deploy, suspend, delete: deleteDef } from '@/api/modules/workflow'

const searchForm = reactive({
  name: '',
  key: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

const loading = ref(false)
const tableData = ref([])

// 部署对话框
const deployDialogVisible = ref(false)
const deployLoading = ref(false)
const deployFormRef = ref(null)
const uploadRef = ref(null)
const deployForm = reactive({
  name: '',
  file: null
})

const deployFormRules = {
  name: [{ required: true, message: '请输入流程名称', trigger: 'blur' }]
}

// 详情对话框
const detailDialogVisible = ref(false)
const detailData = reactive({
  processId: '',
  name: '',
  key: '',
  version: '',
  status: null,
  deploymentTime: '',
  description: '',
  resourceName: ''
})

async function loadData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    Object.keys(params).forEach(key => {
      if (params[key] === '') delete params[key]
    })
    // const res = await list(params)
    // tableData.value = res.data.records || res.data || []
    // pagination.total = res.data.total || 0
    tableData.value = []
    pagination.total = 0
  } catch (error) {
    ElMessage.error('加载流程定义列表失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

function handleReset() {
  searchForm.name = ''
  searchForm.key = ''
  handleSearch()
}

function handleDeployNew() {
  deployForm.name = ''
  deployForm.file = null
  deployDialogVisible.value = true
}

function handleFileChange(file) {
  deployForm.file = file.raw
}

function handleFileRemove() {
  deployForm.file = null
}

async function handleDeployConfirm() {
  const valid = await deployFormRef.value.validate().catch(() => false)
  if (!valid) return
  if (!deployForm.file) {
    ElMessage.warning('请选择BPMN文件')
    return
  }
  deployLoading.value = true
  try {
    // const formData = new FormData()
    // formData.append('name', deployForm.name)
    // formData.append('file', deployForm.file)
    // await deploy(formData)
    ElMessage.success('流程部署成功')
    deployDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('流程部署失败')
  } finally {
    deployLoading.value = false
  }
}

function handleSuspend(row) {
  ElMessageBox.confirm(`确定要挂起流程"${row.name}"吗？挂起后无法发起新流程。`, '挂起确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // await suspend({ processId: row.processId })
      ElMessage.success('流程已挂起')
      loadData()
    } catch (error) {
      ElMessage.error('挂起失败')
    }
  }).catch(() => {})
}

function handleActivate(row) {
  ElMessageBox.confirm(`确定要激活流程"${row.name}"吗？`, '激活确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // await activate({ processId: row.processId })
      ElMessage.success('流程已激活')
      loadData()
    } catch (error) {
      ElMessage.error('激活失败')
    }
  }).catch(() => {})
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除流程定义"${row.name}"吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // await deleteDef(row.processId)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

function handleViewDetail(row) {
  Object.assign(detailData, {
    processId: row.processId || '',
    name: row.name || '',
    key: row.key || '',
    version: row.version || '',
    status: row.status ?? null,
    deploymentTime: row.deploymentTime || '',
    description: row.description || '',
    resourceName: row.resourceName || ''
  })
  detailDialogVisible.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.process-definition-container {
  padding: 16px;
}

.search-card {
  margin-bottom: 16px;
}

.table-card {
  margin-bottom: 16px;
}

.action-bar {
  margin-bottom: 16px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
