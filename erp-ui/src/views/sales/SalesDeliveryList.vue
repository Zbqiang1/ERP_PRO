<template>
  <div class="sales-delivery-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline ref="searchFormRef">
        <el-form-item label="发货单号" prop="deliveryNo">
          <el-input v-model="searchForm.deliveryNo" placeholder="请输入发货单号" clearable />
        </el-form-item>
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
            <el-option label="待发货" value="PENDING" />
            <el-option label="已发货" value="SHIPPED" />
            <el-option label="已签收" value="RECEIVED" />
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
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增发货单</el-button>
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
        <el-table-column prop="deliveryNo" label="发货单号" min-width="160" sortable="custom" />
        <el-table-column prop="soNo" label="销售单号" width="160" />
        <el-table-column prop="customerName" label="客户名称" width="180" />
        <el-table-column prop="warehouseName" label="发货仓库" width="120" />
        <el-table-column prop="deliveryDate" label="发货日期" width="120" sortable="custom" />
        <el-table-column prop="totalAmount" label="总金额" width="140">
          <template #default="{ row }">
            {{ formatMoney(row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="shippingAddress" label="收货地址" min-width="180" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)" size="small">
              {{ row.status === 'PENDING' ? '待发货' : row.status === 'SHIPPED' ? '已发货' : '已签收' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.status === 'PENDING'"
              link type="success" size="small"
              @click="handleConfirmDelivery(row)"
            >确认发货</el-button>
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

    <!-- 新增发货单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="新增发货单"
      width="950px"
      :close-on-click-modal="false"
      destroy-on-close
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="销售订单" prop="soId">
              <el-select v-model="formData.soId" placeholder="请选择已确认的销售订单" filterable style="width: 100%" @change="handleSOChange">
                <el-option v-for="so in soOptions" :key="so.id" :label="so.soNo" :value="so.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户" prop="customerName">
              <el-input v-model="formData.customerName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发货仓库" prop="warehouseId">
              <el-select v-model="formData.warehouseId" placeholder="请选择仓库" style="width: 100%">
                <el-option label="成品仓A" :value="1" />
                <el-option label="成品仓B" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="发货日期" prop="deliveryDate">
              <el-date-picker v-model="formData.deliveryDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="收货地址" prop="shippingAddress">
              <el-input v-model="formData.shippingAddress" placeholder="请输入收货地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="备注信息" />
        </el-form-item>
      </el-form>

      <el-divider content-position="left">发货明细</el-divider>
      <el-table :data="formData.details" border size="small">
        <el-table-column prop="productCode" label="产品编码" width="130" />
        <el-table-column prop="productName" label="产品名称" width="130" />
        <el-table-column prop="orderedQty" label="订购数量" width="90" />
        <el-table-column prop="shippedQty" label="已发数量" width="90" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="actualQty" label="本次发货数量" width="120">
          <template #default="{ row }">
            <el-input-number v-model="row.actualQty" :min="0" :max="(row.orderedQty || 0) - (row.shippedQty || 0)" size="small" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column prop="unitPrice" label="单价" width="100" />
        <el-table-column label="金额" width="110">
          <template #default="{ row }">
            {{ ((row.actualQty || 0) * (row.unitPrice || 0)).toFixed(2) }}
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 12px; text-align: right;">
        <span style="font-weight: bold;">发货总金额：</span>
        <span style="color: #f56c6c; font-size: 16px;">{{ computedDeliveryTotal.toFixed(2) }}</span>
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
      title="发货单详情"
      width="950px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="发货单号">{{ detailData.deliveryNo }}</el-descriptions-item>
        <el-descriptions-item label="销售单号">{{ detailData.soNo }}</el-descriptions-item>
        <el-descriptions-item label="客户">{{ detailData.customerName }}</el-descriptions-item>
        <el-descriptions-item label="发货仓库">{{ detailData.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="发货日期">{{ detailData.deliveryDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTagType(detailData.status)" size="small">
            {{ detailData.status === 'PENDING' ? '待发货' : detailData.status === 'SHIPPED' ? '已发货' : '已签收' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="3">{{ detailData.shippingAddress }}</el-descriptions-item>
        <el-descriptions-item label="总金额">
          <span style="color: #f56c6c; font-weight: bold;">{{ formatMoney(detailData.totalAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">发货明细</el-divider>
      <el-table :data="detailData.details || []" border size="small" empty-text="暂无明细">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="productCode" label="产品编码" width="130" />
        <el-table-column prop="productName" label="产品名称" width="130" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="actualQty" label="发货数量" width="90" />
        <el-table-column prop="unitPrice" label="单价" width="100" />
        <el-table-column label="金额" width="110">
          <template #default="{ row }">
            {{ formatMoney((row.actualQty || 0) * (row.unitPrice || 0)) }}
          </template>
        </el-table-column>
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
import { salesDelivery } from '@/api/modules/sales'
import { salesOrder } from '@/api/modules/sales'
import { pageCustomer } from '@/api/modules/customer'

const formatMoney = (val) => {
  if (val == null) return '¥0.00'
  return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const statusTagType = (status) => {
  if (status === 'RECEIVED') return 'success'
  if (status === 'SHIPPED') return 'primary'
  return 'warning'
}

// ---------- 搜索 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  deliveryNo: '', soNo: '', customerId: '', status: ''
})

// ---------- 表格 ----------
const tableLoading = ref(false)
const tableData = ref([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const customerOptions = ref([])
const soOptions = ref([])

// ---------- 表单 ----------
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const formData = reactive({
  soId: '', soNo: '', customerId: '', customerName: '',
  warehouseId: '', deliveryDate: '', shippingAddress: '', remark: '', details: []
})

const formRules = {
  soId: [{ required: true, message: '请选择销售订单', trigger: 'change' }],
  warehouseId: [{ required: true, message: '请选择发货仓库', trigger: 'change' }],
  deliveryDate: [{ required: true, message: '请选择发货日期', trigger: 'change' }]
}

const computedDeliveryTotal = computed(() => {
  return (formData.details || []).reduce((sum, d) => sum + (d.actualQty || 0) * (d.unitPrice || 0), 0)
})

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- 加载 ----------
const loadCustomers = async () => {
  try {
    const res = await pageCustomer({ page: 1, pageSize: 999, status: 1 })
    customerOptions.value = res.data?.records || res.records || []
  } catch (e) { /* ignore */ }
}

const loadSOOptions = async () => {
  try {
    const res = await salesOrder.page({ page: 1, pageSize: 999, status: 'CONFIRMED,PARTIAL_SHIPPED' })
    soOptions.value = res.data?.records || res.records || []
  } catch (e) { /* ignore */ }
}

const handleSOChange = async (soId) => {
  const selected = soOptions.value.find(s => s.id === soId)
  if (!selected) return
  formData.soNo = selected.soNo || ''
  formData.customerId = selected.customerId || ''
  formData.customerName = selected.customerName || ''
  try {
    const res = await salesOrder.getById(soId)
    const data = res.data || res
    formData.details = (data.details || []).map(d => ({
      productCode: d.productCode, productName: d.productName,
      orderedQty: d.quantity || 0, shippedQty: d.shippedQty || 0,
      unit: d.unit, actualQty: (d.quantity || 0) - (d.shippedQty || 0), unitPrice: d.unitPrice || 0
    }))
  } catch (e) { ElMessage.error('获取订单明细失败') }
}

// ---------- 列表 ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current, pageSize: pagination.pageSize,
      deliveryNo: searchForm.deliveryNo || undefined,
      soNo: searchForm.soNo || undefined,
      customerId: searchForm.customerId || undefined,
      status: searchForm.status || undefined
    }
    const res = await salesDelivery.page(params)
    tableData.value = res.data?.records || res.records || []
    pagination.total = res.data?.total || res.total || 0
  } catch (error) { ElMessage.error('查询失败') } finally { tableLoading.value = false }
}

const handleSearch = () => { pagination.current = 1; fetchData() }
const handleReset = () => { searchFormRef.value?.resetFields(); pagination.current = 1; fetchData() }
const handleSizeChange = () => { pagination.current = 1; fetchData() }
const handleCurrentChange = () => { fetchData() }

// ---------- 新增 ----------
const resetFormData = () => {
  formData.soId = ''; formData.soNo = ''; formData.customerId = ''; formData.customerName = ''
  formData.warehouseId = ''; formData.deliveryDate = ''; formData.shippingAddress = ''; formData.remark = ''
  formData.details = []
}

const handleAdd = () => { resetFormData(); dialogVisible.value = true }

const handleDialogClosed = () => { resetFormData(); formRef.value?.resetFields() }

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  const validDetails = formData.details.filter(d => d.actualQty > 0)
  if (validDetails.length === 0) { ElMessage.warning('请至少填写一项发货数量'); return }
  submitLoading.value = true
  try {
    await salesDelivery.add({
      ...formData, details: validDetails, totalAmount: computedDeliveryTotal.value
    })
    ElMessage.success('保存成功'); dialogVisible.value = false; fetchData()
  } catch (error) { ElMessage.error('保存失败') } finally { submitLoading.value = false }
}

// ---------- 确认发货 ----------
const handleConfirmDelivery = async (row) => {
  try {
    await ElMessageBox.confirm('确定要确认发货吗？', '提示', { type: 'warning' })
    await salesDelivery.confirm(row.id)
    ElMessage.success('发货确认成功'); fetchData()
  } catch (error) { if (error !== 'cancel') ElMessage.error('确认失败') }
}

// ---------- 查看 ----------
const handleView = async (row) => {
  try {
    const res = await salesDelivery.getById(row.id)
    Object.assign(detailData, res.data || res)
    detailVisible.value = true
  } catch (error) { ElMessage.error('获取详情失败') }
}

const handleExport = () => { ElMessage.info('导出功能开发中') }

onMounted(() => { loadCustomers(); loadSOOptions(); fetchData() })
</script>

<style scoped lang="scss">
.sales-delivery-list-container {
  padding: 16px;
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .action-bar { display: flex; align-items: center; }
  .pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 16px; }
  .dialog-footer { display: flex; justify-content: flex-end; gap: 8px; }
}
</style>
