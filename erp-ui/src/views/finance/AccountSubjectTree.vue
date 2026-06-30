<template>
  <div class="account-subject-tree">
    <el-card shadow="never">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAddRoot">新增一级科目</el-button>
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
        <el-table-column prop="subjectCode" label="科目编码" width="150" />
        <el-table-column prop="subjectName" label="科目名称" min-width="200" />
        <el-table-column prop="subjectType" label="科目类型" width="120">
          <template #default="{ row }">{{ subjectTypeLabel(row.subjectType) }}</template>
        </el-table-column>
        <el-table-column prop="balanceDirection" label="余额方向" width="100">
          <template #default="{ row }">
            <el-tag :type="row.balanceDirection === 'DEBIT' ? 'primary' : 'success'">
              {{ row.balanceDirection === 'DEBIT' ? '借方' : '贷方' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleAddChild(row)">添加子科目</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="删除该科目将同时删除其子科目，确定删除？" @confirm="handleDelete(row)">
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
        <el-form-item label="上级科目">{{ parentSubjectName }}</el-form-item>
        <el-form-item label="科目编码" prop="subjectCode">
          <el-input v-model="form.subjectCode" placeholder="请输入科目编码" :maxlength="50" />
        </el-form-item>
        <el-form-item label="科目名称" prop="subjectName">
          <el-input v-model="form.subjectName" placeholder="请输入科目名称" :maxlength="100" />
        </el-form-item>
        <el-form-item label="科目类型" prop="subjectType">
          <el-select v-model="form.subjectType" placeholder="请选择科目类型" class="w-full">
            <el-option label="资产" value="ASSET" />
            <el-option label="负债" value="LIABILITY" />
            <el-option label="权益" value="EQUITY" />
            <el-option label="收入" value="INCOME" />
            <el-option label="成本" value="COST" />
            <el-option label="费用" value="EXPENSE" />
          </el-select>
        </el-form-item>
        <el-form-item label="余额方向" prop="balanceDirection">
          <el-select v-model="form.balanceDirection" placeholder="请选择余额方向" class="w-full">
            <el-option label="借方" value="DEBIT" />
            <el-option label="贷方" value="CREDIT" />
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
import { accountSubjectTree, accountSubjectAdd, accountSubjectUpdate, accountSubjectDelete, accountSubjectGetById } from '@/api/modules/finance'

const loading = ref(false)
const tableRef = ref(null)
const tableData = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add')
const currentRow = ref(null); const parentSubjectName = ref('-')
const formRef = ref(null)

const form = reactive({
  id: null, parentId: null, subjectCode: '', subjectName: '',
  subjectType: '', balanceDirection: 'DEBIT', sortOrder: 0
})

const rules = {
  subjectCode: [{ required: true, message: '请输入科目编码', trigger: 'blur' }],
  subjectName: [{ required: true, message: '请输入科目名称', trigger: 'blur' }],
  subjectType: [{ required: true, message: '请选择科目类型', trigger: 'change' }],
  balanceDirection: [{ required: true, message: '请选择余额方向', trigger: 'change' }]
}

const subjectTypeMap = { ASSET: '资产', LIABILITY: '负债', EQUITY: '权益', INCOME: '收入', COST: '成本', EXPENSE: '费用' }
const subjectTypeLabel = (t) => subjectTypeMap[t] || t

const fetchData = async () => {
  loading.value = true
  try {
    const res = await accountSubjectTree()
    if (res.code === 200) { tableData.value = res.data || [] }
  } catch { ElMessage.error('获取科目树失败') } finally { loading.value = false }
}

const handleAddRoot = () => {
  dialogMode.value = 'add'; dialogTitle.value = '新增一级科目'
  currentRow.value = null; parentSubjectName.value = '-'
  resetForm()
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  dialogMode.value = 'add'; dialogTitle.value = `添加子科目 - ${row.subjectName}`
  currentRow.value = null; parentSubjectName.value = row.subjectName
  form.parentId = row.id
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑科目'
  try {
    const res = await accountSubjectGetById(row.id)
    if (res.code === 200) {
      const data = res.data
      Object.keys(form).forEach(k => { if (data[k] !== undefined) form[k] = data[k] })
    } else {
      Object.keys(form).forEach(k => { if (row[k] !== undefined) form[k] = row[k] })
    }
  } catch {
    Object.keys(form).forEach(k => { if (row[k] !== undefined) form[k] = row[k] })
  }
  currentRow.value = row
  parentSubjectName.value = row.parentId ? '上级科目' : '-'
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    const res = await accountSubjectDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  const apiMethod = dialogMode.value === 'add' ? accountSubjectAdd : accountSubjectUpdate
  try {
    const res = await apiMethod({ ...form })
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

const resetForm = () => {
  Object.assign(form, { id: null, parentId: null, subjectCode: '', subjectName: '', subjectType: '', balanceDirection: 'DEBIT', sortOrder: 0 })
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

onMounted(() => fetchData())
</script>

<style scoped>
.account-subject-tree { padding: 16px; }
.mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; }
</style>
