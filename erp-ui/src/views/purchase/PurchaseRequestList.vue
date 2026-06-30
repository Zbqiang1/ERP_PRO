<template>
  <div class="purchase-request-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline ref="searchFormRef">
        <el-form-item label="申请单号" prop="prNo">
          <el-input v-model="searchForm.prNo" placeholder="请输入申请单号" clearable />
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
          <el-input v-model="searchForm.applicant" placeholder="请输入申请人" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已转订单" value="CONVERTED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期" prop="applyDateRange">
          <el-date-picker
            v-model="searchForm.applyDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
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
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增采购申请</el-button>
          <el-button :icon="Download" @click="handleExport">导出</el-button>
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
        empty-text="暂无数据"
      >
        <el-table-column prop="prNo" label="申请单号" min-width="160" sortable="custom" />
        <el-table-column prop="deptName" label="申请部门" width="120" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="applyDate" label="申请日期" width="120" sortable="custom" />
        <el-table-column prop="expectedDeliveryDate" label="期望到货日期" width="140" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type" size="small">
              {{ statusMap[row.status]?.label || row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" sortable="custom" />
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.status === 'DRAFT'"
              link
              type="primary"
              size="small"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-popconfirm
              v-if="row.status === 'DRAFT'"
              title="确定要删除该申请单吗？"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
            <el-button
              v-if="row.status === 'DRAFT'"
              link
              type="success"
              size="small"
              @click="handleSubmitApproval(row)"
            >
              提交审批
            </el-button>
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
      width="900px"
      :close-on-click-modal="false"
      destroy-on-close
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="申请单号" prop="prNo">
              <el-input v-model="formData.prNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请部门" prop="deptId">
              <el-select v-model="formData.deptId" placeholder="请选择部门" clearable>
                <el-option label="生产部" value="1" />
                <el-option label="研发部" value="2" />
                <el-option label="采购部" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请人" prop="applicantId">
              <el-input v-model="formData.applicantName" placeholder="当前用户" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="申请日期" prop="applyDate">
              <el-date-picker v-model="formData.applyDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="期望到货日期" prop="expectedDeliveryDate">
              <el-date-picker v-model="formData.expectedDeliveryDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 申请明细 -->
      <el-divider content-position="left">申请明细</el-divider>
      <el-button type="primary" size="small" :icon="Plus" @click="handleAddDetail" style="margin-bottom: 12px">
        添加物料
      </el-button>
      <el-table :data="formData.details" border size="small">
        <el-table-column prop="materialCode" label="物料编码" min-width="130">
          <template #default="{ row, $index }">
            <el-input v-model="row.materialCode" placeholder="物料编码" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="materialName" label="物料名称" min-width="130">
          <template #default="{ row, $index }">
            <el-input v-model="row.materialName" placeholder="物料名称" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="specification" label="规格型号" width="120">
          <template #default="{ row }">
            <el-input v-model="row.specification" placeholder="规格" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="110">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" :precision="0" size="small" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80">
          <template #default="{ row }">
            <el-input v-model="row.unit" placeholder="单位" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="estimatedUnitPrice" label="预估单价" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.estimatedUnitPrice" :min="0" :precision="2" size="small" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column prop="estimatedAmount" label="预估金额" width="120">
          <template #default="{ row }">
            {{ ((row.quantity || 0) * (row.estimatedUnitPrice || 0)).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="130">
          <template #default="{ row }">
            <el-input v-model="row.remark" placeholder="备注" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="70" align="center">
          <template #default="{ $index }">
            <el-button link type="danger" size="small" @click="handleRemoveDetail($index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存草稿</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="采购申请详情"
      width="900px"
      :close-on-click-modal="false"
    >
      <!-- 头信息 -->
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="申请单号">{{ detailData.prNo }}</el-descriptions-item>
        <el-descriptions-item label="申请部门">{{ detailData.deptName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ detailData.applyDate }}</el-descriptions-item>
        <el-descriptions-item label="期望到货日期">{{ detailData.expectedDeliveryDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusMap[detailData.status]?.type" size="small">
            {{ statusMap[detailData.status]?.label || detailData.status }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>

      <!-- 明细 -->
      <el-divider content-position="left">申请明细</el-divider>
      <el-table :data="detailData.details || []" border size="small" :empty-text="'暂无明细'">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="materialCode" label="物料编码" width="130" />
        <el-table-column prop="materialName" label="物料名称" width="130" />
        <el-table-column prop="specification" label="规格型号" width="120" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="estimatedUnitPrice" label="预估单价" width="120" />
        <el-table-column label="预估金额" width="120">
          <template #default="{ row }">
            {{ ((row.quantity || 0) * (row.estimatedUnitPrice || 0)).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="130" />
      </el-table>

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
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import { purchaseRequest } from '@/api/modules/purchase'

// ---------- 状态映射 ----------
const statusMap = {
  DRAFT: { label: '草稿', type: 'info' },
  APPROVED: { label: '已审批', type: 'success' },
  CONVERTED: { label: '已转订单', type: 'warning' },
  REJECTED: { label: '已驳回', type: 'danger' }
}

// ---------- 搜索 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  prNo: '',
  applicant: '',
  status: '',
  applyDateRange: []
})

// ---------- 表格 ----------
const tableLoading = ref(false)
const tableData = ref([])
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// ---------- 表单 ----------
const dialogVisible = ref(false)
const dialogTitle = ref('新增采购申请')
const submitLoading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const formRef = ref(null)
const formData = reactive({
  prNo: '',
  deptId: '',
  applicantId: '',
  applicantName: '',
  applyDate: '',
  expectedDeliveryDate: '',
  details: []
})

const formRules = {
  deptId: [{ required: true, message: '请选择申请部门', trigger: 'change' }],
  applyDate: [{ required: true, message: '请选择申请日期', trigger: 'change' }],
  expectedDeliveryDate: [{ required: true, message: '请选择期望到货日期', trigger: 'change' }]
}

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- CRUD ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current,
      pageSize: pagination.pageSize,
      prNo: searchForm.prNo || undefined,
      applicant: searchForm.applicant || undefined,
      status: searchForm.status || undefined,
      applyDateStart: searchForm.applyDateRange?.[0] || undefined,
      applyDateEnd: searchForm.applyDateRange?.[1] || undefined
    }
    const res = await purchaseRequest.page(params)
    tableData.value = res.data?.records || res.records || []
    pagination.total = res.data?.total || res.total || 0
  } catch (error) {
    ElMessage.error('查询失败：' + (error.message || '未知错误'))
  } finally {
    tableLoading.value = false
  }
}

const handleSearch = () => { pagination.current = 1; fetchData() }
const handleReset = () => { searchFormRef.value?.resetFields(); pagination.current = 1; fetchData() }
const handleSizeChange = () => { pagination.current = 1; fetchData() }
const handleCurrentChange = () => { fetchData() }

const resetFormData = () => {
  formData.prNo = ''
  formData.deptId = ''
  formData.applicantId = ''
  formData.applicantName = ''
  formData.applyDate = ''
  formData.expectedDeliveryDate = ''
  formData.details = []
}

const handleAdd = () => {
  isEdit.value = false
  editId.value = null
  dialogTitle.value = '新增采购申请'
  resetFormData()
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true
  editId.value = row.id
  dialogTitle.value = '编辑采购申请'
  try {
    const res = await purchaseRequest.getById(row.id)
    const data = res.data || res
    Object.assign(formData, {
      prNo: data.prNo || '',
      deptId: data.deptId || '',
      applicantId: data.applicantId || '',
      applicantName: data.applicantName || '',
      applyDate: data.applyDate || '',
      expectedDeliveryDate: data.expectedDeliveryDate || '',
      details: data.details ? [...data.details] : []
    })
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取申请单信息失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (formData.details.length === 0) {
    ElMessage.warning('请至少添加一条申请明细')
    return
  }
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await purchaseRequest.update({ id: editId.value, ...formData })
      ElMessage.success('更新成功')
    } else {
      await purchaseRequest.add({ ...formData, status: 'DRAFT' })
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

const handleDialogClosed = () => {
  resetFormData()
  formRef.value?.resetFields()
}

// ---------- 明细操作 ----------
const handleAddDetail = () => {
  formData.details.push({
    materialCode: '',
    materialName: '',
    specification: '',
    quantity: 1,
    unit: '个',
    estimatedUnitPrice: 0,
    remark: ''
  })
}

const handleRemoveDetail = (index) => {
  formData.details.splice(index, 1)
}

// ---------- 查看 ----------
const handleView = async (row) => {
  try {
    const res = await purchaseRequest.getById(row.id)
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
    await purchaseRequest.remove(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// ---------- 提交审批 ----------
const handleSubmitApproval = async (row) => {
  try {
    await ElMessageBox.confirm('确定要提交审批吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await purchaseRequest.submitApproval(row.id)
    ElMessage.success('提交审批成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('提交审批失败')
    }
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.purchase-request-list-container {
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
