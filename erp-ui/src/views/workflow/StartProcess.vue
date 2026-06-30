<template>
  <div class="start-process-container">
    <el-card class="form-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span>发起新流程</span>
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        style="max-width: 600px"
      >
        <el-form-item label="选择流程" prop="processDefinitionId">
          <el-select
            v-model="formData.processDefinitionId"
            placeholder="请选择流程定义"
            filterable
            style="width: 100%"
            @change="handleProcessChange"
          >
            <el-option
              v-for="item in processList"
              :key="item.processId"
              :label="`${item.name} (v${item.version})`"
              :value="item.processId"
            >
              <span>{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">v{{ item.version }}</span>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="业务标识" prop="businessKey">
          <el-input v-model="formData.businessKey" placeholder="请输入业务标识（如：leave-2024001）">
            <template #prepend>
              <el-icon><Key /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="业务标题" prop="businessTitle">
          <el-input v-model="formData.businessTitle" placeholder="请输入业务标题" maxlength="100" show-word-limit />
        </el-form-item>

        <!-- 流程变量（动态表单） -->
        <template v-if="processVariables.length > 0">
          <el-divider content-position="left">流程变量</el-divider>
          <el-form-item
            v-for="variable in processVariables"
            :key="variable.key"
            :label="variable.label"
            :prop="'variables.' + variable.key"
            :rules="variable.required ? [{ required: true, message: `请输入${variable.label}`, trigger: 'blur' }] : []"
          >
            <el-input
              v-if="variable.type === 'string' || !variable.type"
              v-model="formData.variables[variable.key]"
              :placeholder="`请输入${variable.label}`"
            />
            <el-input-number
              v-else-if="variable.type === 'number'"
              v-model="formData.variables[variable.key]"
              :placeholder="`请输入${variable.label}`"
              style="width: 100%"
            />
            <el-date-picker
              v-else-if="variable.type === 'date'"
              v-model="formData.variables[variable.key]"
              type="date"
              :placeholder="`请选择${variable.label}`"
              style="width: 100%"
            />
          </el-form-item>
        </template>

        <el-form-item>
          <el-button type="primary" :icon="Promotion" :loading="submitting" @click="handleSubmit">
            发起流程
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 当前流程描述 -->
    <el-card v-if="selectedProcess" class="info-card" shadow="never">
      <template #header>
        <span>流程信息</span>
      </template>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="流程名称">{{ selectedProcess.name }}</el-descriptions-item>
        <el-descriptions-item label="流程标识">{{ selectedProcess.key }}</el-descriptions-item>
        <el-descriptions-item label="版本号">v{{ selectedProcess.version }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="selectedProcess.status === 1 ? 'success' : 'warning'" size="small">
            {{ selectedProcess.status === 1 ? '已发布' : '已挂起' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ selectedProcess.description || '无' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 发起成功对话框 -->
    <el-dialog v-model="successDialogVisible" title="流程发起成功" width="500px" :close-on-click-modal="false">
      <el-result icon="success" title="流程已成功发起" :sub-title="`流程实例ID: ${successInstanceId}`">
        <template #extra>
          <el-button type="primary" @click="successDialogVisible = false">继续发起</el-button>
          <el-button @click="handleViewInstance">查看实例</el-button>
        </template>
      </el-result>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Key, Promotion } from '@element-plus/icons-vue'
// import { list as listDefinitions, startProcess } from '@/api/modules/workflow'

const router = useRouter()

const formRef = ref(null)
const submitting = ref(false)
const processList = ref([])
const selectedProcess = ref(null)
const processVariables = ref([])

const formData = reactive({
  processDefinitionId: '',
  businessKey: '',
  businessTitle: '',
  variables: {}
})

const formRules = {
  processDefinitionId: [
    { required: true, message: '请选择流程定义', trigger: 'change' }
  ],
  businessKey: [
    { required: true, message: '请输入业务标识', trigger: 'blur' }
  ]
}

// 成功对话框
const successDialogVisible = ref(false)
const successInstanceId = ref('')

async function loadProcessList() {
  try {
    // const res = await listDefinitions({ page: 1, pageSize: 999 })
    // processList.value = (res.data.records || res.data || []).filter(p => p.status === 1)
    processList.value = []
  } catch (error) {
    ElMessage.error('加载流程定义列表失败')
  }
}

function handleProcessChange(processId) {
  selectedProcess.value = processList.value.find(p => p.processId === processId) || null
  processVariables.value = selectedProcess.value?.variables || []
  formData.variables = {}
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    // const res = await startProcess({
    //   processDefinitionId: formData.processDefinitionId,
    //   businessKey: formData.businessKey,
    //   businessTitle: formData.businessTitle,
    //   variables: formData.variables
    // })
    // successInstanceId.value = res.data?.processInstanceId || res.data || ''
    successInstanceId.value = 'demo-instance-id'
    ElMessage.success('流程发起成功')
    successDialogVisible.value = true
  } catch (error) {
    ElMessage.error('流程发起失败')
  } finally {
    submitting.value = false
  }
}

function handleReset() {
  formData.processDefinitionId = ''
  formData.businessKey = ''
  formData.businessTitle = ''
  formData.variables = {}
  selectedProcess.value = null
  processVariables.value = []
  formRef.value?.resetFields()
}

function handleViewInstance() {
  successDialogVisible.value = false
  router.push('/workflow/instance')
}

onMounted(() => {
  loadProcessList()
})
</script>

<style scoped>
.start-process-container {
  padding: 16px;
}

.form-card {
  margin-bottom: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-card {
  min-height: 200px;
}
</style>
