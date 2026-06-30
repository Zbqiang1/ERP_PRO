<template>
  <div class="user-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="searchForm.realName" placeholder="请输入真实姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮栏 -->
    <el-card class="table-card" shadow="never">
      <div class="action-bar">
        <div class="action-left">
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增用户</el-button>
          <el-button type="danger" :icon="Delete" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
          <el-button :icon="Download" @click="handleExport">导出</el-button>
        </div>
      </div>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="realName" label="真实姓名" min-width="100" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="160" />
        <el-table-column prop="deptName" label="所属部门" min-width="140" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
            <el-button type="warning" link size="small" :icon="Key" @click="handleResetPassword(row)">重置密码</el-button>
            <el-button type="success" link size="small" :icon="UserFilled" @click="handleAssignRoles(row)">分配角色</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无数据" />
        </template>
      </el-table>

      <!-- 分页 -->
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

    <!-- 新增/编辑对话框 -->
    <FormDialog
      v-model:visible="dialogVisible"
      :title="dialogTitle"
      :width="600"
      @confirm="handleDialogConfirm"
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="formData.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId">
          <el-tree-select
            v-model="formData.deptId"
            :data="deptTree"
            :props="{ value: 'id', label: 'deptName', children: 'children' }"
            placeholder="请选择部门"
            check-strictly
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="formData.roleIds" multiple placeholder="请选择角色" style="width: 100%">
            <el-option
              v-for="role in roleList"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </FormDialog>

    <!-- 重置密码对话框 -->
    <el-dialog v-model="passwordDialogVisible" title="重置密码" width="450px" destroy-on-close>
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordFormRules" label-width="100px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="passwordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePasswordConfirm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配角色对话框 -->
    <el-dialog v-model="roleDialogVisible" title="分配角色" width="600px" destroy-on-close>
      <el-transfer
        v-model="selectedRoleIds"
        :data="roleTransferData"
        :titles="['未分配角色', '已分配角色']"
        filterable
        filter-placeholder="搜索角色"
      />
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRoleAssignConfirm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Download, Edit, Key, UserFilled } from '@element-plus/icons-vue'
import { pageUser, getUserById, addUser, updateUser, deleteUser, resetPassword, assignRoles } from '@/api/modules/user'
import { getDeptTree } from '@/api/modules/department'
import { listAllEnabled } from '@/api/modules/role'
import FormDialog from '@/components/common/FormDialog.vue'

// 搜索表单
const searchForm = reactive({
  username: '',
  realName: '',
  phone: '',
  status: null
})

// 分页
const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

// 表格
const loading = ref(false)
const tableData = ref([])
const selectedIds = ref([])

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const isEdit = ref(false)
const formRef = ref(null)
const editId = ref(null)

// 表单数据
const formData = reactive({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  deptId: '',
  status: 1,
  roleIds: []
})


// 表单校验规则
const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
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

// 部门树
const deptTree = ref([])

// 角色列表
const roleList = ref([])

// 重置密码
const passwordDialogVisible = ref(false)
const passwordFormRef = ref(null)
const resetPasswordUserId = ref(null)
const passwordForm = reactive({
  newPassword: '',
  confirmPassword: ''
})
const passwordFormRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 分配角色
const roleDialogVisible = ref(false)
const assignRoleUserId = ref(null)
const selectedRoleIds = ref([])
const roleTransferData = ref([])

// 加载列表
async function loadData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null || params[key] === undefined) {
        delete params[key]
      }
    })
    const res = await pageUser(params)
    tableData.value = res.data.records || res.data || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 加载部门树
async function loadDeptTree() {
  try {
    const res = await getDeptTree()
    deptTree.value = res.data || []
  } catch (error) {
    console.error('加载部门树失败', error)
  }
}

// 加载角色列表
async function loadRoleList() {
  try {
    const res = await listAllEnabled()
    roleList.value = res.data || []
  } catch (error) {
    console.error('加载角色列表失败', error)
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  loadData()
}

// 重置
function handleReset() {
  Object.assign(searchForm, {
    username: '',
    realName: '',
    phone: '',
    status: null
  })
  handleSearch()
}

// 新增
function handleAdd() {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  editId.value = null
  resetForm()
  dialogVisible.value = true
}

// 编辑
async function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  editId.value = row.id
  dialogVisible.value = true
  try {
    const res = await getUserById(row.id)
    const data = res.data
    Object.assign(formData, {
      username: data.username || '',
      password: '',
      realName: data.realName || '',
      phone: data.phone || '',
      email: data.email || '',
      deptId: data.deptId || '',
      status: data.status ?? 1,
      roleIds: data.roleIds || []
    })
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 删除
function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除用户"${row.username}"吗？删除后不可恢复。`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 批量删除
function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个用户吗？删除后不可恢复。`, '批量删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await Promise.all(selectedIds.value.map(id => deleteUser(id)))
      ElMessage.success('批量删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

// 导出
function handleExport() {
  ElMessage.info('导出功能开发中')
}

// 重置密码
function handleResetPassword(row) {
  resetPasswordUserId.value = row.id
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

// 确认重置密码
async function handlePasswordConfirm() {
  const valid = await passwordFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    await resetPassword(resetPasswordUserId.value, { newPassword: passwordForm.newPassword })
    ElMessage.success('密码重置成功')
    passwordDialogVisible.value = false
  } catch (error) {
    ElMessage.error('密码重置失败')
  }
}

// 分配角色
function handleAssignRoles(row) {
  assignRoleUserId.value = row.id
  roleTransferData.value = roleList.value.map(role => ({
    key: role.id,
    label: role.roleName
  }))

  // 获取用户已有的角色
  selectedRoleIds.value = []
  try {
    getUserById(row.id).then(res => {
      selectedRoleIds.value = (res.data.roleIds || [])
    })
  } catch (error) {
    // ignore
  }
  roleDialogVisible.value = true
}

// 确认角色分配
async function handleRoleAssignConfirm() {
  try {
    await assignRoles(assignRoleUserId.value, selectedRoleIds.value)
    ElMessage.success('角色分配成功')
    roleDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('角色分配失败')
  }
}

// 对话框确认
async function handleDialogConfirm() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) {
      await updateUser({ ...formData, id: editId.value })
      ElMessage.success('更新成功')
    } else {
      await addUser(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '新增失败')
  }
}

// 对话框关闭
function handleDialogClosed() {
  resetForm()
}

// 重置表单
function resetForm() {
  Object.assign(formData, {
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    deptId: '',
    status: 1,
    roleIds: []
  })
  formRef.value?.resetFields()
}

// 表格选择变化
function handleSelectionChange(selection) {
  selectedIds.value = selection.map(item => item.id)
}

onMounted(() => {
  loadData()
  loadDeptTree()
  loadRoleList()
})
</script>

<style scoped>
.user-list-container {
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
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.action-left {
  display: flex;
  gap: 8px;
}

.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
