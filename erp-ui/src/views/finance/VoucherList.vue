<template>
  <div class="voucher-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="凭证号">
          <el-input v-model="searchForm.voucherNo" placeholder="请输入凭证号" clearable />
        </el-form-item>
        <el-form-item label="凭证类型">
          <el-select v-model="searchForm.voucherType" placeholder="请选择类型" clearable>
            <el-option label="记账" value="JOURNAL" />
            <el-option label="转账" value="TRANSFER" />
            <el-option label="调整" value="ADJUST" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已审核" value="AUDITED" />
            <el-option label="已过账" value="POSTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker v-model="searchForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增凭证</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="voucherNo" label="凭证号" width="150" />
        <el-table-column prop="voucherDate" label="凭证日期" width="120" />
        <el-table-column prop="voucherType" label="凭证类型" width="100">
          <template #default="{ row }">{{ typeLabel(row.voucherType) }}</template>
        </el-table-column>
        <el-table-column prop="totalDebit" label="借方合计" width="130" align="right">
          <template #default="{ row }">{{ row.totalDebit?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="totalCredit" label="贷方合计" width="130" align="right">
          <template #default="{ row }">{{ row.totalCredit?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 'DRAFT'" type="success" link size="small" @click="handleAudit(row)">审核</el-button>
            <el-button v-if="row.status === 'AUDITED'" type="warning" link size="small" @click="handlePost(row)">过账</el-button>
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

    <el-dialog v-model="detailVisible" title="凭证详情" width="900px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="凭证号">{{ detailData.voucherNo }}</el-descriptions-item>
          <el-descriptions-item label="凭证日期">{{ detailData.voucherDate }}</el-descriptions-item>
          <el-descriptions-item label="凭证类型">{{ typeLabel(detailData.voucherType) }}</el-descriptions-item>
          <el-descriptions-item label="借方合计">{{ detailData.totalDebit?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="贷方合计">{{ detailData.totalCredit?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="状态"><el-tag :type="statusTagType(detailData.status)">{{ statusLabel(detailData.status) }}</el-tag></el-descriptions-item>
        </el-descriptions>
        <h4 class="mt-16 mb-8">分录明细</h4>
        <el-table :data="detailData.entries || []" border stripe size="small" class="w-full">
          <el-table-column prop="subjectName" label="科目" width="150" />
          <el-table-column prop="summary" label="摘要" min-width="200" />
          <el-table-column prop="debitAmount" label="借方金额" width="130" align="right">
            <template #default="{ row }">{{ row.debitAmount?.toFixed(2) || '0.00' }}</template>
          </el-table-column>
          <el-table-column prop="creditAmount" label="贷方金额" width="130" align="right">
            <template #default="{ row }">{{ row.creditAmount?.toFixed(2) || '0.00' }}</template>
          </el-table-column>
        </el-table>
      </template>
    </el-dialog>

    <VoucherFormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { voucherPage, voucherAdd, voucherUpdate, voucherDelete, voucherGetById, voucherAudit, voucherPost } from '@/api/modules/finance'
import VoucherFormDialog from './components/VoucherFormDialog.vue'

const loading = ref(false)
const searchForm = ref({ voucherNo: '', voucherType: '', status: '', dateRange: [] })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const typeMap = { JOURNAL: '记账', TRANSFER: '转账', ADJUST: '调整' }
const statusMap = { DRAFT: '草稿', AUDITED: '已审核', POSTED: '已过账' }
const statusTagMap = { DRAFT: 'info', AUDITED: 'success', POSTED: 'primary' }
const typeLabel = (t) => typeMap[t] || t
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    if (params.dateRange?.length === 2) { params.startDate = params.dateRange[0]; params.endDate = params.dateRange[1] }
    delete params.dateRange
    const res = await voucherPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取凭证列表失败') } finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { voucherNo: '', voucherType: '', status: '', dateRange: [] }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增凭证'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑凭证'
  try { const res = await voucherGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleDetail = async (row) => {
  try { const res = await voucherGetById(row.id); detailData.value = res.code === 200 ? res.data : row } catch { detailData.value = row }
  detailVisible.value = true
}
const handleAudit = async (row) => {
  try {
    await ElMessageBox.confirm('确定审核该凭证？', '审核凭证', { type: 'info' })
    const res = await voucherAudit(row.id)
    res.code === 200 ? (ElMessage.success('审核成功'), fetchData()) : ElMessage.error(res.msg || '审核失败')
  } catch { /* */ }
}
const handlePost = async (row) => {
  try {
    await ElMessageBox.confirm('确定过账该凭证？', '过账凭证', { type: 'warning' })
    const res = await voucherPost(row.id)
    res.code === 200 ? (ElMessage.success('过账成功'), fetchData()) : ElMessage.error(res.msg || '过账失败')
  } catch { /* */ }
}
const handleDelete = async (row) => {
  try {
    const res = await voucherDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? voucherAdd : voucherUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => fetchData())
</script>

<style scoped>
.voucher-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; } .mb-8 { margin-bottom: 8px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
