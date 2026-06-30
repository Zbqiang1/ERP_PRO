<template>
  <div class="my-done-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="流程名称">
          <el-input v-model="searchForm.processName" placeholder="请输入流程名称" clearable />
        </el-form-item>
        <el-form-item label="业务标识">
          <el-input v-model="searchForm.businessKey" placeholder="请输入业务标识" clearable />
        </el-form-item>
        <el-form-item label="审批结果">
          <el-select v-model="searchForm.result" placeholder="请选择结果" clearable>
            <el-option label="已通过" value="approved" />
            <el-option label="已驳回" value="rejected" />
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
        <el-table-column prop="taskId" label="任务ID" width="120" show-overflow-tooltip />
        <el-table-column prop="processName" label="流程名称" min-width="160" />
        <el-table-column prop="businessKey" label="业务标识" min-width="140" />
        <el-table-column prop="taskName" label="审批节点" min-width="140" />
        <el-table-column prop="result" label="审批结果" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.result === 'approved' ? 'success' : 'danger'" size="small">
              {{ row.result === 'approved' ? '已通过' : '已驳回' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comment" label="审批意见" min-width="160" show-overflow-tooltip />
        <el-table-column prop="endTime" label="完成时间" width="170" />
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" @click="handleViewDetail(row)">详情</el-button>
            <el-button type="success" link size="small" :icon="Search" @click="handleViewDiagram(row)">流程图</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无已办任务" />
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

    <!-- 详情对话框 - 审批历史 -->
    <el-dialog v-model="detailDialogVisible" title="任务详情" width="750px" destroy-on-close>
      <!-- 基本属性 -->
      <el-descriptions :column="2" border style="margin-bottom: 16px">
        <el-descriptions-item label="流程名称">{{ detailData.processName }}</el-descriptions-item>
        <el-descriptions-item label="任务节点">{{ detailData.taskName }}</el-descriptions-item>
        <el-descriptions-item label="业务标识">{{ detailData.businessKey }}</el-descriptions-item>
        <el-descriptions-item label="审批结果">
          <el-tag :type="detailData.result === 'approved' ? 'success' : 'danger'" size="small">
            {{ detailData.result === 'approved' ? '已通过' : '已驳回' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2">{{ detailData.comment || '无' }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ detailData.endTime }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ detailData.duration }}</el-descriptions-item>
      </el-descriptions>

      <!-- 审批历史 -->
      <h4 style="margin-bottom: 12px">审批历史</h4>
      <el-timeline>
        <el-timeline-item
          v-for="item in approvalHistory"
          :key="item.id"
          :timestamp="item.createTime"
          :type="item.result === 'approved' ? 'success' : item.result === 'rejected' ? 'danger' : 'primary'"
          placement="top"
        >
          <el-card shadow="never" size="small">
            <p><strong>审批节点：</strong>{{ item.taskName }}</p>
            <p><strong>审批人：</strong>{{ item.assignee }}</p>
            <p><strong>审批结果：</strong>
              <el-tag :type="item.result === 'approved' ? 'success' : 'danger'" size="small">
                {{ item.result === 'approved' ? '通过' : '驳回' }}
              </el-tag>
            </p>
            <p v-if="item.comment"><strong>审批意见：</strong>{{ item.comment }}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item v-if="approvalHistory.length === 0" timestamp="">
          <el-empty description="暂无审批历史" :image-size="40" />
        </el-timeline-item>
      </el-timeline>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 流程图对话框 -->
    <el-dialog v-model="diagramDialogVisible" title="流程图" width="900px" destroy-on-close>
      <div class="diagram-container">
        <el-empty v-if="!diagramUrl" description="暂无流程图数据" />
        <img v-else :src="diagramUrl" alt="流程图" style="max-width: 100%; height: auto" />
      </div>
      <template #footer>
        <el-button @click="diagramDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, View } from '@element-plus/icons-vue'
// import { myDone } from '@/api/modules/workflow'

const searchForm = reactive({
  processName: '',
  businessKey: '',
  result: '',
  dateRange: []
})

const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

const loading = ref(false)
const tableData = ref([])

// 详情对话框
const detailDialogVisible = ref(false)
const detailData = reactive({
  processName: '',
  taskName: '',
  businessKey: '',
  result: '',
  comment: '',
  endTime: '',
  duration: ''
})
const approvalHistory = ref([])

// 流程图对话框
const diagramDialogVisible = ref(false)
const diagramUrl = ref('')

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
    // const res = await myDone(params)
    // tableData.value = res.data.records || res.data || []
    // pagination.total = res.data.total || 0
    tableData.value = []
    pagination.total = 0
  } catch (error) {
    ElMessage.error('加载已办任务失败')
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
    result: '',
    dateRange: []
  })
  handleSearch()
}

// 查看详情
function handleViewDetail(row) {
  Object.assign(detailData, {
    processName: row.processName || '',
    taskName: row.taskName || '',
    businessKey: row.businessKey || '',
    result: row.result || '',
    comment: row.comment || '',
    endTime: row.endTime || '',
    duration: row.duration || ''
  })
  // 加载审批历史
  approvalHistory.value = row.approvalHistory || row.historyList || []
  detailDialogVisible.value = true
}

// 查看流程图
function handleViewDiagram(row) {
  diagramUrl.value = row.diagramUrl || row.processDiagramUrl || ''
  diagramDialogVisible.value = true
  if (!diagramUrl.value) {
    // 如果有 processInstanceId，可以动态加载流程图
    // try {
    //   const res = await getDiagram(row.processInstanceId)
    //   diagramUrl.value = res.data
    // } catch (error) {
    //   ElMessage.error('加载流程图失败')
    // }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.my-done-container {
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

.diagram-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}
</style>
