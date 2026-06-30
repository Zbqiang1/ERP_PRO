<template>
  <div class="bom-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="BOM编号">
          <el-input v-model="searchForm.bomNo" placeholder="请输入BOM编号" clearable />
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="searchForm.productName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已停用" value="DISABLED" />
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增BOM</el-button>
      </div>
      <el-table
        v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full"
        row-key="id" @expand-change="handleExpandChange"
      >
        <el-table-column type="expand">
          <template #default="{ row }">
            <el-table :data="expandData[row.id] || []" border stripe size="small" class="w-full" empty-text="暂无物料明细">
              <el-table-column prop="materialName" label="物料名称" width="150" />
              <el-table-column prop="quantity" label="数量" width="100" align="right" />
              <el-table-column prop="unit" label="单位" width="80" />
              <el-table-column prop="scrapRate" label="损耗率(%)" width="100" align="right">
                <template #default="{ row: r }">{{ r.scrapRate ? (r.scrapRate * 100).toFixed(2) + '%' : '0%' }}</template>
              </el-table-column>
              <el-table-column prop="sortOrder" label="排序" width="80" align="right" />
            </el-table>
          </template>
        </el-table-column>
        <el-table-column prop="bomNo" label="BOM编号" width="130" />
        <el-table-column prop="productName" label="产品名称" width="150" />
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="expireDate" label="失效日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link size="small" @click="handleDetail(row)">查看明细</el-button>
            <el-popconfirm title="确定删除该BOM？" @confirm="handleDelete(row)">
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

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" @confirm="handleSave"
    />

    <el-dialog v-model="detailVisible" title="BOM详情" width="900px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="BOM编号">{{ detailData.bomNo }}</el-descriptions-item>
          <el-descriptions-item label="产品名称">{{ detailData.productName }}</el-descriptions-item>
          <el-descriptions-item label="版本">{{ detailData.version }}</el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ detailData.effectiveDate }}</el-descriptions-item>
          <el-descriptions-item label="失效日期">{{ detailData.expireDate }}</el-descriptions-item>
          <el-descriptions-item label="状态"><el-tag :type="statusTagType(detailData.status)">{{ statusLabel(detailData.status) }}</el-tag></el-descriptions-item>
        </el-descriptions>
        <h4 class="mt-16 mb-8">物料明细</h4>
        <el-table :data="detailData.items || detailData.bomDetails || []" border stripe size="small" class="w-full">
          <el-table-column prop="materialName" label="物料" width="150" />
          <el-table-column prop="quantity" label="数量" width="100" align="right" />
          <el-table-column prop="unit" label="单位" width="80" />
          <el-table-column prop="scrapRate" label="损耗率(%)" width="110" align="right">
            <template #default="{ row }">{{ row.scrapRate ? (row.scrapRate * 100).toFixed(2) + '%' : '0%' }}</template>
          </el-table-column>
          <el-table-column prop="sortOrder" label="排序" width="80" align="right" />
        </el-table>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { bomHeaderPage, bomHeaderAdd, bomHeaderUpdate, bomHeaderDelete, bomHeaderGetById, bomDetailListByHeaderId } from '@/api/modules/production'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ bomNo: '', productName: '', status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const expandData = ref({})

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const statusMap = { DRAFT: '草稿', PUBLISHED: '已发布', DISABLED: '已停用' }
const statusTagMap = { DRAFT: 'info', PUBLISHED: 'success', DISABLED: 'danger' }
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await bomHeaderPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取BOM列表失败') } finally { loading.value = false }
}

const handleExpandChange = async (row, expandedRows) => {
  if (expandedRows.includes(row)) {
    try {
      const res = await bomDetailListByHeaderId(row.id)
      if (res.code === 200) { expandData.value[row.id] = res.data || [] }
    } catch { expandData.value[row.id] = [] }
  }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { bomNo: '', productName: '', status: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增BOM'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑BOM'
  try { const res = await bomHeaderGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleDetail = async (row) => {
  try {
    const res = await bomHeaderGetById(row.id)
    const data = res.code === 200 ? res.data : row
    const itemsRes = await bomDetailListByHeaderId(row.id)
    data.items = itemsRes.code === 200 ? (itemsRes.data || []) : []
    detailData.value = data
  } catch { detailData.value = row }
  detailVisible.value = true
}
const handleDelete = async (row) => {
  try {
    const res = await bomHeaderDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}

const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? bomHeaderAdd : bomHeaderUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => fetchData())
</script>

<style scoped>
.bom-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; } .mb-8 { margin-bottom: 8px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
