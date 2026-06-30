<template>
  <div class="my-tasks-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="流程名称">
          <el-input v-model="searchForm.processName" placeholder="请输入流程名称" clearable />
        </el-form-item>
        <el-form-item label="业务标识">
          <el-input v-model="searchForm.businessKey" placeholder="请输入业务标识" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格卡片 -->
    <el-card class="table-card" shadow="never">
      <!-- 统计摘要 -->
      <el-row :gutter="16" class="summary-row">
        <el-col :span="6">
          <el-statistic title="待办任务" :value="pagination.total" />
        </el-col>
      </el-row>

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
        <el-table-column prop="startUserId" label="发起人" width="100" />
        <el-table-column prop="taskName" label="当前节点" min-width="140" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column prop="assignee" label="当前处理人" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.assignee" size="small">{{ row.assignee }}</el-tag>
            <el-tag v-else type="info" size="small">未签收</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="!row.assignee"
              type="primary"
              link
              size="small"
              :icon="Finished"
              @click="handleClaim(row)"
            >
              签收
            </el-button>
            <el-button
              v-else
              type="success"
              link
              size="small"
              :icon="Select"
              @click="handleComplete(row)"
            >
              办理
            </el-button>
            <el-button
              type="warning"
              link
              size="small"
              :icon="Share"
              @click="handleDelegate(row)"
            >
              委派
            </el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无待办任务" />
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

    <!-- 办理审批对话框 -->
    <el-dialog v-model="completeDialogVisible" title="办理任务" width="600px" destroy-on-close @closed="handleCompleteClosed">
      <el-descriptions :column="2" border style="margin-bottom: 16px">
        <el-descriptions-item label="流程名称">{{ completingTask.processName }}</el-descriptions-item>
        <el-descriptions-item label="任务节点">{{ completingTask.taskName }}</el-descriptions-item>
        <el-descriptions-item label="业务标识">{{ completingTask.businessKey }}</el-descriptions-item>
        <el-descriptions-item label="发起人">{{ completingTask.startUserId }}</el-descriptions-item>
      </el-descriptions>

      <el-form ref="completeFormRef" :model="completeForm" :rules="completeFormRules" label-width="100px">
        <el-form-item label="审批意见" prop="comment">
          <el-input
            v-model="completeForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="handleReject">驳回</el-button>
        <el-button type="success" @click="handleApprove">同意</el-button>
      </template>
    </el-dialog>

    <!-- 委派对话框 -->
    <el-dialog v-model="delegateDialogVisible" title="委派任务" width="500px" destroy-on-close>
      <el-form ref="delegateFormRef" :model="delegateForm" :rules="delegateFormRules" label-width="100px">
        <el-form-item label="委派人" prop="delegateUserId">
          <el-select v-model="delegateForm.delegateUserId" filterable placeholder="请选择委派人" style="width: 100%">
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="`${user.realName}(${user.username})`"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="委派说明" prop="comment">
          <el-input
            v-model="delegateForm.comment"
            type="textarea"
            :rows="3"
            placeholder="请输入委派说明"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="delegateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleDelegateConfirm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Finished, Select, Share } from '@element-plus/icons-vue'
// import { myTasks, claim, complete, delegate } from '@/api/modules/workflow'
// import { pageUser } from '@/api/modules/user'

const searchForm = reactive({
  processName: '',
  businessKey: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

const loading = ref(false)
const tableData = ref([])

// 办理对话框
const completeDialogVisible = ref(false)
const completeFormRef = ref(null)
const completingTask = reactive({
  taskId: '',
  processName: '',
  taskName: '',
  businessKey: '',
  startUserId: ''
})
const completeForm = reactive({
  comment: ''
})
const completeFormRules = {
  // 审批意见可选
}

// 委派对话框
const delegateDialogVisible = ref(false)
const delegateFormRef = ref(null)
const delegatingTaskId = ref('')
const userList = ref([])
const delegateForm = reactive({
  delegateUserId: '',
  comment: ''
})
const delegateFormRules = {
  delegateUserId: [
    { required: true, message: '请选择委派人', trigger: 'change' }
  ]
}

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
    // const res = await myTasks(params)
    // tableData.value = res.data.records || res.data || []
    // pagination.total = res.data.total || 0
    tableData.value = []
    pagination.total = 0
  } catch (error) {
    ElMessage.error('加载待办任务失败')
  } finally {
    loading.value = false
  }
}

async function loadUsers() {
  try {
    // const res = await pageUser({ page: 1, pageSize: 999 })
    // userList.value = res.data.records || res.data || []
    userList.value = []
  } catch (error) {
    console.error('加载用户列表失败', error)
  }
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

function handleReset() {
  searchForm.processName = ''
  searchForm.businessKey = ''
  handleSearch()
}

// 签收任务
async function handleClaim(row) {
  try {
    // await claim({ taskId: row.taskId })
    ElMessage.success('任务签收成功')
    loadData()
  } catch (error) {
    ElMessage.error('任务签收失败')
  }
}

// 办理任务
function handleComplete(row) {
  Object.assign(completingTask, {
    taskId: row.taskId,
    processName: row.processName || '',
    taskName: row.taskName || '',
    businessKey: row.businessKey || '',
    startUserId: row.startUserId || ''
  })
  completeForm.comment = ''
  completeDialogVisible.value = true
}

// 同意
async function handleApprove() {
  try {
    // await complete({
    //   taskId: completingTask.taskId,
    //   approved: true,
    //   comment: completeForm.comment
    // })
    ElMessage.success('任务审批通过')
    completeDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 驳回
async function handleReject() {
  try {
    // await complete({
    //   taskId: completingTask.taskId,
    //   approved: false,
    //   comment: completeForm.comment
    // })
    ElMessage.success('任务已驳回')
    completeDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

function handleCompleteClosed() {
  completeForm.comment = ''
}

// 委派任务
function handleDelegate(row) {
  delegatingTaskId.value = row.taskId
  delegateForm.delegateUserId = ''
  delegateForm.comment = ''
  delegateDialogVisible.value = true
  if (userList.value.length === 0) {
    loadUsers()
  }
}

async function handleDelegateConfirm() {
  const valid = await delegateFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    // await delegate({
    //   taskId: delegatingTaskId.value,
    //   delegateUserId: delegateForm.delegateUserId,
    //   comment: delegateForm.comment
    // })
    ElMessage.success('委派成功')
    delegateDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('委派失败')
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.my-tasks-container {
  padding: 16px;
}

.search-card {
  margin-bottom: 16px;
}

.table-card {
  margin-bottom: 16px;
}

.summary-row {
  margin-bottom: 16px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
