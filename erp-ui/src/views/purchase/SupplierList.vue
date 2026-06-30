<template>
  <div class="supplier-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline :gutter="20" ref="searchFormRef">
        <el-form-item label="供应商编码" prop="supplierCode">
          <el-input v-model="searchForm.supplierCode" placeholder="请输入供应商编码" clearable />
        </el-form-item>
        <el-form-item label="供应商名称" prop="supplierName">
          <el-input v-model="searchForm.supplierName" placeholder="请输入供应商名称" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <el-card class="action-card" shadow="never">
      <div class="action-bar">
        <div class="action-left">
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增供应商</el-button>
          <el-button :icon="Download" @click="handleExport">导出</el-button>
        </div>
        <div class="action-right">
          <el-button
            type="danger"
            :icon="Delete"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
        :empty-text="'暂无数据'"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="supplierCode" label="供应商编码" min-width="140" sortable="custom" />
        <el-table-column prop="supplierName" label="供应商名称" min-width="180" sortable="custom" />
        <el-table-column prop="contactPerson" label="联系人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" sortable="custom" />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm
              title="确定要删除该供应商吗？"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="650px"
      :close-on-click-modal="false"
      destroy-on-close
      @closed="handleDialogClosed"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="110px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商编码" prop="supplierCode">
              <el-input v-model="formData.supplierCode" placeholder="请输入供应商编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="supplierName">
              <el-input v-model="formData.supplierName" placeholder="请输入供应商名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactPerson">
              <el-input v-model="formData.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-switch
                v-model="formData.status"
                :active-value="1"
                :inactive-value="0"
                active-text="启用"
                inactive-text="禁用"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开户银行" prop="bankName">
              <el-input v-model="formData.bankName" placeholder="请输入开户银行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号" prop="bankAccount">
              <el-input v-model="formData.bankAccount" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="税号" prop="taxNo">
          <el-input v-model="formData.taxNo" placeholder="请输入税号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="供应商详情"
      width="650px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="供应商编码">{{ detailData.supplierCode }}</el-descriptions-item>
        <el-descriptions-item label="供应商名称">{{ detailData.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detailData.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailData.email }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 1 ? 'success' : 'danger'" size="small">
            {{ detailData.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detailData.address }}</el-descriptions-item>
        <el-descriptions-item label="开户银行">{{ detailData.bankName }}</el-descriptions-item>
        <el-descriptions-item label="银行账号">{{ detailData.bankAccount }}</el-descriptions-item>
        <el-descriptions-item label="税号" :span="2">{{ detailData.taxNo }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, Delete } from '@element-plus/icons-vue'
import { pageSupplier, getById, addSupplier, updateSupplier, deleteSupplier } from '@/api/modules/supplier'

// ---------- 搜索表单 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  supplierCode: '',
  supplierName: '',
  status: ''
})

// ---------- 表格数据 ----------
const tableLoading = ref(false)
const tableData = ref([])
const selectedRows = ref([])

// noinspection JSAnnotator
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// ---------- 分页 ----------
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// ---------- 对话框 ----------
const dialogVisible = ref(false)
const dialogTitle = ref('新增供应商')
const submitLoading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const formRef = ref(null)
const formData = reactive({
  supplierCode: '',
  supplierName: '',
  contactPerson: '',
  phone: '',
  email: '',
  address: '',
  bankName: '',
  bankAccount: '',
  taxNo: '',
  status: 1
})

const formRules = {
  supplierCode: [{ required: true, message: '请输入供应商编码', trigger: 'blur' }],
  supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }]
}

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- 查询 ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await pageSupplier(params)
    tableData.value = res.data?.records || res.records || []
    pagination.total = res.data?.total || res.total || 0
  } catch (error) {
    ElMessage.error('查询失败：' + (error.message || '未知错误'))
  } finally {
    tableLoading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  fetchData()
}

const handleReset = () => {
  searchFormRef.value?.resetFields()
  pagination.current = 1
  fetchData()
}

const handleSizeChange = () => {
  pagination.current = 1
  fetchData()
}

const handleCurrentChange = () => {
  fetchData()
}

// ---------- CRUD ----------
const handleAdd = () => {
  isEdit.value = false
  editId.value = null
  dialogTitle.value = '新增供应商'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true
  editId.value = row.id
  dialogTitle.value = '编辑供应商'
  try {
    const res = await getById(row.id)
    const data = res.data || res
    Object.assign(formData, {
      supplierCode: data.supplierCode || '',
      supplierName: data.supplierName || '',
      contactPerson: data.contactPerson || '',
      phone: data.phone || '',
      email: data.email || '',
      address: data.address || '',
      bankName: data.bankName || '',
      bankAccount: data.bankAccount || '',
      taxNo: data.taxNo || '',
      status: data.status ?? 1
    })
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取供应商信息失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateSupplier({ id: editId.value, ...formData })
      ElMessage.success('更新成功')
    } else {
      await addSupplier(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error((isEdit.value ? '更新' : '新增') + '失败：' + (error.message || '未知错误'))
  } finally {
    submitLoading.value = false
  }
}

const handleDialogClosed = () => {
  resetForm()
}

const resetForm = () => {
  formRef.value?.resetFields()
  formData.supplierCode = ''
  formData.supplierName = ''
  formData.contactPerson = ''
  formData.phone = ''
  formData.email = ''
  formData.address = ''
  formData.bankName = ''
  formData.bankAccount = ''
  formData.taxNo = ''
  formData.status = 1
}

// ---------- 查看详情 ----------
const handleView = async (row) => {
  try {
    const res = await getById(row.id)
    const data = res.data || res
    Object.assign(detailData, data)
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

// ---------- 删除 ----------
const handleDelete = async (row) => {
  try {
    await deleteSupplier(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    ElMessage.error('删除失败：' + (error.message || '未知错误'))
  }
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要删除的数据')
    return
  }
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 条数据吗？`,
      '批量删除',
      { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
    )
    const ids = selectedRows.value.map(r => r.id)
    await Promise.all(ids.map(id => deleteSupplier(id)))
    ElMessage.success('批量删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

// ---------- 导出 ----------
const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

// ---------- 生命周期 ----------
onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.supplier-list-container {
  padding: 16px;

  .search-card,
  .action-card,
  .table-card {
    margin-bottom: 16px;
  }

  .action-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pagination-wrapper {
    display: flex;
    justify-content: flex-end;
    margin-top: 16px;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
  }
}
</style>
