<template>
  <div class="sales-return-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline ref="searchFormRef">
        <el-form-item label="退货单号" prop="returnNo">
          <el-input v-model="searchForm.returnNo" placeholder="请输入退货单号" clearable />
        </el-form-item>
        <el-form-item label="销售单号" prop="soNo">
          <el-input v-model="searchForm.soNo" placeholder="请输入销售单号" clearable />
        </el-form-item>
        <el-form-item label="退货日期" prop="returnDateRange">
          <el-date-picker
            v-model="searchForm.returnDateRange"
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
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增退货单</el-button>
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
        <el-table-column prop="returnNo" label="退货单号" min-width="160" sortable="custom" />
        <el-table-column prop="soNo" label="销售单号" width="160" />
        <el-table-column prop="customerName" label="客户名称" width="180" />
        <el-table-column prop="returnDate" label="退货日期" width="120" sortable="custom" />
        <el-table-column prop="totalAmount" label="总金额" width="140" sortable="custom">
          <template #default="{ row }">
            {{ formatMoney(row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="returnReason" label="退货原因" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'COMPLETED' ? 'success' : 'warning'" size="small">
              {{ row.status === 'COMPLETED' ? '已完成' : '处理中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" sortable="custom" />
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看详情</el-button>
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

    <!-- 新增退货单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="新增退货单"
      width="950px"
      :close-on-click-modal="false"
      destroy-on-close
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="销售订单" prop="soId">
              <el-select v-model="formData.soId" placeholder="请选择销售订单" filterable style="width: 100%" @change="handleSOChange">
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
            <el-form-item label="退货日期" prop="returnDate">
              <el-date-picker v-model="formData.returnDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="退货原因" prop="returnReason">
          <el-input v-model="formData.returnReason" type="textarea" :rows="2" placeholder="请输入退货原因" />
        </el-form-item>
      </el-form>

      <el-divider content-position="left">退货明细</el-divider>
      <el-table :data="formData.details" border size="small">
        <el-table-column prop="productCode" label="产品编码" width="130" />
        <el-table-column prop="productName" label="产品名称" width="130" />
        <el-table-column prop="shippedQty" label="已发数量" width="90" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="returnQty" label="退货数量" width="110">
          <template #default="{ row }">
            <el-input-number v-model="row.returnQty" :min="0" :max="row.shippedQty" size="small" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column prop="unitPrice" label="单价" width="100" />
        <el-table-column label="退货金额" width="110">
          <template #default="{ row }">
            {{ ((row.returnQty || 0) * (row.unitPrice || 0)).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120">
          <template #default="{ row }">
            <el-input v-model="row.remark" placeholder="备注" size="small" />
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 12px; text-align: right;">
        <span style="font-weight: bold;">退货总金额：</span>
        <span style="color: #f56c6c; font-size: 16px;">{{ computedReturnTotal.toFixed(2) }}</span>
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
      title="退货单详情"
      width="950px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="退货单号">{{ detailData.returnNo }}</el-descriptions-item>
        <el-descriptions-item label="销售单号">{{ detailData.soNo }}</el-descriptions-item>
        <el-descriptions-item label="客户">{{ detailData.customerName }}</el-descriptions-item>
        <el-descriptions-item label="退货日期">{{ detailData.returnDate }}</el-descriptions-item>
        <el-descriptions-item label="退货总金额">
          <span style="color: #f56c6c; font-weight: bold;">{{ formatMoney(detailData.totalAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === 'COMPLETED' ? 'success' : 'warning'" size="small">
            {{ detailData.status === 'COMPLETED' ? '已完成' : '处理中' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="退货原因" :span="3">{{ detailData.returnReason }}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">退货明细</el-divider>
      <el-table :data="detailData.details || []" border size="small" empty-text="暂无明细">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="productCode" label="产品编码" width="130" />
        <el-table-column prop="productName" label="产品名称" width="130" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="returnQty" label="退货数量" width="90" />
        <el-table-column prop="unitPrice" label="单价" width="100" />
        <el-table-column label="退货金额" width="110">
          <template #default="{ row }">
            {{ formatMoney((row.returnQty || 0) * (row.unitPrice || 0)) }}
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
import { ElMessage } from 'element-plus'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import { salesReturn } from '@/api/modules/sales'
import { salesOrder } from '@/api/modules/sales'

const formatMoney = (val) => {
  if (val == null) return '¥0.00'
  return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// ---------- 搜索 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  returnNo: '', soNo: '', returnDateRange: []
})

// ---------- 表格 ----------
const tableLoading = ref(false)
const tableData = ref([])
const pagination = reactive({ current: 1, pageSize: 10, total: 0 })
const soOptions = ref([])

// ---------- 表单 ----------
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const formData = reactive({
  soId: '', soNo: '', customerId: '', customerName: '',
  returnDate: '', returnReason: '', details: []
})

const formRules = {
  soId: [{ required: true, message: '请选择销售订单', trigger: 'change' }],
  returnDate: [{ required: true, message: '请选择退货日期', trigger: 'change' }],
  returnReason: [{ required: true, message: '请输入退货原因', trigger: 'blur' }]
}

const computedReturnTotal = computed(() => {
  return (formData.details || []).reduce((sum, d) => sum + (d.returnQty || 0) * (d.unitPrice || 0), 0)
})

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- 加载销售订单 ----------
const loadSOOptions = async () => {
  try {
    const res = await salesOrder.page({ page: 1, pageSize: 999, status: 'CONFIRMED,PARTIAL_SHIPPED,COMPLETED' })
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
      shippedQty: d.shippedQty || d.quantity || 0,
      unit: d.unit, returnQty: 0, unitPrice: d.unitPrice || 0, remark: ''
    }))
  } catch (e) { ElMessage.error('获取订单明细失败') }
}

// ---------- 列表 ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current, pageSize: pagination.pageSize,
      returnNo: searchForm.returnNo || undefined,
      soNo: searchForm.soNo || undefined,
      returnDateStart: searchForm.returnDateRange?.[0] || undefined,
      returnDateEnd: searchForm.returnDateRange?.[1] || undefined
    }
    const res = await salesReturn.page(params)
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
  formData.returnDate = ''; formData.returnReason = ''; formData.details = []
}

const handleAdd = () => { resetFormData(); dialogVisible.value = true }

const handleDialogClosed = () => { resetFormData(); formRef.value?.resetFields() }

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  const validDetails = formData.details.filter(d => d.returnQty > 0)
  if (validDetails.length === 0) { ElMessage.warning('请至少填写一项退货数量'); return }
  submitLoading.value = true
  try {
    await salesReturn.add({
      ...formData, details: validDetails, totalAmount: computedReturnTotal.value
    })
    ElMessage.success('保存成功'); dialogVisible.value = false; fetchData()
  } catch (error) { ElMessage.error('保存失败') } finally { submitLoading.value = false }
}

// ---------- 查看 ----------
const handleView = async (row) => {
  try {
    const res = await salesReturn.getById(row.id)
    Object.assign(detailData, res.data || res)
    detailVisible.value = true
  } catch (error) { ElMessage.error('获取详情失败') }
}

const handleExport = () => { ElMessage.info('导出功能开发中') }

onMounted(() => { loadSOOptions(); fetchData() })
</script>

<style scoped lang="scss">
.sales-return-list-container {
  padding: 16px;
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .action-bar { display: flex; align-items: center; }
  .pagination-wrapper { display: flex; justify-content: flex-end; margin-top: 16px; }
  .dialog-footer { display: flex; justify-content: flex-end; gap: 8px; }
}
</style>
