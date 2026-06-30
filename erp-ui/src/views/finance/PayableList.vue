<template>
  <div class="payable-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="应付单号">
          <el-input v-model="searchForm.payableNo" placeholder="请输入应付单号" clearable />
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="searchForm.supplierId" placeholder="请选择供应商" clearable filterable>
            <el-option v-for="s in supplierOptions" :key="s.id" :label="s.supplierName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="未付" value="UNPAID" />
            <el-option label="部分付款" value="PARTIAL" />
            <el-option label="已付" value="PAID" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增应付款</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full" show-summary :summary-method="getSummaries">
        <el-table-column prop="payableNo" label="应付单号" width="150" />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="amount" label="应付金额" width="120" align="right">
          <template #default="{ row }">{{ row.amount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120" align="right">
          <template #default="{ row }">{{ row.paidAmount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="120" align="right">
          <template #default="{ row }">
            <span :style="{ color: (row.balance || 0) > 0 ? '#f56c6c' : '' }">{{ row.balance?.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="dueDate" label="到期日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status !== 'PAID'" type="success" link size="small" @click="handlePay(row)">付款</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="flex justify-end mt-16">
        <el-pagination
          v-model:current-page="pagination.pageNo" v-model:page-size="pagination.pageSize"
          :total="pagination.total" :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData"
        />
      </div>
    </el-card>

    <el-dialog v-model="payVisible" title="付款" width="500px" destroy-on-close>
      <el-form ref="payFormRef" :model="payForm" :rules="payRules" label-width="100px">
        <el-form-item label="应付单号">{{ payRow?.payableNo }}</el-form-item>
        <el-form-item label="供应商">{{ payRow?.supplierName }}</el-form-item>
        <el-form-item label="余额">{{ payRow?.balance?.toFixed(2) }}</el-form-item>
        <el-form-item label="付款金额" prop="amount">
          <el-input-number v-model="payForm.amount" :min="0.01" :max="payRow?.balance" :precision="2" class="w-full" />
        </el-form-item>
        <el-form-item label="付款方式" prop="paymentMethod">
          <el-select v-model="payForm.paymentMethod" placeholder="请选择" class="w-full">
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="现金" value="CASH" />
            <el-option label="支票" value="CHEQUE" />
          </el-select>
        </el-form-item>
        <el-form-item label="参考号" prop="referenceNo">
          <el-input v-model="payForm.referenceNo" placeholder="请输入参考号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="payVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePaySubmit">确定付款</el-button>
      </template>
    </el-dialog>

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :supplier-options="supplierOptions" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { payablePage, payableAdd, payableUpdate, payableDelete, payableGetById, payablePay } from '@/api/modules/finance'
import { supplierListAll } from '@/api/modules/supplier'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ payableNo: '', supplierId: null, status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const supplierOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const payVisible = ref(false); const payRow = ref(null)
const payFormRef = ref(null)
const payForm = reactive({ amount: 0, paymentMethod: '', referenceNo: '' })
const payRules = {
  amount: [{ required: true, message: '请输入付款金额', trigger: 'blur' }],
  paymentMethod: [{ required: true, message: '请选择付款方式', trigger: 'change' }]
}

const statusMap = { UNPAID: '未付', PARTIAL: '部分付款', PAID: '已付' }
const statusTagMap = { UNPAID: 'danger', PARTIAL: 'warning', PAID: 'success' }
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const getSummaries = (param) => {
  const { columns, data } = param
  const sums = []
  columns.forEach((col, index) => {
    if (index === 0) { sums[index] = '合计'; return }
    if (['amount', 'paidAmount', 'balance'].includes(col.property)) {
      sums[index] = data.reduce((prev, cur) => prev + (cur[col.property] || 0), 0).toFixed(2)
    }
  })
  return sums
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await payablePage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取应付款列表失败') } finally { loading.value = false }
}

const fetchSuppliers = async () => {
  try { const res = await supplierListAll(); supplierOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { payableNo: '', supplierId: null, status: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增应付款'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑应付款'
  try { const res = await payableGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handlePay = (row) => {
  payRow.value = row
  payForm.amount = row.balance || 0
  payForm.paymentMethod = ''
  payForm.referenceNo = ''
  payVisible.value = true
}
const handlePaySubmit = async () => {
  const valid = await payFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    const res = await payablePay({ id: payRow.value.id, ...payForm })
    res.code === 200 ? (ElMessage.success('付款成功'), payVisible.value = false, fetchData()) : ElMessage.error(res.msg || '付款失败')
  } catch { ElMessage.error('付款失败') }
}
const handleDelete = async (row) => {
  try {
    const res = await payableDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? payableAdd : payableUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchSuppliers(); fetchData() })
</script>

<style scoped>
.payable-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
