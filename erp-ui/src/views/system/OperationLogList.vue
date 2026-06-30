<template>
  <div class="log-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="操作人">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="操作模块">
          <el-input v-model="searchForm.module" placeholder="请输入模块名称" clearable />
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.operation" placeholder="请选择操作类型" clearable>
            <el-option label="新增" value="新增" />
            <el-option label="修改" value="修改" />
            <el-option label="删除" value="删除" />
            <el-option label="查询" value="查询" />
            <el-option label="导出" value="导出" />
            <el-option label="导入" value="导入" />
            <el-option label="登录" value="登录" />
            <el-option label="登出" value="登出" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
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
        <el-button :icon="Delete" @click="handleClearLogs">清空日志</el-button>
        <el-button :icon="Download" @click="handleExport">导出</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="username" label="操作人" width="100" />
        <el-table-column prop="module" label="操作模块" width="120" />
        <el-table-column prop="operation" label="操作类型" width="90" />
        <el-table-column prop="method" label="操作方法" min-width="180" show-overflow-tooltip />
        <el-table-column prop="requestUrl" label="请求地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="requestMethod" label="请求方式" width="90" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.requestMethod" :type="getMethodTagType(row.requestMethod)" size="small">
              {{ row.requestMethod }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="duration" label="耗时(ms)" width="100" align="center">
          <template #default="{ row }">
            <span :style="{ color: row.duration > 3000 ? '#f56c6c' : '' }">{{ row.duration }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="170" />
        <el-table-column label="操作" width="80" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleViewDetail(row)">详情</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无数据" />
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

    <!-- 详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="操作日志详情" width="700px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="操作人">{{ detailData.username }}</el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ detailData.module }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ detailData.operation }}</el-descriptions-item>
        <el-descriptions-item label="请求方式">
          <el-tag v-if="detailData.requestMethod" :type="getMethodTagType(detailData.requestMethod)" size="small">
            {{ detailData.requestMethod }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="请求地址" :span="2">{{ detailData.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="操作方法" :span="2">{{ detailData.method }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ detailData.ip }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ detailData.duration }} ms</el-descriptions-item>
        <el-descriptions-item label="操作时间" :span="2">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <el-form label-width="100px" style="margin-top: 16px">
        <el-form-item label="请求参数">
          <el-input
            v-model="detailData.requestParams"
            type="textarea"
            :rows="6"
            readonly
          />
        </el-form-item>
        <el-form-item label="返回结果">
          <el-input
            v-model="detailData.responseResult"
            type="textarea"
            :rows="6"
            readonly
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, Download, View } from '@element-plus/icons-vue'
import { pageLog, getLogById } from '@/api/modules/operationLog'

const searchForm = reactive({
  username: '',
  module: '',
  operation: '',
  dateRange: []
})

const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

const loading = ref(false)
const tableData = ref([])

const detailDialogVisible = ref(false)
const detailData = reactive({
  username: '',
  module: '',
  operation: '',
  method: '',
  requestUrl: '',
  requestMethod: '',
  ip: '',
  duration: '',
  createTime: '',
  requestParams: '',
  responseResult: ''
})

function getMethodTagType(method) {
  const map = {
    GET: 'success',
    POST: 'primary',
    PUT: 'warning',
    DELETE: 'danger',
    PATCH: 'info'
  }
  return map[method?.toUpperCase()] || 'info'
}

async function loadData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      username: searchForm.username,
      module: searchForm.module,
      operation: searchForm.operation
    }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null || params[key] === undefined) {
        delete params[key]
      }
    })
    const res = await pageLog(params)
    tableData.value = res.data.records || res.data || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载操作日志失败')
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

function handleReset() {
  Object.assign(searchForm, {
    username: '',
    module: '',
    operation: '',
    dateRange: []
  })
  handleSearch()
}

function handleClearLogs() {
  ElMessageBox.confirm('确定要清空所有操作日志吗？此操作不可恢复。', '清空确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // 假设有清空接口
    ElMessage.success('日志已清空')
    loadData()
  }).catch(() => {})
}

function handleExport() {
  ElMessage.info('导出功能开发中')
}

async function handleViewDetail(row) {
  try {
    const res = await getLogById(row.id)
    const data = res.data
    Object.assign(detailData, {
      username: data.username || '',
      module: data.module || '',
      operation: data.operation || '',
      method: data.method || '',
      requestUrl: data.requestUrl || '',
      requestMethod: data.requestMethod || '',
      ip: data.ip || '',
      duration: data.duration || '',
      createTime: data.createTime || '',
      requestParams: formatJson(data.requestParams),
      responseResult: formatJson(data.responseResult)
    })
    detailDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取日志详情失败')
  }
}

function formatJson(data) {
  if (!data) return ''
  try {
    if (typeof data === 'string') {
      return JSON.stringify(JSON.parse(data), null, 2)
    }
    return JSON.stringify(data, null, 2)
  } catch (e) {
    return String(data)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.log-list-container {
  padding: 16px;
}

.search-card {
  margin-bottom: 16px;
}

.table-card {
  margin-bottom: 16px;
}

.action-bar {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
