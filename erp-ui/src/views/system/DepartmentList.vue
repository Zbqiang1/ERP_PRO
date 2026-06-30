<template>
  <div class="dept-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="部门名称">
          <el-input v-model="searchForm.deptName" placeholder="请输入部门名称" clearable />
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
        <el-button type="primary" :icon="Plus" @click="handleAdd(null)">新增根部门</el-button>
        <el-button :icon="Sort" @click="toggleExpandAll">{{ expandAll ? '折叠全部' : '展开全部' }}</el-button>
      </div>

      <el-table
        ref="tableRef"
        v-loading="loading"
        :data="tableData"
        border
        stripe
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="expandAll"
      >
        <el-table-column prop="deptName" label="部门名称" min-width="200" />
        <el-table-column prop="deptCode" label="部门编码" min-width="140" />
        <el-table-column prop="leader" label="负责人" min-width="120" />
        <el-table-column prop="phone" label="联系电话" min-width="140" />
        <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button type="success" link size="small" :icon="Plus" @click="handleAdd(row)">新增子级</el-button>
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无数据" />
        </template>
      </el-table>
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
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="deptTreeData"
            :props="{ value: 'id', label: 'deptName', children: 'children' }"
            placeholder="请选择上级部门（留空为顶级）"
            check-strictly
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="formData.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" prop="deptCode">
          <el-input v-model="formData.deptCode" placeholder="请输入部门编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="formData.leader" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="formData.sortOrder" :min="0" :max="9999" />
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Edit, Sort } from '@element-plus/icons-vue'
import { pageDepartment, getById, add, update, deleteDept, getDeptTree } from '@/api/modules/department'
import FormDialog from '@/components/common/FormDialog.vue'

const searchForm = reactive({
  deptName: ''
})

const loading = ref(false)
const tableData = ref([])
const tableRef = ref(null)
const expandAll = ref(true)

const deptTreeData = ref([])

const dialogVisible = ref(false)
const dialogTitle = ref('新增部门')
const isEdit = ref(false)
const formRef = ref(null)
const editId = ref(null)

const formData = reactive({
  parentId: null,
  deptName: '',
  deptCode: '',
  leader: '',
  phone: '',
  sortOrder: 0,
  status: 1
})

const formRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  deptCode: [
    { required: true, message: '请输入部门编码', trigger: 'blur' }
  ],
  sortOrder: [
    { required: true, message: '请输入排序号', trigger: 'blur' }
  ]
}

function toggleExpandAll() {
  expandAll.value = !expandAll.value
}

async function loadData() {
  loading.value = true
  try {
    const res = await getDeptTree()
    const rawData = res.data || []
    if (searchForm.deptName) {
      tableData.value = filterTree(rawData, searchForm.deptName)
    } else {
      tableData.value = rawData
    }
  } catch (error) {
    ElMessage.error('加载部门列表失败')
  } finally {
    loading.value = false
  }
}

function filterTree(tree, keyword) {
  return tree.reduce((acc, node) => {
    const children = node.children ? filterTree(node.children, keyword) : []
    if ((node.deptName && node.deptName.includes(keyword)) || children.length > 0) {
      acc.push({ ...node, children })
    }
    return acc
  }, [])
}

async function loadDeptTree() {
  try {
    const res = await getDeptTree()
    deptTreeData.value = res.data || []
  } catch (error) {
    console.error('加载部门树失败', error)
  }
}

function handleSearch() {
  loadData()
}

function handleReset() {
  searchForm.deptName = ''
  loadData()
}

function handleAdd(parent) {
  isEdit.value = false
  dialogTitle.value = parent ? `新增子级部门` : '新增根部门'
  editId.value = null
  resetForm()
  formData.parentId = parent ? parent.id : null
  dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑部门'
  editId.value = row.id
  dialogVisible.value = true
  try {
    const res = await getById(row.id)
    const data = res.data
    Object.assign(formData, {
      parentId: data.parentId || null,
      deptName: data.deptName || '',
      deptCode: data.deptCode || '',
      leader: data.leader || '',
      phone: data.phone || '',
      sortOrder: data.sortOrder || 0,
      status: data.status ?? 1
    })
  } catch (error) {
    ElMessage.error('获取部门信息失败')
  }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除部门"${row.deptName}"吗？如果有子部门将一并删除。`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDept(row.id)
      ElMessage.success('删除成功')
      loadData()
      loadDeptTree()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

async function handleDialogConfirm() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) {
      await update({ ...formData, id: editId.value })
      ElMessage.success('更新成功')
    } else {
      await add(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
    loadDeptTree()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '新增失败')
  }
}

function handleDialogClosed() {
  resetForm()
}

function resetForm() {
  Object.assign(formData, {
    parentId: null,
    deptName: '',
    deptCode: '',
    leader: '',
    phone: '',
    sortOrder: 0,
    status: 1
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadData()
  loadDeptTree()
})
</script>

<style scoped>
.dept-list-container {
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
</style>
