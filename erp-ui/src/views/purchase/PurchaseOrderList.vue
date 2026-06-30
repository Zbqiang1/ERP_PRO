<template>
  <div class="purchase-order-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline ref="searchFormRef">
        <el-form-item label="采购单号" prop="poNo">
          <el-input v-model="searchForm.poNo" placeholder="请输入采购单号" clearable />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierId">
          <el-select v-model="searchForm.supplierId" placeholder="请选择供应商" clearable filterable>
            <el-option v-for="s in supplierOptions" :key="s.id" :label="s.supplierName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option v-for="(v, k) in statusMap" :key="k" :label="v.label" :value="k" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单日期" prop="orderDateRange">
          <el-date-picker
            v-model="searchForm.orderDateRange"
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
          <el-button type="primary" :icon="Plus" @click="handleAdd">新增采购订单</el-button>
          <el-button type="success" :icon="DocumentAdd" @click="handleCreateFromPR">从申请单生成</el-button>
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
        <el-table-column prop="poNo" label="采购单号" min-width="160" sortable="custom" />
        <el-table-column prop="supplierName" label="供应商" width="180" />
        <el-table-column prop="orderDate" label="下单日期" width="120" sortable="custom" />
        <el-table-column prop="deliveryDate" label="到货日期" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="140" sortable="custom">
          <template #default="{ row }">
            {{ formatMoney(row.totalAmount) }}
          </template>
        </el-table-column>
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
              v-if="['PENDING'].includes(row.status)"
              link
              type="primary"
              size="small"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-popconfirm
              v-if="['PENDING'].includes(row.status)"
              title="确定要删除该订单吗？"
              confirm-button-text="确定"
              cancel-button-text="取消"
              @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
            <el-button
              v-if="['PENDING'].includes(row.status)"
              link
              type="success"
              size="small"
              @click="handleConfirm(row)"
            >
              确认
            </el-button>
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
      width="950px"
      :close-on-click-modal="false"
      destroy-on-close
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="采购单号" prop="poNo">
              <el-input v-model="formData.poNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="供应商" prop="supplierId">
              <el-select v-model="formData.supplierId" placeholder="请选择供应商" filterable style="width: 100%">
                <el-option v-for="s in supplierOptions" :key="s.id" :label="s.supplierName" :value="s.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="申请单号" prop="prId">
              <el-input v-model="formData.prNo" placeholder="关联申请单" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="下单日期" prop="orderDate">
              <el-date-picker v-model="formData.orderDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="到货日期" prop="deliveryDate">
              <el-date-picker v-model="formData.deliveryDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider content-position="left">订单明细</el-divider>
      <el-button type="primary" size="small" :icon="Plus" @click="handleAddDetail" style="margin-bottom: 12px">
        添加物料
      </el-button>
      <el-table :data="formData.details" border size="small">
        <el-table-column prop="materialCode" label="物料编码" min-width="130">
          <template #default="{ row }">
            <el-input v-model="row.materialCode" placeholder="物料编码" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="materialName" label="物料名称" min-width="130">
          <template #default="{ row }">
            <el-input v-model="row.materialName" placeholder="物料名称" size="small" />
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
        <el-table-column prop="unitPrice" label="单价" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.unitPrice" :min="0" :precision="2" size="small" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="金额" width="110">
          <template #default="{ row }">
            {{ ((row.quantity || 0) * (row.unitPrice || 0)).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120">
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

      <div style="margin-top: 12px; text-align: right;">
        <span style="font-weight: bold;">合计金额：</span>
        <span style="color: #f56c6c; font-size: 16px;">{{ computedTotal.toFixed(2) }}</span>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="采购订单详情"
      width="950px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="采购单号">{{ detailData.poNo }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ detailData.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="申请单号">{{ detailData.prNo }}</el-descriptions-item>
        <el-descriptions-item label="下单日期">{{ detailData.orderDate }}</el-descriptions-item>
        <el-descriptions-item label="到货日期">{{ detailData.deliveryDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusMap[detailData.status]?.type" size="small">
            {{ statusMap[detailData.status]?.label || detailData.status }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="总金额">
          <span style="color: #f56c6c; font-weight: bold;">{{ formatMoney(detailData.totalAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">订单明细</el-divider>
      <el-table :data="detailData.details || []" border size="small" :empty-text="'暂无明细'">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="materialCode" label="物料编码" width="130" />
        <el-table-column prop="materialName" label="物料名称" width="130" />
        <el-table-column prop="quantity" label="订购数量" width="90" />
        <el-table-column prop="receivedQty" label="已收数量" width="90" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="unitPrice" label="单价" width="100">
          <template #default="{ row }">{{ formatMoney(row.unitPrice) }}</template>
        </el-table-column>
        <el-table-column label="金额" width="110">
          <template #default="{ row }">{{ formatMoney((row.quantity || 0) * (row.unitPrice || 0)) }}</template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" />
      </el-table>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 从PR生成采购单对话框 -->
    <el-dialog
      v-model="prDialogVisible"
      title="选择已审批的采购申请单"
      width="750px"
      :close-on-click-modal="false"
    >
      <el-table
        v-loading="prLoading"
        :data="prTableData"
        border
        @row-click="handleSelectPR"
        highlight-current-row
        style="cursor: pointer"
        max-height="400"
        empty-text="暂无已审批的申请单"
      >
        <el-table-column prop="prNo" label="申请单号" width="160" />
        <el-table-column prop="deptName" label="申请部门" width="120" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="applyDate" label="申请日期" width="120" />
        <el-table-column prop="expectedDeliveryDate" label="期望到货日期" width="140" />
      </el-table>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="prDialogVisible = false">取消</el-button>
          <el-button type="primary" :disabled="!selectedPR" @click="handleConfirmCreateFromPR">确定生成</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, DocumentAdd } from '@element-plus/icons-vue'
import { purchaseOrder } from '@/api/modules/purchase'
import { pageSupplier } from '@/api/modules/supplier'
import { purchaseRequest } from '@/api/modules/purchase'

// ---------- 状态映射 ----------
const statusMap = {
  PENDING: { label: '待确认', type: 'warning' },
  CONFIRMED: { label: '已确认', type: 'primary' },
  PARTIAL_RECEIVED: { label: '部分到货', type: 'info' },
  COMPLETED: { label: '已完成', type: 'success' },
  CANCELLED: { label: '已取消', type: 'danger' }
}

// ---------- 工具函数 ----------
const formatMoney = (val) => {
  if (val == null) return '0.00'
  return Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// ---------- 搜索 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  poNo: '',
  supplierId: '',
  status: '',
  orderDateRange: []
})

// ---------- 表格 ----------
const tableLoading = ref(false)
const tableData = ref([])
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

const supplierOptions = ref([])

// ---------- 表单 ----------
const dialogVisible = ref(false)
const dialogTitle = ref('新增采购订单')
const submitLoading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const formRef = ref(null)
const formData = reactive({
  poNo: '',
  supplierId: '',
  prId: '',
  prNo: '',
  orderDate: '',
  deliveryDate: '',
  details: []
})

const formRules = {
  supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
  orderDate: [{ required: true, message: '请选择下单日期', trigger: 'change' }],
  deliveryDate: [{ required: true, message: '请选择到货日期', trigger: 'change' }]
}

const computedTotal = computed(() => {
  return (formData.details || []).reduce((sum, d) => sum + (d.quantity || 0) * (d.unitPrice || 0), 0)
})

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- PR选择 ----------
const prDialogVisible = ref(false)
const prLoading = ref(false)
const prTableData = ref([])
const selectedPR = ref(null)

// ---------- 加载供应商 ----------
const loadSuppliers = async () => {
  try {
    const res = await pageSupplier({ page: 1, pageSize: 999, status: 1 })
    supplierOptions.value = res.data?.records || res.records || []
  } catch (e) { /* ignore */ }
}

// ---------- 列表加载 ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current,
      pageSize: pagination.pageSize,
      poNo: searchForm.poNo || undefined,
      supplierId: searchForm.supplierId || undefined,
      status: searchForm.status || undefined,
      orderDateStart: searchForm.orderDateRange?.[0] || undefined,
      orderDateEnd: searchForm.orderDateRange?.[1] || undefined
    }
    const res = await purchaseOrder.page(params)
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
const resetFormData = () => {
  formData.poNo = ''
  formData.supplierId = ''
  formData.prId = ''
  formData.prNo = ''
  formData.orderDate = ''
  formData.deliveryDate = ''
  formData.details = []
}

const handleAdd = () => {
  isEdit.value = false
  editId.value = null
  dialogTitle.value = '新增采购订单'
  resetFormData()
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true
  editId.value = row.id
  dialogTitle.value = '编辑采购订单'
  try {
    const res = await purchaseOrder.getById(row.id)
    const data = res.data || res
    Object.assign(formData, {
      poNo: data.poNo || '',
      supplierId: data.supplierId || '',
      prId: data.prId || '',
      prNo: data.prNo || '',
      orderDate: data.orderDate || '',
      deliveryDate: data.deliveryDate || '',
      details: data.details ? [...data.details] : []
    })
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取订单信息失败')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (formData.details.length === 0) {
    ElMessage.warning('请至少添加一条订单明细')
    return
  }
  submitLoading.value = true
  try {
    const payload = { ...formData, totalAmount: computedTotal.value }
    if (isEdit.value) {
      await purchaseOrder.update({ id: editId.value, ...payload })
      ElMessage.success('更新成功')
    } else {
      await purchaseOrder.add({ ...payload, status: 'PENDING' })
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
    quantity: 1,
    unit: '个',
    unitPrice: 0,
    remark: ''
  })
}

const handleRemoveDetail = (index) => {
  formData.details.splice(index, 1)
}

// ---------- 查看 ----------
const handleView = async (row) => {
  try {
    const res = await purchaseOrder.getById(row.id)
    Object.assign(detailData, res.data || res)
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

// ---------- 删除 ----------
const handleDelete = async (row) => {
  try {
    await purchaseOrder.remove(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// ---------- 确认 ----------
const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确定要确认该采购订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await purchaseOrder.confirm(row.id)
    ElMessage.success('订单已确认')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('确认失败')
    }
  }
}

// ---------- 从PR生成 ----------
const handleCreateFromPR = async () => {
  selectedPR.value = null
  prDialogVisible.value = true
  prLoading.value = true
  try {
    const res = await purchaseRequest.page({ page: 1, pageSize: 999, status: 'APPROVED' })
    prTableData.value = res.data?.records || res.records || []
  } catch (error) {
    ElMessage.error('加载申请单失败')
  } finally {
    prLoading.value = false
  }
}

const handleSelectPR = (row) => {
  selectedPR.value = row
}

const handleConfirmCreateFromPR = async () => {
  if (!selectedPR.value) {
    ElMessage.warning('请选择一条申请单')
    return
  }
  try {
    isEdit.value = false
    editId.value = null
    dialogTitle.value = '从申请单生成采购订单'
    resetFormData()
    formData.prId = selectedPR.value.id
    formData.prNo = selectedPR.value.prNo
    // 将申请明细转为订单明细
    formData.details = (selectedPR.value.details || []).map(d => ({
      materialCode: d.materialCode || '',
      materialName: d.materialName || '',
      quantity: d.quantity || 1,
      unit: d.unit || '个',
      unitPrice: d.estimatedUnitPrice || 0,
      remark: ''
    }))
    prDialogVisible.value = false
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('生成失败')
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

onMounted(() => {
  loadSuppliers()
  fetchData()
})
</script>

<style scoped lang="scss">
.purchase-order-list-container {
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
