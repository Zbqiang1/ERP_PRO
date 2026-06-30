<template>
  <div class="process-instance-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="流程名称">
          <el-input v-model="searchForm.processName" placeholder="请输入流程名称" clearable />
        </el-form-item>
        <el-form-item label="业务标识">
          <el-input v-model="searchForm.businessKey" placeholder="请输入业务标识" clearable />
        </el-form-item>
        <el-form-item label="发起人">
          <el-input v-model="searchForm.startUserId" placeholder="请输入发起人" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="运行中" value="running" />
            <el-option label="已完成" value="completed" />
            <el-option label="已终止" value="terminated" />
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
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="instanceId" label="实例ID" width="130" show-overflow-tooltip />
        <el-table-column prop="processName" label="流程名称" min-width="160" />
        <el-table-column prop="businessKey" label="业务标识" min-width="140" />
        <el-table-column prop="startUserId" label="发起人" width="100" />
        <el-table-column prop="currentTask" label="当前节点" min-width="140" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'running'" type="primary" size="small">运行中</el-tag>
            <el-tag v-else-if="row.status === 'completed'" type="success" size="small">已完成</el-tag>
            <el-tag v-else-if="row.status === 'terminated'" type="danger" size="small">已终止</el-tag>
            <el-tag v-else type="info" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="发起时间" width="170" />
        <el-table-column prop="endTime" label="结束时间" width="170" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleViewDiagram(row)">流程图</el-button>
            <el-button type="success" link size="small" :icon="Clock" @click="handleViewHistory(row)">审批历史</el-button>
            <el-button
              v-if="row.status === 'running'"
              type="danger"
              link
              size="small"
              :icon="Close"
              @click="handleTerminate(row)"
            >
              终止
            </el-button>
            <el-button
              v-if="row.status !== 'running'"
              type="danger"
              link
              size="small"
              :icon="Delete"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无流程实例" />
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

    <!-- 流程图对话框（高亮当前节点） -->
    <el-dialog v-model="diagramDialogVisible" title="流程图" width="950px" destroy-on-close>
      <div class="diagram-wrapper">
        <el-empty v-if="!diagramData.url" description="暂无流程图数据" />
        <div v-else class="diagram-content">
          <img :src="diagramData.url" alt="流程图" style="max-width: 100%; height: auto" />
          <!-- 当前节点高亮标注 -->
          <div v-if="diagramData.currentNodes && diagramData.currentNodes.length > 0" class="current-nodes-info">
            <el-tag type="warning" size="small">当前节点:</el-tag>
            <el-tag
              v-for="node in diagramData.currentNodes"
              :key="node"
              type="danger"
              size="small"
              style="margin-left: 4px"
            >
              {{ node }}
            </el-tag>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="diagramDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 审批历史对话框 -->
    <el-dialog v-model="historyDialogVisible" title="审批历史" width="700px" destroy-on-close>
      <el-descriptions :column="2" border style="margin-bottom: 16px">
        <el-descriptions-item label="流程实例ID">{{ historyInstance.instanceId }}</el-descriptions-item>
        <el-descriptions-item label="流程名称">{{ historyInstance.processName }}</el-descriptions-item>
        <el-descriptions-item label="业务标识">{{ historyInstance.businessKey }}</el-descriptions-item>
        <el-descriptions-item label="发起人">{{ historyInstance.startUserId }}</el-descriptions-item>
        <el-descriptions-item label="发起时间">{{ historyInstance.startTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(historyInstance.status)" size="small">
            {{ getStatusText(historyInstance.status) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <h4 style="margin-bottom: 12px">审批轨迹</h4>
      <el-timeline>
        <el-timeline-item
          v-for="item in approvalHistoryList"
          :key="item.id"
          :timestamp="item.createTime || item.approvalTime"
          :type="getApprovalTimelineType(item.result)"
          :hollow="item.activityType === 'startEvent' || item.activityType === 'endEvent'"
          placement="top"
        >
          <el-card shadow="never" size="small">
            <template #header>
              <div class="timeline-card-header">
                <span>{{ item.taskName || item.activityName }}</span>
                <el-tag
                  v-if="item.result === 'approved'"
                  type="success"
                  size="small"
                >
                  已通过
                </el-tag>
                <el-tag
                  v-else-if="item.result === 'rejected'"
                  type="danger"
                  size="small"
                >
                  已驳回
                </el-tag>
                <el-tag
                  v-else
                  size="small"
                >
                  {{ item.activityType === 'startEvent' ? '开始' : item.activityType === 'endEvent' ? '结束' : '进行中' }}
                </el-tag>
              </div>
            </template>
            <p v-if="item.assignee"><strong>处理人：</strong>{{ item.assignee }}</p>
            <p v-if="item.comment"><strong>审批意见：</strong>{{ item.comment }}</p>
            <p v-if="item.duration"><strong>耗时：</strong>{{ item.duration }}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item v-if="approvalHistoryList.length === 0" timestamp="">
          <el-empty description="暂无审批历史" :image-size="40" />
        </el-timeline-item>
      </el-timeline>
      <template #footer>
        <el-button @click="historyDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, View, Clock, Close, Delete } from '@element-plus/icons-vue'
// import { list as listInstances, viewDiagram, viewHistory, terminate, delete: deleteInst } from '@/api/modules/workflow'

const searchForm = reactive({
  processName: '',
  businessKey: '',
  startUserId: '',
  status: '',
  dateRange: []
})

const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

const loading = ref(false)
const tableData = ref([])

// 流程图对话框
const diagramDialogVisible = ref(false)
const diagramData = reactive({
  url: '',
  currentNodes: []
})

// 审批历史对话框
const historyDialogVisible = ref(false)
const historyInstance = reactive({
  instanceId: '',
  processName: '',
  businessKey: '',
  startUserId: '',
  startTime: '',
  status: ''
})
const approvalHistoryList = ref([])

function getStatusTagType(status) {
  const map = { running: 'primary', completed: 'success', terminated: 'danger' }
  return map[status] || 'info'
}

function getStatusText(status) {
  const map = { running: '运行中', completed: '已完成', terminated: '已终止' }
  return map[status] || status || '未知'
}

function getApprovalTimelineType(result) {
  if (result === 'approved') return 'success'
  if (result === 'rejected') return 'danger'
  return 'primary'
}

async function loadData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
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
    // const res = await listInstances(params)
    // tableData.value = res.data.records || res.data || []
    // pagination.total = res.data.total || 0
    tableData.value = []
    pagination.total = 0
  } catch (error) {
    ElMessage.error('加载流程实例列表失败')
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
    processName: '',
    businessKey: '',
    startUserId: '',
    status: '',
    dateRange: []
  })
  handleSearch()
}

// 查看流程图（高亮当前节点）
async function handleViewDiagram(row) {
  diagramData.url = ''
  diagramData.currentNodes = []
  try {
    // const res = await viewDiagram(row.instanceId)
    // diagramData.url = res.data?.url || res.data || ''
    // diagramData.currentNodes = res.data?.currentNodes || []
  } catch (error) {
    ElMessage.error('加载流程图失败')
  }
  diagramDialogVisible.value = true
}

// 查看审批历史
async function handleViewHistory(row) {
  Object.assign(historyInstance, {
    instanceId: row.instanceId || '',
    processName: row.processName || '',
    businessKey: row.businessKey || '',
    startUserId: row.startUserId || '',
    startTime: row.startTime || '',
    status: row.status || ''
  })
  try {
    // const res = await viewHistory(row.instanceId)
    // approvalHistoryList.value = res.data || []
    approvalHistoryList.value = []
  } catch (error) {
    approvalHistoryList.value = []
    ElMessage.error('加载审批历史失败')
  }
  historyDialogVisible.value = true
}

// 终止流程
function handleTerminate(row) {
  ElMessageBox.confirm(`确定要终止流程实例"${row.instanceId}"吗？终止后流程将结束运行。`, '终止确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // await terminate({ instanceId: row.instanceId })
      ElMessage.success('流程已终止')
      loadData()
    } catch (error) {
      ElMessage.error('终止失败')
    }
  }).catch(() => {})
}

// 删除流程实例
function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除流程实例"${row.instanceId}"吗？删除后不可恢复。`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // await deleteInst(row.instanceId)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.process-instance-container {
  padding: 16px;
}

.search-card {
  margin-bottom: 16px;
}

.table-card {
  margin-bottom: 16px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.diagram-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.diagram-content {
  width: 100%;
  text-align: center;
}

.current-nodes-info {
  margin-top: 12px;
  text-align: center;
}

.timeline-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
