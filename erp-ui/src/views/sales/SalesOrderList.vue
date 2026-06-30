<template>
  <div class="sales-order-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline ref="searchFormRef">
        <el-form-item label="销售单号" prop="soNo">
          <el-input v-model="searchForm.soNo" placeholder="请输入销售单号" clearable />
        </el-form-item>
        <el-form-item label="客户" prop="customerId">
          <el-select v-model="searchForm.customerId" placeholder="请选择客户" clearable filterable>
            <el-option v-for="c in customerOptions" :key="c.id" :label="c.customerName" :value="c.id" />
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
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增销售订单</el-button>
        <el-button :icon="Download" @click="handleExport">导出</el-button>
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
        <el-table-column prop="soNo" label="销售单号" min-width="160" sortable="custom" />
        <el-table-column prop="customerName" label="客户名称" width="180" />
        <el-table-column prop="orderDate" label="下单日期" width="120" sortable="custom" />
        <el-table-column prop="deliveryDate" label="交付日期" width="120" />
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
              v-if="['PENDING', 'DRAFT'].includes(row.status)"
              link type="primary" size="small"
              @click="handleEdit(row)"
            >编辑</el-button>
            <el-popconfirm
              v-if="['PENDING', 'DRAFT'].includes(row.status)"
              title="确定要删除该订单吗？" @confirm="handleDelete(row)"
            >
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
            <el-button
              v-if="['PENDING'].includes(row.status)"
              link type="success" size="small"
              @click="handleConfirm(row)"
            >确认</el-button>
            <el-button
              v-if="['CONFIRMED'].includes(row.status)"
              link type="warning" size="small"
              @click="handleClose(row)"
            >关闭</el-button>
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
            <el-form-item label="销售单号" prop="soNo">
              <el-input v-model="formData.soNo" disabled placeholder="系统自动生成" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户" prop="customerId">
              <el-select v-model="formData.customerId" placeholder="请选择客户" filterable style="width: 100%">
                <el-option v-for="c in customerOptions" :key="c.id" :label="c.customerName" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="报价单号" prop="quotationId">
              <el-input v-model="formData.quotationNo" disabled placeholder="关联报价单" />
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
            <el-form-item label="交付日期" prop="deliveryDate">
              <el-date-picker v-model="formData.deliveryDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider content-position="left">订单明细</el-divider>
      <el-button type="primary" size="small" :icon="Plus" @click="handleAddDetail" style="margin-bottom: 12px">
        添加产品
      </el-button>
      <el-table :data="formData.details" border size="small">
        <el-table-column prop="productCode" label="产品编码" min-width="130">
          <template #default="{ row }">
            <el-input v-model="row.productCode" placeholder="产品编码" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="产品名称" min-width="130">
          <template #default="{ row }">
            <el-input v-model="row.productName" placeholder="产品名称" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="110">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="1" size="small" controls-position="right" style="width: 100%" />
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
        <span style="font-weight: bold;">订单总金额：</span>
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
      title="销售订单详情"
      width="950px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="销售单号">{{ detailData.soNo }}</el-descriptions-item>
        <el-descriptions-item label="客户">{{ detailData.customerName }}</el-descriptions-item>
        <el-descriptions-item label="报价单号">{{ detailData.quotationNo }}</el-descriptions-item>
        <el-descriptions-item label="下单日期">{{ detailData.orderDate }}</el-descriptions-item>
        <el-descriptions-item label="交付日期">{{ detailData.deliveryDate }}</el-descriptions-item>
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
      <el-table :data="detailData.details || []" border size="small" empty-text="暂无明细">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="productCode" label="产品编码" width="130" />
        <el-table-column prop="productName" label="产品名称" width="130" />
        <el-table-column prop="quantity" label="订购数量" width="90" />
        <el-table-column prop="shippedQty" label="已发数量" width="90" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="unitPrice" label="单价" width="100" />
        <el-table-column label="金额" width="110">
          <template #default="{ row }">
            {{ formatMoney((row.quantity || 0) * (row.unitPrice || 0)) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import { salesOrder } from '@/api/modules/sales'
import { pageCustomer } from '@/api/modules/customer'

// ---------- 状态映射 ----------
const statusMap = {
  DRAFT: { label: '草稿', type: 'info' },
  PENDING: { label: '待审批', type: 'warning' },
  CONFIRMED: { label: '已确认', type: 'primary' },
  PARTIAL_SHIPPED: { label: '部分发货', type: 'info' },
  COMPLETED: { label: '已完成', type: 'success' },
  CANCELLED: { label: '已取消', type: 'danger' }
}

const formatMoney = (val) => {
  if (val == null) return '¥0.00'
  return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// ---------- 搜索 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  soNo: '', customerId: '', status: '', orderDateRange: []
})

// ---------- 表格 ----------
const tableLoading = ref(false)
const tableData = ref([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const customerOptions = ref([])

// ---------- 表单 ----------
const dialogVisible = ref(false)
const dialogTitle = ref('新增销售订单')
const submitLoading = ref(false)
const isEdit = ref(false)
const editId = ref(null)

const formRef = ref(null)
const formData = reactive({
  soNo: '', customerId: '', quotationId: '', quotationNo: '',
  orderDate: '', deliveryDate: '', details: []
})

const formRules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  orderDate: [{ required: true, message: '请选择下单日期', trigger: 'change' }]
}

const computedTotal = computed(() => {
  return (formData.details || []).reduce((sum, d) => sum + (d.quantity || 0) * (d.unitPrice || 0), 0)
})

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- 加载客户 ----------
const loadCustomers = async () => {
  try {
    const res = await pageCustomer({ page: 1, pageSize: 999, status: 1 })
    customerOptions.value = res.data?.records || res.records || []
  } catch (e) { /* ignore */ }
}

// ---------- 列表 ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current,
      pageSize: pagination.pageSize,
      soNo: searchForm.soNo || undefined,
      customerId: searchForm.customerId || undefined,
      status: searchForm.status || undefined,
      orderDateStart: searchForm.orderDateRange?.[0] || undefined,
      orderDateEnd: searchForm.orderDateRange?.[1] || undefined
    }
    const res = await salesOrder.page(params)
    tableData.value = res.data?.records || res.records || []
    pagination.total = res.data?.total || res.total || 0
  } catch (error) { ElMessage.error('查询失败') } finally { tableLoading.value = false }
}

const handleSearch = () => { pagination.current = 1; fetchData() }
const handleReset = () => { searchFormRef.value?.resetFields(); pagination.current = 1; fetchData() }
const handleSizeChange = () => { pagination.current = 1; fetchData() }
const handleCurrentChange = () => { fetchData() }

// ---------- CRUD ----------
const resetFormData = () => {
  formData.soNo = ''; formData.customerId = ''; formData.quotationId = ''; formData.quotationNo = ''
  formData.orderDate = ''; formData.deliveryDate = ''; formData.details = []
}

const handleAdd = () => {
  isEdit.value = false; editId.value = null
  dialogTitle.value = '新增销售订单'
  resetFormData()
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true; editId.value = row.id
  dialogTitle.value = '编辑销售订单'
  try {
    const res = await salesOrder.getById(row.id)
    const data = res.data || res
    Object.assign(formData, {
      soNo: data.soNo || '', customerId: data.customerId || '',
      quotationId: data.quotationId || '', quotationNo: data.quotationNo || '',
      orderDate: data.orderDate || '', deliveryDate: data.deliveryDate || '',
      details: data.details ? [...data.details] : []
    })
    dialogVisible.value = true
  } catch (error) { ElMessage.error('获取订单信息失败') }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (formData.details.length === 0) { ElMessage.warning('请至少添加一条明细'); return }
  submitLoading.value = true
  try {
    const payload = { ...formData, totalAmount: computedTotal.value }
    if (isEdit.value) {
      await salesOrder.update({ id: editId.value, ...payload })
      ElMessage.success('更新成功')
    } else {
      await salesOrder.add({ ...payload, status: 'PENDING' })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false; fetchData()
  } catch (error) { ElMessage.error((isEdit.value ? '更新' : '新增') + '失败') } finally { submitLoading.value = false }
}

const handleDialogClosed = () => { resetFormData(); formRef.value?.resetFields() }

const handleAddDetail = () => {
  formData.details.push({ productCode: '', productName: '', quantity: 1, unit: '个', unitPrice: 0, remark: '' })
}

const handleRemoveDetail = (index) => { formData.details.splice(index, 1) }

const handleView = async (row) => {
  try {
    const res = await salesOrder.getById(row.id)
    Object.assign(detailData, res.data || res)
    detailVisible.value = true
  } catch (error) { ElMessage.error('获取详情失败') }
}

const handleDelete = async (row) => {
  try { await salesOrder.remove(row.id); ElMessage.success('删除成功'); fetchData() }
  catch (error) { ElMessage.error('删除失败') }
}

const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确定要确认该销售订单吗？', '提示', { type: 'warning' })
    await salesOrder.confirm(row.id)
    ElMessage.success('订单已确认'); fetchData()
  } catch (error) { if (error !== 'cancel') ElMessage.error('确认失败') }
}

const handleClose = async (row) => {
  try {
    await ElMessageBox.confirm('确定要关闭该销售订单吗？关闭后无法恢复。', '提示', { type: 'warning' })
    await salesOrder.close(row.id)
    ElMessage.success('订单已关闭'); fetchData()
  } catch (error) { if (error !== 'cancel') ElMessage.error('关闭失败') }
}

const handleExport = () => { ElMessage.info('导出功能开发中') }

onMounted(() => { loadCustomers(); fetchData() })
</script>

<style scoped lang="scss">
.sales-order-list-container {
  padding: 16px;
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .action-bar { display: flex; align-items: center; }
  .pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 16px; }
  .dialog-footer { display: flex; justify-content: flex-end; gap: 8px; }
}
</style>
