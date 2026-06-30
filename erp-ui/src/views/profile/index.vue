<template>
  <div class="profile-container">
    <el-card class="avatar-card" shadow="never">
      <div class="user-info-header">
        <el-avatar :size="80" :icon="UserFilled" />
        <div class="user-info-text">
          <h2>{{ userInfo.realName || userInfo.username }}</h2>
          <p>{{ userInfo.deptName || '未分配部门' }} | {{ userInfo.phone || '未填写手机号' }}</p>
        </div>
      </div>
    </el-card>

    <el-card class="content-card" shadow="never">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="basicForm" :rules="basicFormRules" ref="basicFormRef" label-width="100px" style="max-width: 600px">
            <el-form-item label="用户名">
              <el-input v-model="basicForm.username" disabled />
            </el-form-item>
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="basicForm.realName" placeholder="请输入真实姓名" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="basicForm.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="basicForm.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="所属部门">
              <el-input v-model="basicForm.deptName" disabled />
            </el-form-item>
            <el-form-item label="创建时间">
              <el-input v-model="basicForm.createTime" disabled />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="basicSaving" @click="handleSaveBasic">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form :model="passwordForm" :rules="passwordFormRules" ref="passwordFormRef" label-width="100px" style="max-width: 500px">
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="passwordSaving" @click="handleChangePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 操作日志 -->
        <el-tab-pane label="操作日志" name="log">
          <el-form :model="logSearchForm" inline @submit.prevent>
            <el-form-item label="操作模块">
              <el-input v-model="logSearchForm.module" placeholder="请输入模块名称" clearable />
            </el-form-item>
            <el-form-item label="操作类型">
              <el-select v-model="logSearchForm.operation" placeholder="请选择" clearable>
                <el-option label="新增" value="新增" />
                <el-option label="修改" value="修改" />
                <el-option label="删除" value="删除" />
                <el-option label="查询" value="查询" />
                <el-option label="导出" value="导出" />
                <el-option label="登录" value="登录" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleLogSearch">搜索</el-button>
              <el-button :icon="Refresh" @click="handleLogReset">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table v-loading="logLoading" :data="logTableData" border stripe>
            <el-table-column prop="module" label="操作模块" width="140" />
            <el-table-column prop="operation" label="操作类型" width="100" />
            <el-table-column prop="method" label="操作方法" min-width="180" show-overflow-tooltip />
            <el-table-column prop="requestUrl" label="请求地址" min-width="200" show-overflow-tooltip />
            <el-table-column prop="requestMethod" label="请求方式" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="getMethodTag(row.requestMethod)" size="small">{{ row.requestMethod }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="ip" label="IP地址" width="140" />
            <el-table-column prop="duration" label="耗时(ms)" width="90" align="center" />
            <el-table-column prop="createTime" label="操作时间" width="170" />
            <template #empty>
              <el-empty description="暂无操作日志" />
            </template>
          </el-table>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="logPagination.page"
              v-model:page-size="logPagination.pageSize"
              :total="logPagination.total"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              @size-change="loadLogData"
              @current-change="loadLogData"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, UserFilled } from '@element-plus/icons-vue'
import { getUserById, updateUser } from '@/api/modules/user'
import { pageLog } from '@/api/modules/operationLog'

const activeTab = ref('basic')

// ==================== 基本信息 ====================
const basicFormRef = ref(null)
const basicSaving = ref(false)
const userInfo = reactive({
  id: null,
  username: '',
  realName: '',
  phone: '',
  email: '',
  deptName: '',
  createTime: ''
})

const basicForm = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  deptName: '',
  createTime: ''
})

const basicFormRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// ==================== 修改密码 ====================
const passwordFormRef = ref(null)
const passwordSaving = ref(false)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordFormRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// ==================== 操作日志 ====================
const logLoading = ref(false)
const logTableData = ref([])
const logPagination = reactive({ page: 1, pageSize: 20, total: 0 })
const logSearchForm = reactive({
  module: '',
  operation: ''
})

function getMethodTag(method) {
  const map = {
    GET: 'success',
    POST: 'primary',
    PUT: 'warning',
    DELETE: 'danger',
    PATCH: 'info'
  }
  return map[method?.toUpperCase()] || 'info'
}

// ==================== 加载用户信息 ====================
async function loadUserInfo() {
  try {
    // 从 store 或 localStorage 获取当前用户 ID
    const userId = String(JSON.parse(localStorage.getItem('userInfo') || '{}').id || '')
    if (!userId) {
      ElMessage.warning('未获取到用户信息')
      return
    }
    const res = await getUserById(userId)
    const data = res.data
    Object.assign(userInfo, {
      id: data.id,
      username: data.username || '',
      realName: data.realName || '',
      phone: data.phone || '',
      email: data.email || '',
      deptName: data.deptName || '',
      createTime: data.createTime || ''
    })
    Object.assign(basicForm, {
      username: data.username || '',
      realName: data.realName || '',
      phone: data.phone || '',
      email: data.email || '',
      deptName: data.deptName || '',
      createTime: data.createTime || ''
    })
  } catch (error) {
    ElMessage.error('加载用户信息失败')
  }
}

async function handleSaveBasic() {
  const valid = await basicFormRef.value.validate().catch(() => false)
  if (!valid) return
  basicSaving.value = true
  try {
    await updateUser({
      id: userInfo.id,
      realName: basicForm.realName,
      phone: basicForm.phone,
      email: basicForm.email
    })
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    basicSaving.value = false
  }
}

async function handleChangePassword() {
  const valid = await passwordFormRef.value.validate().catch(() => false)
  if (!valid) return
  passwordSaving.value = true
  try {
    // 假设有修改密码的 API
    await updateUser({
      id: userInfo.id,
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    ElMessage.success('密码修改成功，请重新登录')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    ElMessage.error('密码修改失败')
  } finally {
    passwordSaving.value = false
  }
}

// ==================== 操作日志 ====================
async function loadLogData() {
  logLoading.value = true
  try {
    const params = {
      page: logPagination.page,
      pageSize: logPagination.pageSize,
      username: userInfo.username,
      module: logSearchForm.module,
      operation: logSearchForm.operation
    }
    Object.keys(params).forEach(key => {
      if (params[key] === '') delete params[key]
    })
    const res = await pageLog(params)
    logTableData.value = res.data.records || res.data || []
    logPagination.total = res.data.total || 0
  } catch (error) {
    console.error('加载操作日志失败', error)
  } finally {
    logLoading.value = false
  }
}

function handleLogSearch() {
  logPagination.page = 1
  loadLogData()
}

function handleLogReset() {
  logSearchForm.module = ''
  logSearchForm.operation = ''
  handleLogSearch()
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 16px;
}

.avatar-card {
  margin-bottom: 16px;
}

.user-info-header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info-text h2 {
  margin: 0 0 8px 0;
  font-size: 22px;
}

.user-info-text p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.content-card {
  min-height: 500px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
