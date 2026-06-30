<template>
  <div class="menu-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent>
        <el-form-item label="菜单名称">
          <el-input v-model="searchForm.menuName" placeholder="请输入菜单名称" clearable />
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
        <el-button type="primary" :icon="Plus" @click="handleAdd(null)">新增根菜单</el-button>
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
        <el-table-column prop="menuName" label="菜单名称" min-width="200" />
        <el-table-column prop="icon" label="图标" width="80" align="center">
          <template #default="{ row }">
            <el-icon v-if="row.icon" :size="18">
              <component :is="row.icon" />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="menuType" label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.menuType === 1" size="small">目录</el-tag>
            <el-tag v-else-if="row.menuType === 2" type="success" size="small">菜单</el-tag>
            <el-tag v-else-if="row.menuType === 3" type="warning" size="small">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" min-width="140" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="160" show-overflow-tooltip />
        <el-table-column prop="perms" label="权限标识" min-width="160" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="70" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.menuType !== 3" type="success" link size="small" :icon="Plus" @click="handleAdd(row)">新增子级</el-button>
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
      :width="600"
      @confirm="handleDialogConfirm"
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="menuTreeSelectData"
            :props="{ value: 'id', label: 'menuName', children: 'children' }"
            placeholder="请选择上级菜单（留空为顶级）"
            check-strictly
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="formData.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="formData.menuType">
            <el-radio :value="1">目录</el-radio>
            <el-radio :value="2">菜单</el-radio>
            <el-radio :value="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="formData.menuType !== 3" label="路由路径" prop="path">
          <el-input v-model="formData.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item v-if="formData.menuType === 2" label="组件路径">
          <el-input v-model="formData.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item v-if="formData.menuType !== 3" label="图标">
          <el-input v-model="formData.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item v-if="formData.menuType === 3" label="权限标识">
          <el-input v-model="formData.perms" placeholder="例如: system:user:add" />
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
import { pageMenu, getMenuById, addMenu, updateMenu, deleteMenu, getMenuTree } from '@/api/modules/menu'
import FormDialog from '@/components/common/FormDialog.vue'

const searchForm = reactive({
  menuName: ''
})

const loading = ref(false)
const tableData = ref([])
const tableRef = ref(null)
const expandAll = ref(true)

const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const isEdit = ref(false)
const formRef = ref(null)
const editId = ref(null)
const parentMenuId = ref(null)

const menuTreeSelectData = ref([])

const formData = reactive({
  parentId: null,
  menuName: '',
  menuType: 2,
  path: '',
  component: '',
  icon: '',
  perms: '',
  sortOrder: 0,
  status: 1
})

const formRules = {
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  menuType: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
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
    const res = await getMenuTree()
    const rawData = res.data || []
    if (searchForm.menuName) {
      tableData.value = filterTree(rawData, searchForm.menuName)
    } else {
      tableData.value = rawData
    }
  } catch (error) {
    ElMessage.error('加载菜单列表失败')
  } finally {
    loading.value = false
  }
}

function filterTree(tree, keyword) {
  return tree.reduce((acc, node) => {
    const children = node.children ? filterTree(node.children, keyword) : []
    if (node.menuName && node.menuName.includes(keyword) || children.length > 0) {
      acc.push({ ...node, children })
    }
    return acc
  }, [])
}

async function loadMenuTreeSelect() {
  try {
    const res = await getMenuTree()
    // 只返回目录和菜单，不包含按钮
    menuTreeSelectData.value = filterMenuType(res.data || [])
  } catch (error) {
    console.error('加载菜单树失败', error)
  }
}

function filterMenuType(tree) {
  return tree
    .filter(node => node.menuType !== 3)
    .map(node => ({
      ...node,
      children: node.children ? filterMenuType(node.children) : []
    }))
}

function handleSearch() {
  loadData()
}

function handleReset() {
  searchForm.menuName = ''
  loadData()
}

function handleAdd(parent) {
  isEdit.value = false
  dialogTitle.value = parent ? `新增子级菜单` : '新增根菜单'
  editId.value = null
  parentMenuId.value = parent ? parent.id : null
  resetForm()
  formData.parentId = parent ? parent.id : null
  dialogVisible.value = true
}

async function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑菜单'
  editId.value = row.id
  dialogVisible.value = true
  try {
    const res = await getMenuById(row.id)
    const data = res.data
    Object.assign(formData, {
      parentId: data.parentId || null,
      menuName: data.menuName || '',
      menuType: data.menuType || 2,
      path: data.path || '',
      component: data.component || '',
      icon: data.icon || '',
      perms: data.perms || '',
      sortOrder: data.sortOrder || 0,
      status: data.status ?? 1
    })
  } catch (error) {
    ElMessage.error('获取菜单信息失败')
  }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除菜单"${row.menuName}"吗？如果有子菜单将一并删除。`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('删除成功')
      loadData()
      loadMenuTreeSelect()
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
      await updateMenu({ ...formData, id: editId.value })
      ElMessage.success('更新成功')
    } else {
      await addMenu(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
    loadMenuTreeSelect()
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
    menuName: '',
    menuType: 2,
    path: '',
    component: '',
    icon: '',
    perms: '',
    sortOrder: 0,
    status: 1
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadData()
  loadMenuTreeSelect()
})
</script>

<style scoped>
.menu-list-container {
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
