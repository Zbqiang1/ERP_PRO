<template>
  <div class="prod-inspection-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="检验单号">
          <el-input v-model="searchForm.inspectionNo" placeholder="请输入检验单号" clearable />
        </el-form-item>
        <el-form-item label="工单号">
          <el-input v-model="searchForm.woNo" placeholder="请输入工单号" clearable />
        </el-form-item>
        <el-form-item label="检验类型">
          <el-select v-model="searchForm.inspectionType" placeholder="请选择" clearable>
            <el-option label="首检" value="FIRST_CHECK" />
            <el-option label="巡检" value="PATROL_CHECK" />
            <el-option label="终检" value="FINAL_CHECK" />
          </el-select>
        </el-form-item>
        <el-form-item label="检验结果">
          <el-select v-model="searchForm.inspectionResult" placeholder="请选择" clearable>
            <el-option label="合格" value="PASS" />
            <el-option label="不合格" value="FAIL" />
            <el-option label="让步接收" value="CONCESSION" />
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
        <el-button type="primary" icon="Plus" @click="handleAdd">新增检验单</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="inspectionNo" label="检验单号" width="150" />
        <el-table-column prop="woNo" label="工单号" width="150" />
        <el-table-column prop="inspectorName" label="检验员" width="100" />
        <el-table-column prop="inspectionDate" label="检验日期" width="120" />
        <el-table-column prop="inspectionType" label="检验类型" width="100">
          <template #default="{ row }">{{ typeLabel(row.inspectionType) }}</template>
        </el-table-column>
        <el-table-column prop="inspectionResult" label="检验结果" width="110">
          <template #default="{ row }">
            <el-tag :type="resultTag(row.inspectionResult)">{{ resultLabel(row.inspectionResult) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qualifiedQty" label="合格数量" width="100" align="right" />
        <el-table-column prop="defectQty" label="缺陷数量" width="100" align="right" />
        <el-table-column label="操作" width="180" fixed="right">
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

    <ProdInspectionFormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" @confirm="handleSave"
    />

    <el-dialog v-model="detailVisible" title="检验单详情" width="700px" destroy-on-close>
      <template v-if="detailData">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="检验单号">{{ detailData.inspectionNo }}</el-descriptions-item>
          <el-descriptions-item label="工单号">{{ detailData.woNo }}</el-descriptions-item>
          <el-descriptions-item label="检验员">{{ detailData.inspectorName }}</el-descriptions-item>
          <el-descriptions-item label="检验日期">{{ detailData.inspectionDate }}</el-descriptions-item>
          <el-descriptions-item label="检验类型">{{ typeLabel(detailData.inspectionType) }}</el-descriptions-item>
          <el-descriptions-item label="检验结果">
            <el-tag :type="resultTag(detailData.inspectionResult)">{{ resultLabel(detailData.inspectionResult) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="合格数量">{{ detailData.qualifiedQty }}</el-descriptions-item>
          <el-descriptions-item label="缺陷数量">{{ detailData.defectQty }}</el-descriptions-item>
          <el-descriptions-item label="缺陷描述">{{ detailData.defectDesc }}</el-descriptions-item>
        </el-descriptions>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { prodInspectionPage, prodInspectionAdd, prodInspectionUpdate, prodInspectionDelete, prodInspectionGetById } from '@/api/modules/production'
import ProdInspectionFormDialog from './components/ProdInspectionFormDialog.vue'

const loading = ref(false)
const searchForm = ref({ inspectionNo: '', woNo: '', inspectionType: '', inspectionResult: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)
const detailVisible = ref(false); const detailData = ref(null)

const typeMap = { FIRST_CHECK: '首检', PATROL_CHECK: '巡检', FINAL_CHECK: '终检' }
const resultMap = { PASS: '合格', FAIL: '不合格', CONCESSION: '让步接收' }
const resultTagMap = { PASS: 'success', FAIL: 'danger', CONCESSION: 'warning' }
const typeLabel = (t) => typeMap[t] || t
const resultLabel = (r) => resultMap[r] || r
const resultTag = (r) => resultTagMap[r] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await prodInspectionPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取检验单列表失败') } finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { inspectionNo: '', woNo: '', inspectionType: '', inspectionResult: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增检验单'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑检验单'
  try { const res = await prodInspectionGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleDetail = async (row) => {
  try { const res = await prodInspectionGetById(row.id); detailData.value = res.code === 200 ? res.data : row } catch { detailData.value = row }
  detailVisible.value = true
}
const handleDelete = async (row) => {
  try {
    const res = await prodInspectionDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? prodInspectionAdd : prodInspectionUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => fetchData())
</script>

<style scoped>
.prod-inspection-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
