<template>
  <div class="dict-list-container">
    <el-row :gutter="16">
      <!-- 左侧：字典类型列表 -->
      <el-col :span="8">
        <el-card shadow="never" class="left-card">
          <template #header>
            <div class="card-header">
              <span>字典类型</span>
              <el-button type="primary" size="small" :icon="Plus" @click="handleAddType">新增</el-button>
            </div>
          </template>
          <el-table
            v-loading="typeLoading"
            :data="typeTableData"
            border
            stripe
            highlight-current-row
            @row-click="handleTypeClick"
          >
            <el-table-column prop="dictName" label="字典名称" min-width="120" show-overflow-tooltip />
            <el-table-column prop="dictType" label="字典编码" min-width="120" show-overflow-tooltip />
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link size="small" :icon="Edit" @click.stop="handleEditType(row)">编辑</el-button>
                <el-button type="danger" link size="small" :icon="Delete" @click.stop="handleDeleteType(row)">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <el-empty description="暂无字典类型" />
            </template>
          </el-table>
          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="typePagination.page"
              v-model:page-size="typePagination.pageSize"
              :total="typePagination.total"
              :page-sizes="[10, 20, 50]"
              layout="total, prev, pager, next"
              small
              @size-change="loadTypeData"
              @current-change="loadTypeData"
            />
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：字典条目列表 -->
      <el-col :span="16">
        <el-card shadow="never" class="right-card">
          <template #header>
            <div class="card-header">
              <span>字典条目{{ currentType ? ` - ${currentType.dictName}` : '' }}</span>
              <el-button
                type="primary"
                size="small"
                :icon="Plus"
                :disabled="!currentType"
                @click="handleAddEntry"
              >
                新增条目
              </el-button>
            </div>
          </template>

          <el-form :model="entrySearchForm" inline @submit.prevent>
            <el-form-item label="标签">
              <el-input v-model="entrySearchForm.dictLabel" placeholder="请输入标签" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" @click="handleEntrySearch">搜索</el-button>
              <el-button :icon="Refresh" @click="handleEntryReset">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table v-loading="entryLoading" :data="entryTableData" border stripe>
            <el-table-column prop="dictLabel" label="字典标签" min-width="140" />
            <el-table-column prop="dictValue" label="字典值" min-width="120" />
            <el-table-column prop="sortOrder" label="排序" width="80" align="center" />
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                  {{ row.status === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link size="small" :icon="Edit" @click="handleEditEntry(row)">编辑</el-button>
                <el-button type="danger" link size="small" :icon="Delete" @click="handleDeleteEntry(row)">删除</el-button>
              </template>
            </el-table-column>
            <template #empty>
              <el-empty :description="currentType ? '暂无条目数据' : '请先选择字典类型'" />
            </template>
          </el-table>

          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="entryPagination.page"
              v-model:page-size="entryPagination.pageSize"
              :total="entryPagination.total"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next"
              @size-change="loadEntryData"
              @current-change="loadEntryData"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 字典类型对话框 -->
    <FormDialog
      v-model:visible="typeDialogVisible"
      :title="typeDialogTitle"
      :width="500"
      @confirm="handleTypeDialogConfirm"
      @closed="handleTypeDialogClosed"
    >
      <el-form ref="typeFormRef" :model="typeFormData" :rules="typeFormRules" label-width="100px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="typeFormData.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典编码" prop="dictType">
          <el-input v-model="typeFormData.dictType" placeholder="请输入字典编码" :disabled="isTypeEdit" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="typeFormData.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="typeFormData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
    </FormDialog>

    <!-- 字典条目对话框 -->
    <FormDialog
      v-model:visible="entryDialogVisible"
      :title="entryDialogTitle"
      :width="500"
      @confirm="handleEntryDialogConfirm"
      @closed="handleEntryDialogClosed"
    >
      <el-form ref="entryFormRef" :model="entryFormData" :rules="entryFormRules" label-width="100px">
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="entryFormData.dictLabel" placeholder="请输入字典标签" />
        </el-form-item>
        <el-form-item label="字典值" prop="dictValue">
          <el-input v-model="entryFormData.dictValue" placeholder="请输入字典值" :disabled="isEntryEdit" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="entryFormData.sortOrder" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="entryFormData.status"
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
import { Search, Refresh, Plus, Delete, Edit } from '@element-plus/icons-vue'
import { page as pageDict, getById, add, update, deleteDict, getByDictType } from '@/api/modules/dictionary'
import FormDialog from '@/components/common/FormDialog.vue'

// ==================== 字典类型 ====================
const typeLoading = ref(false)
const typeTableData = ref([])
const typePagination = reactive({ page: 1, pageSize: 20, total: 0 })
const currentType = ref(null)

const typeDialogVisible = ref(false)
const typeDialogTitle = ref('新增字典类型')
const isTypeEdit = ref(false)
const typeFormRef = ref(null)
const typeEditId = ref(null)

const typeFormData = reactive({
  dictName: '',
  dictType: '',
  description: '',
  status: 1
})

const typeFormRules = {
  dictName: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  dictType: [{ required: true, message: '请输入字典编码', trigger: 'blur' }]
}

// ==================== 字典条目 ====================
const entryLoading = ref(false)
const entryTableData = ref([])
const entryPagination = reactive({ page: 1, pageSize: 20, total: 0 })

const entrySearchForm = reactive({
  dictLabel: ''
})
const currentDictType = ref('')

const entryDialogVisible = ref(false)
const entryDialogTitle = ref('新增字典条目')
const isEntryEdit = ref(false)
const entryFormRef = ref(null)
const entryEditId = ref(null)

const entryFormData = reactive({
  dictLabel: '',
  dictValue: '',
  sortOrder: 0,
  status: 1
})

const entryFormRules = {
  dictLabel: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
  dictValue: [{ required: true, message: '请输入字典值', trigger: 'blur' }]
}

// ==================== 类型操作 ====================
async function loadTypeData() {
  typeLoading.value = true
  try {
    const res = await pageDict({ page: typePagination.page, pageSize: typePagination.pageSize })
    typeTableData.value = res.data.records || res.data || []
    typePagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('加载字典类型失败')
  } finally {
    typeLoading.value = false
  }
}

function handleTypeClick(row) {
  currentType.value = row
  currentDictType.value = row.dictType
  entryPagination.page = 1
  loadEntryData()
}

function handleAddType() {
  isTypeEdit.value = false
  typeDialogTitle.value = '新增字典类型'
  typeEditId.value = null
  resetTypeForm()
  typeDialogVisible.value = true
}

async function handleEditType(row) {
  isTypeEdit.value = true
  typeDialogTitle.value = '编辑字典类型'
  typeEditId.value = row.id
  typeDialogVisible.value = true
  try {
    const res = await getById(row.id)
    const data = res.data
    Object.assign(typeFormData, {
      dictName: data.dictName || '',
      dictType: data.dictType || '',
      description: data.description || '',
      status: data.status ?? 1
    })
  } catch (error) {
    ElMessage.error('获取字典类型信息失败')
  }
}

function handleDeleteType(row) {
  ElMessageBox.confirm(`确定要删除字典类型"${row.dictName}"吗？关联的条目也将被删除。`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDict(row.id)
      ElMessage.success('删除成功')
      if (currentType.value?.id === row.id) {
        currentType.value = null
        entryTableData.value = []
      }
      loadTypeData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

async function handleTypeDialogConfirm() {
  const valid = await typeFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isTypeEdit.value) {
      await update({ ...typeFormData, id: typeEditId.value })
      ElMessage.success('更新成功')
    } else {
      await add(typeFormData)
      ElMessage.success('新增成功')
    }
    typeDialogVisible.value = false
    loadTypeData()
  } catch (error) {
    ElMessage.error(isTypeEdit.value ? '更新失败' : '新增失败')
  }
}

function handleTypeDialogClosed() {
  resetTypeForm()
}

function resetTypeForm() {
  Object.assign(typeFormData, { dictName: '', dictType: '', description: '', status: 1 })
  typeFormRef.value?.resetFields()
}

// ==================== 条目操作 ====================
async function loadEntryData() {
  if (!currentType.value) return
  entryLoading.value = true
  try {
    const params = {
      dictType: currentDictType.value,
      page: entryPagination.page,
      pageSize: entryPagination.pageSize
    }
    const res = await getByDictType(currentDictType.value)
    // API返回所有条目，前端分页
    const allData = res.data || []
    entryTableData.value = allData
    entryPagination.total = allData.length
  } catch (error) {
    ElMessage.error('加载字典条目失败')
  } finally {
    entryLoading.value = false
  }
}

function handleEntrySearch() {
  entryPagination.page = 1
  loadEntryData()
}

function handleEntryReset() {
  entrySearchForm.dictLabel = ''
  loadEntryData()
}

function handleAddEntry() {
  isEntryEdit.value = false
  entryDialogTitle.value = '新增字典条目'
  entryEditId.value = null
  resetEntryForm()
  entryDialogVisible.value = true
}

async function handleEditEntry(row) {
  isEntryEdit.value = true
  entryDialogTitle.value = '编辑字典条目'
  entryEditId.value = row.id
  entryDialogVisible.value = true
  try {
    const res = await getById(row.id)
    const data = res.data
    Object.assign(entryFormData, {
      dictLabel: data.dictLabel || '',
      dictValue: data.dictValue || '',
      sortOrder: data.sortOrder || 0,
      status: data.status ?? 1
    })
  } catch (error) {
    ElMessage.error('获取字典条目信息失败')
  }
}

function handleDeleteEntry(row) {
  ElMessageBox.confirm(`确定要删除字典条目"${row.dictLabel}"吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDict(row.id)
      ElMessage.success('删除成功')
      loadEntryData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

async function handleEntryDialogConfirm() {
  const valid = await entryFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    const payload = {
      ...entryFormData,
      dictType: currentDictType.value
    }
    if (isEntryEdit.value) {
      await update({ ...payload, id: entryEditId.value })
      ElMessage.success('更新成功')
    } else {
      await add(payload)
      ElMessage.success('新增成功')
    }
    entryDialogVisible.value = false
    loadEntryData()
  } catch (error) {
    ElMessage.error(isEntryEdit.value ? '更新失败' : '新增失败')
  }
}

function handleEntryDialogClosed() {
  resetEntryForm()
}

function resetEntryForm() {
  Object.assign(entryFormData, { dictLabel: '', dictValue: '', sortOrder: 0, status: 1 })
  entryFormRef.value?.resetFields()
}

onMounted(() => {
  loadTypeData()
})
</script>

<style scoped>
.dict-list-container {
  padding: 16px;
}

.left-card,
.right-card {
  min-height: 600px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 12px;
}
</style>
