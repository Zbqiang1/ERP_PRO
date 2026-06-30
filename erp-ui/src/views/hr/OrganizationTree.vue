<template>
  <div class="organization-tree">
    <el-card shadow="never">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAddRoot">新增组织</el-button>
        <el-button icon="RefreshRight" @click="fetchData">刷新</el-button>
        <el-button icon="Sort" @click="handleExpandAll">展开全部</el-button>
        <el-button icon="Rank" @click="handleCollapseAll">收起全部</el-button>
      </div>
      <el-table
        ref="tableRef"
        v-loading="loading"
        :data="tableData"
        border
        stripe
        row-key="id"
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        empty-text="暂无数据"
        class="w-full"
      >
        <el-table-column prop="orgCode" label="组织编码" width="130" />
        <el-table-column prop="orgName" label="组织名称" min-width="200" />
        <el-table-column prop="orgType" label="组织类型" width="120">
          <template #default="{ row }">
            <el-tag :type="orgTypeTag(row.orgType)">{{ orgTypeLabel(row.orgType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="leaderName" label="负责人" width="100" />
        <el-table-column prop="sortOrder" label="排序" width="80" align="right" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleAddChild(row)">添加子组织</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="删除该组织将同时删除其子组织，确定删除？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" destroy-on-close @close="handleCancel">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级组织">{{ parentOrgName }}</el-form-item>
        <el-form-item label="组织编码" prop="orgCode">
          <el-input v-model="form.orgCode" placeholder="请输入组织编码" :maxlength="50" />
        </el-form-item>
        <el-form-item label="组织名称" prop="orgName">
          <el-input v-model="form.orgName" placeholder="请输入组织名称" :maxlength="100" />
        </el-form-item>
        <el-form-item label="组织类型" prop="orgType">
          <el-select v-model="form.orgType" placeholder="请选择组织类型" class="w-full">
            <el-option label="公司" value="COMPANY" />
            <el-option label="部门" value="DEPT" />
            <el-option label="小组" value="GROUP" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="leaderId">
          <el-select v-model="form.leaderId" placeholder="请选择负责人" filterable class="w-full">
            <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" class="w-full" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { organizationTree, organizationAdd, organizationUpdate, organizationDelete, organizationGetById } from '@/api/modules/hr'
import { employeeListAll } from '@/api/modules/hr'

const loading = ref(false)
const tableRef = ref(null)
const tableData = ref([])
const employeeOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add')
const currentRow = ref(null); const parentOrgName = ref('-')
const formRef = ref(null)

const form = reactive({
  id: null, parentId: null, orgCode: '', orgName: '',
  orgType: '', leaderId: null, sortOrder: 0
})

const rules = {
  orgCode: [{ required: true, message: '请输入组织编码', trigger: 'blur' }],
  orgName: [{ required: true, message: '请输入组织名称', trigger: 'blur' }],
  orgType: [{ required: true, message: '请选择组织类型', trigger: 'change' }]
}

const orgTypeMap = { COMPANY: '公司', DEPT: '部门', GROUP: '小组' }
const orgTypeTagMap = { COMPANY: 'primary', DEPT: 'success', GROUP: 'warning' }
const orgTypeLabel = (t) => orgTypeMap[t] || t
const orgTypeTag = (t) => orgTypeTagMap[t] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const res = await organizationTree()
    if (res.code === 200) { tableData.value = res.data || [] }
  } catch { ElMessage.error('获取组织树失败') } finally { loading.value = false }
}

const fetchEmployees = async () => {
  try { const res = await employeeListAll(); employeeOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleAddRoot = () => {
  dialogMode.value = 'add'; dialogTitle.value = '新增组织'
  currentRow.value = null; parentOrgName.value = '-'
  resetForm()
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  dialogMode.value = 'add'; dialogTitle.value = `添加子组织 - ${row.orgName}`
  currentRow.value = null; parentOrgName.value = row.orgName
  form.parentId = row.id
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑组织'
  try {
    const res = await organizationGetById(row.id)
    if (res.code === 200) Object.keys(form).forEach(k => { if (res.data[k] !== undefined) form[k] = res.data[k] })
    else Object.keys(form).forEach(k => { if (row[k] !== undefined) form[k] = row[k] })
  } catch { Object.keys(form).forEach(k => { if (row[k] !== undefined) form[k] = row[k] }) }
  currentRow.value = row
  parentOrgName.value = row.parentId ? '上级组织' : '-'
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    const res = await organizationDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  const apiMethod = dialogMode.value === 'add' ? organizationAdd : organizationUpdate
  try {
    const res = await apiMethod({ ...form })
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

const resetForm = () => {
  Object.assign(form, { id: null, parentId: null, orgCode: '', orgName: '', orgType: '', leaderId: null, sortOrder: 0 })
}

const handleCancel = () => { dialogVisible.value = false; resetForm() }

const handleExpandAll = () => {
  const els = tableRef.value?.$el?.querySelectorAll('.el-table__expand-icon')
  els?.forEach(el => el.click())
}
const handleCollapseAll = () => {
  const els = tableRef.value?.$el?.querySelectorAll('.el-table__expand-icon--expanded')
  els?.forEach(el => el.click())
}

onMounted(() => { fetchData(); fetchEmployees() })
</script>

<style scoped>
.organization-tree { padding: 16px; }
.mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; }
</style>
