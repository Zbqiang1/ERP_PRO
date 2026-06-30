<template>
  <div class="receivable-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="应收单号">
          <el-input v-model="searchForm.receivableNo" placeholder="请输入应收单号" clearable />
        </el-form-item>
        <el-form-item label="客户">
          <el-select v-model="searchForm.customerId" placeholder="请选择客户" clearable filterable>
            <el-option v-for="c in customerOptions" :key="c.id" :label="c.customerName" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="未收" value="UNPAID" />
            <el-option label="部分收款" value="PARTIAL" />
            <el-option label="已收" value="PAID" />
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增应收款</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full" show-summary :summary-method="getSummaries">
        <el-table-column prop="receivableNo" label="应收单号" width="150" />
        <el-table-column prop="customerName" label="客户名称" width="150" />
        <el-table-column prop="amount" label="应收金额" width="120" align="right">
          <template #default="{ row }">{{ row.amount?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="receivedAmount" label="已收金额" width="120" align="right">
          <template #default="{ row }">{{ row.receivedAmount?.toFixed(2) }}</template>
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
        <el-table-column label="操作" width="240" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status !== 'PAID'" type="success" link size="small" @click="handleCollect(row)">收款</el-button>
            <el-button type="primary" link size="small" @click="handleLog(row)">日志</el-button>
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

    <el-dialog v-model="collectVisible" title="收款" width="500px" destroy-on-close>
      <el-form ref="collectFormRef" :model="collectForm" :rules="collectRules" label-width="100px">
        <el-form-item label="应收单号">{{ collectRow?.receivableNo }}</el-form-item>
        <el-form-item label="客户">{{ collectRow?.customerName }}</el-form-item>
        <el-form-item label="余额">{{ collectRow?.balance?.toFixed(2) }}</el-form-item>
        <el-form-item label="收款金额" prop="amount">
          <el-input-number v-model="collectForm.amount" :min="0.01" :max="collectRow?.balance" :precision="2" class="w-full" />
        </el-form-item>
        <el-form-item label="付款方式" prop="paymentMethod">
          <el-select v-model="collectForm.paymentMethod" placeholder="请选择" class="w-full">
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="现金" value="CASH" />
            <el-option label="支票" value="CHEQUE" />
            <el-option label="承兑汇票" value="ACCEPTANCE_BILL" />
          </el-select>
        </el-form-item>
        <el-form-item label="参考号" prop="referenceNo">
          <el-input v-model="collectForm.referenceNo" placeholder="请输入参考号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="collectVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCollectSubmit">确定收款</el-button>
      </template>
    </el-dialog>

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" :customer-options="customerOptions" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { receivablePage, receivableAdd, receivableUpdate, receivableDelete, receivableGetById, receivableCollect } from '@/api/modules/finance'
import { customerListAll } from '@/api/modules/customer'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ receivableNo: '', customerId: null, status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const customerOptions = ref([])

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const collectVisible = ref(false); const collectRow = ref(null)
const collectFormRef = ref(null)
const collectForm = reactive({ amount: 0, paymentMethod: '', referenceNo: '' })
const collectRules = {
  amount: [{ required: true, message: '请输入收款金额', trigger: 'blur' }],
  paymentMethod: [{ required: true, message: '请选择付款方式', trigger: 'change' }]
}

const statusMap = { UNPAID: '未收', PARTIAL: '部分收款', PAID: '已收' }
const statusTagMap = { UNPAID: 'danger', PARTIAL: 'warning', PAID: 'success' }
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const getSummaries = (param) => {
  const { columns, data } = param
  const sums = []
  columns.forEach((col, index) => {
    if (index === 0) { sums[index] = '合计'; return }
    if (col.property === 'amount' || col.property === 'receivedAmount' || col.property === 'balance') {
      sums[index] = data.reduce((prev, cur) => prev + (cur[col.property] || 0), 0).toFixed(2)
    }
  })
  return sums
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await receivablePage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取应收款列表失败') } finally { loading.value = false }
}

const fetchCustomers = async () => {
  try { const res = await customerListAll(); customerOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { receivableNo: '', customerId: null, status: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增应收款'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑应收款'
  try { const res = await receivableGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleCollect = (row) => {
  collectRow.value = row
  collectForm.amount = row.balance || 0
  collectForm.paymentMethod = ''
  collectForm.referenceNo = ''
  collectVisible.value = true
}
const handleLog = (row) => { ElMessage.info('收款日志功能开发中') }

const handleCollectSubmit = async () => {
  const valid = await collectFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    const res = await receivableCollect({ id: collectRow.value.id, ...collectForm })
    res.code === 200 ? (ElMessage.success('收款成功'), collectVisible.value = false, fetchData()) : ElMessage.error(res.msg || '收款失败')
  } catch { ElMessage.error('收款失败') }
}

const handleDelete = async (row) => {
  try {
    const res = await receivableDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? receivableAdd : receivableUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => { fetchCustomers(); fetchData() })
</script>

<style scoped>
.receivable-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
