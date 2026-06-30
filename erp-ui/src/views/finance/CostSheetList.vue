<template>
  <div class="cost-sheet-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="成本单号">
          <el-input v-model="searchForm.costNo" placeholder="请输入成本单号" clearable />
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="searchForm.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="成本期间">
          <el-date-picker v-model="searchForm.costPeriod" type="month" placeholder="选择月份" value-format="YYYY-MM" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增成本单</el-button>
        <el-button type="success" icon="Download" @click="handleExport">导出</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="costNo" label="成本单号" width="150" />
        <el-table-column prop="productName" label="产品名称" width="150" />
        <el-table-column prop="materialCost" label="物料成本" width="120" align="right">
          <template #default="{ row }">{{ row.materialCost?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="laborCost" label="人工成本" width="120" align="right">
          <template #default="{ row }">{{ row.laborCost?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="overheadCost" label="制造费用" width="120" align="right">
          <template #default="{ row }">{{ row.overheadCost?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="totalCost" label="总成本" width="120" align="right">
          <template #default="{ row }">
            <strong>{{ row.totalCost?.toFixed(2) }}</strong>
          </template>
        </el-table-column>
        <el-table-column prop="costPeriod" label="成本期间" width="120" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
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

    <el-dialog v-model="detailVisible" title="成本单详情" width="750px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="成本单号">{{ detailData.costNo }}</el-descriptions-item>
          <el-descriptions-item label="产品名称">{{ detailData.productName }}</el-descriptions-item>
          <el-descriptions-item label="成本期间">{{ detailData.costPeriod }}</el-descriptions-item>
          <el-descriptions-item label="物料成本">{{ detailData.materialCost?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="人工成本">{{ detailData.laborCost?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="制造费用">{{ detailData.overheadCost?.toFixed(2) }}</el-descriptions-item>
          <el-descriptions-item label="总成本"><strong>{{ detailData.totalCost?.toFixed(2) }}</strong></el-descriptions-item>
        </el-descriptions>
      </template>
    </el-dialog>

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { costSheetPage, costSheetAdd, costSheetUpdate, costSheetDelete, costSheetGetById } from '@/api/modules/finance'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ costNo: '', productName: '', costPeriod: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await costSheetPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取成本单列表失败') } finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { costNo: '', productName: '', costPeriod: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增成本单'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑成本单'
  try { const res = await costSheetGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleDetail = async (row) => {
  try { const res = await costSheetGetById(row.id); detailData.value = res.code === 200 ? res.data : row } catch { detailData.value = row }
  detailVisible.value = true
}
const handleExport = () => { ElMessage.success('导出功能开发中') }
const handleDelete = async (row) => {
  try {
    const res = await costSheetDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? costSheetAdd : costSheetUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => fetchData())
</script>

<style scoped>
.cost-sheet-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
