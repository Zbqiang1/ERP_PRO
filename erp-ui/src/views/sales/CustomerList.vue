<template>
  <div class="customer-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline ref="searchFormRef">
        <el-form-item label="客户编码" prop="customerCode">
          <el-input v-model="searchForm.customerCode" placeholder="请输入客户编码" clearable />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户名称" clearable />
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
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增客户</el-button>
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
        empty-text="暂无数据"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="customerCode" label="客户编码" min-width="140" sortable="custom" />
        <el-table-column prop="customerName" label="客户名称" min-width="180" sortable="custom" />
        <el-table-column prop="contactPerson" label="联系人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="creditLimit" label="信用额度" width="140" sortable="custom">
          <template #default="{ row }">
            {{ formatMoney(row.creditLimit) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm
              title="确定要删除该客户吗？"
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
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户编码" prop="customerCode">
              <el-input v-model="formData.customerCode" placeholder="请输入客户编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
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
            <el-form-item label="信用额度" prop="creditLimit">
              <el-input-number v-model="formData.creditLimit" :min="0" :precision="2" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="formData.address" placeholder="请输入地址" />
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
      title="客户详情"
      width="650px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="客户编码">{{ detailData.customerCode }}</el-descriptions-item>
        <el-descriptions-item label="客户名称">{{ detailData.customerName }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ detailData.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailData.email }}</el-descriptions-item>
        <el-descriptions-item label="信用额度">{{ formatMoney(detailData.creditLimit) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 1 ? 'success' : 'danger'" size="small">
            {{ detailData.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detailData.address }}</el-descriptions-item>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, Delete } from '@element-plus/icons-vue'
import { pageCustomer, getById, addCustomer, updateCustomer, deleteCustomer } from '@/api/modules/customer'

// ---------- 工具 ----------
const formatMoney = (val) => {
  if (val == null) return '¥0.00'
  return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// ---------- 搜索 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  customerCode: '',
  customerName: '',
  status: ''
})

// ---------- 表格 ----------
const tableLoading = ref(false)
const tableData = ref([])
const selectedRows = ref([])

const handleSelectionChange = (rows) => { selectedRows.value = rows }

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// ---------- 对话框 ----------
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const submitLoading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const formRef = ref(null)
const formData = reactive({
  customerCode: '',
  customerName: '',
  contactPerson: '',
  phone: '',
  email: '',
  address: '',
  creditLimit: 0,
  status: 1
})

const formRules = {
  customerCode: [{ required: true, message: '请输入客户编码', trigger: 'blur' }],
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }]
}

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- 列表 ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await pageCustomer(params)
    tableData.value = res.data?.records || res.records || []
    pagination.total = res.data?.total || res.total || 0
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    tableLoading.value = false
  }
}

const handleSearch = () => { pagination.current = 1; fetchData() }
const handleReset = () => { searchFormRef.value?.resetFields(); pagination.current = 1; fetchData() }
const handleSizeChange = () => { pagination.current = 1; fetchData() }
const handleCurrentChange = () => { fetchData() }

// ---------- CRUD ----------
const resetForm = () => {
  formRef.value?.resetFields()
  formData.customerCode = ''
  formData.customerName = ''
  formData.contactPerson = ''
  formData.phone = ''
  formData.email = ''
  formData.address = ''
  formData.creditLimit = 0
  formData.status = 1
}

const handleAdd = () => {
  isEdit.value = false
  editId.value = null
  dialogTitle.value = '新增客户'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true
  editId.value = row.id
  dialogTitle.value = '编辑客户'
  try {
    const res = await getById(row.id)
    const data = res.data || res
    Object.assign(formData, {
      customerCode: data.customerCode || '',
      customerName: data.customerName || '',
      contactPerson: data.contactPerson || '',
      phone: data.phone || '',
      email: data.email || '',
      address: data.address || '',
      creditLimit: data.creditLimit ?? 0,
      status: data.status ?? 1
    })
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取客户信息失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateCustomer({ id: editId.value, ...formData })
      ElMessage.success('更新成功')
    } else {
      await addCustomer(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error((isEdit.value ? '更新' : '新增') + '失败')
  } finally {
    submitLoading.value = false
  }
}

const handleDialogClosed = () => { resetForm() }

// ---------- 查看 ----------
const handleView = async (row) => {
  try {
    const res = await getById(row.id)
    Object.assign(detailData, res.data || res)
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

// ---------- 删除 ----------
const handleDelete = async (row) => {
  try {
    await deleteCustomer(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    ElMessage.error('删除失败')
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
    await Promise.all(selectedRows.value.map(r => deleteCustomer(r.id)))
    ElMessage.success('批量删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('批量删除失败')
  }
}

const handleExport = () => { ElMessage.info('导出功能开发中') }

onMounted(() => { fetchData() })
</script>

<style scoped lang="scss">
.customer-list-container {
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
