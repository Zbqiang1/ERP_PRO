<template>
  <div class="role-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="searchForm.roleCode" placeholder="请输入角色编码" clearable />
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
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增角色</el-button>
        <el-button type="danger" :icon="Delete" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="roleName" label="角色名称" min-width="140" />
        <el-table-column prop="roleCode" label="角色编码" min-width="140" />
        <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
            <el-button type="success" link size="small" :icon="Menu" @click="handleAssignMenus(row)">分配菜单</el-button>
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

    <!-- 新增/编辑对话框 -->
    <FormDialog
      v-model:visible="dialogVisible"
      :title="dialogTitle"
      :width="550"
      @confirm="handleDialogConfirm"
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="formData.roleCode" placeholder="请输入角色编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="formData.sortOrder" :min="0" :max="9999" placeholder="排序号" />
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
      </el-form>
    </FormDialog>

    <!-- 分配菜单对话框 -->
    <el-dialog v-model="menuDialogVisible" title="分配菜单" width="500px" destroy-on-close>
      <el-tree
        ref="menuTreeRef"
        :data="menuTreeData"
        show-checkbox
        node-key="id"
        :props="{ children: 'children', label: 'menuName' }"
        :default-checked-keys="checkedMenuIds"
        default-expand-all
        check-strictly
        highlight-current
      />
      <template #footer>
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleMenuAssignConfirm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Edit, Menu } from '@element-plus/icons-vue'
import { pageRole, getRoleById, addRole, updateRole, deleteRole, assignMenus } from '@/api/modules/role'
import { getMenuTree } from '@/api/modules/menu'
import FormDialog from '@/components/common/FormDialog.vue'

const searchForm = reactive({
  roleName: '',
  roleCode: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 20,
  total: 0
})

const loading = ref(false)
const tableData = ref([])
const selectedIds = ref([])

const dialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const isEdit = ref(false)
const formRef = ref(null)
const editId = ref(null)

const formData = reactive({
  roleName: '',
  roleCode: '',
  description: '',
  sortOrder: 0,
  status: 1
})

const formRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' }
  ]
}

// 菜单分配
const menuDialogVisible = ref(false)
const menuTreeRef = ref(null)
const menuTreeData = ref([])
const checkedMenuIds = ref([])
const assignRoleId = ref(null)

async function loadData() {
  loading.value = true
  try {
    const params = {
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    Object.keys(params).forEach(key => {
      if (params[key] === '') {
        delete params[key]
      }
    })
    const res = await pageRole(params)
    tableData.value = res.data.records || res.data || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  } finally {
    loading.value = false
  }
}

async function loadMenuTree() {
  try {
    const res = await getMenuTree()
    menuTreeData.value = res.data || []
  } catch (error) {
    console.error('加载菜单树失败', error)
  }
}

function handleSearch() {
  pagination.page = 1
  loadData()
}

function handleReset() {
  searchForm.roleName = ''
  searchForm.roleCode = ''
  handleSearch()
}

function handleAdd() {
  isEdit.value = false
  dialogTitle.value = '新增角色'
  editId.value = null
  resetForm()
  dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑角色'
  editId.value = row.id
  dialogVisible.value = true
  try {
    const res = await getRoleById(row.id)
    const data = res.data
    Object.assign(formData, {
      roleName: data.roleName || '',
      roleCode: data.roleCode || '',
      description: data.description || '',
      sortOrder: data.sortOrder || 0,
      status: data.status ?? 1
    })
  } catch (error) {
    ElMessage.error('获取角色信息失败')
  }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除角色"${row.roleName}"吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

function handleBatchDelete() {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的角色')
    return
  }
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个角色吗？`, '批量删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await Promise.all(selectedIds.value.map(id => deleteRole(id)))
      ElMessage.success('批量删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

async function handleAssignMenus(row) {
  assignRoleId.value = row.id
  try {
    const res = await getRoleById(row.id)
    checkedMenuIds.value = res.data.menuIds || []
  } catch (error) {
    checkedMenuIds.value = []
  }
  menuDialogVisible.value = true
}

async function handleMenuAssignConfirm() {
  const checkedKeys = menuTreeRef.value.getCheckedKeys()
  const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys()
  const allKeys = [...checkedKeys, ...halfCheckedKeys]
  try {
    await assignMenus({ roleId: assignRoleId.value, menuIds: allKeys })
    ElMessage.success('菜单分配成功')
    menuDialogVisible.value = false
  } catch (error) {
    ElMessage.error('菜单分配失败')
  }
}

async function handleDialogConfirm() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) {
      await updateRole({ ...formData, id: editId.value })
      ElMessage.success('更新成功')
    } else {
      await addRole(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '新增失败')
  }
}

function handleDialogClosed() {
  resetForm()
}

function resetForm() {
  Object.assign(formData, {
    roleName: '',
    roleCode: '',
    description: '',
    sortOrder: 0,
    status: 1
  })
  formRef.value?.resetFields()
}

function handleSelectionChange(selection) {
  selectedIds.value = selection.map(item => item.id)
}

onMounted(() => {
  loadData()
  loadMenuTree()
})
</script>

<style scoped>
.role-list-container {
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
