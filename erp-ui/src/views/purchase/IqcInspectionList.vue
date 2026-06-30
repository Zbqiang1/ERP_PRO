<template>
  <div class="iqc-inspection-list-container">
    <!-- 搜索表单 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline ref="searchFormRef">
        <el-form-item label="检验单号" prop="inspectionNo">
          <el-input v-model="searchForm.inspectionNo" placeholder="请输入检验单号" clearable />
        </el-form-item>
        <el-form-item label="采购单号" prop="poNo">
          <el-input v-model="searchForm.poNo" placeholder="请输入采购单号" clearable />
        </el-form-item>
        <el-form-item label="检验结果" prop="inspectionResult">
          <el-select v-model="searchForm.inspectionResult" placeholder="请选择检验结果" clearable>
            <el-option v-for="(v, k) in resultMap" :key="k" :label="v.label" :value="k" />
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
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增来料检验</el-button>
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
        <el-table-column prop="inspectionNo" label="检验单号" min-width="160" sortable="custom" />
        <el-table-column prop="poNo" label="采购单号" width="160" />
        <el-table-column prop="supplierName" label="供应商" width="180" />
        <el-table-column prop="inspectorName" label="检验员" width="100" />
        <el-table-column prop="inspectionDate" label="检验日期" width="120" sortable="custom" />
        <el-table-column prop="inspectionResult" label="检验结果" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="resultMap[row.inspectionResult]?.type" size="small">
              {{ resultMap[row.inspectionResult]?.label || row.inspectionResult }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qualifiedQty" label="合格数量" width="100" />
        <el-table-column prop="defectQty" label="不良数量" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="170" sortable="custom" />
        <el-table-column label="操作" width="140" fixed="right" align="center">
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

    <!-- 新增检验对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="新增来料检验"
      width="900px"
      :close-on-click-modal="false"
      destroy-on-close
      @closed="handleDialogClosed"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="采购单号" prop="poId">
              <el-select v-model="formData.poId" placeholder="请选择采购单" filterable style="width: 100%" @change="handlePOChange">
                <el-option v-for="po in poOptions" :key="po.id" :label="po.poNo" :value="po.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="检验员" prop="inspectorId">
              <el-input v-model="formData.inspectorName" placeholder="当前用户" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="检验日期" prop="inspectionDate">
              <el-date-picker v-model="formData.inspectionDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="检验结果" prop="inspectionResult">
          <el-radio-group v-model="formData.inspectionResult">
            <el-radio value="QUALIFIED">合格</el-radio>
            <el-radio value="UNQUALIFIED">不合格</el-radio>
            <el-radio value="CONCESSION">让步接收</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="检验备注" />
        </el-form-item>
      </el-form>

      <el-divider content-position="left">检验明细</el-divider>
      <el-table :data="formData.details" border size="small">
        <el-table-column prop="materialCode" label="物料编码" width="130" />
        <el-table-column prop="materialName" label="物料名称" width="130" />
        <el-table-column prop="orderQty" label="订单数量" width="90" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="inspectionQty" label="检验数量" width="110">
          <template #default="{ row }">
            <el-input-number v-model="row.inspectionQty" :min="0" :max="row.orderQty" :precision="0" size="small" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column prop="qualifiedQty" label="合格数量" width="110">
          <template #default="{ row }">
            <el-input-number v-model="row.qualifiedQty" :min="0" :max="row.inspectionQty || 0" :precision="0" size="small" controls-position="right" style="width: 100%" />
          </template>
        </el-table-column>
        <el-table-column label="不良数量" width="90">
          <template #default="{ row }">
            {{ (row.inspectionQty || 0) - (row.qualifiedQty || 0) }}
          </template>
        </el-table-column>
        <el-table-column prop="defectDescription" label="不良描述" min-width="150">
          <template #default="{ row }">
            <el-input v-model="row.defectDescription" placeholder="不良描述" size="small" />
          </template>
        </el-table-column>
      </el-table>

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
      title="来料检验详情"
      width="900px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="检验单号">{{ detailData.inspectionNo }}</el-descriptions-item>
        <el-descriptions-item label="采购单号">{{ detailData.poNo }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ detailData.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="检验员">{{ detailData.inspectorName }}</el-descriptions-item>
        <el-descriptions-item label="检验日期">{{ detailData.inspectionDate }}</el-descriptions-item>
        <el-descriptions-item label="检验结果">
          <el-tag :type="resultMap[detailData.inspectionResult]?.type" size="small">
            {{ resultMap[detailData.inspectionResult]?.label || detailData.inspectionResult }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="合格数量">{{ detailData.qualifiedQty }}</el-descriptions-item>
        <el-descriptions-item label="不良数量">{{ detailData.defectQty }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="3">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">检验明细</el-divider>
      <el-table :data="detailData.details || []" border size="small" :empty-text="'暂无明细'">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="materialCode" label="物料编码" width="130" />
        <el-table-column prop="materialName" label="物料名称" width="130" />
        <el-table-column prop="orderQty" label="订单数量" width="90" />
        <el-table-column prop="unit" label="单位" width="60" />
        <el-table-column prop="inspectionQty" label="检验数量" width="90" />
        <el-table-column prop="qualifiedQty" label="合格数量" width="90" />
        <el-table-column prop="defectQty" label="不良数量" width="90" />
        <el-table-column prop="defectDescription" label="不良描述" min-width="150" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import { iqcInspection } from '@/api/modules/purchase'
import { purchaseOrder } from '@/api/modules/purchase'

// ---------- 结果映射 ----------
const resultMap = {
  QUALIFIED: { label: '合格', type: 'success' },
  UNQUALIFIED: { label: '不合格', type: 'danger' },
  CONCESSION: { label: '让步接收', type: 'warning' }
}

// ---------- 搜索 ----------
const searchFormRef = ref(null)
const searchForm = reactive({
  inspectionNo: '',
  poNo: '',
  inspectionResult: ''
})

// ---------- 表格 ----------
const tableLoading = ref(false)
const tableData = ref([])
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

const poOptions = ref([])

// ---------- 表单 ----------
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const formData = reactive({
  poId: '',
  poNo: '',
  inspectorId: '',
  inspectorName: '',
  inspectionDate: '',
  inspectionResult: 'QUALIFIED',
  remark: '',
  details: []
})

const formRules = {
  poId: [{ required: true, message: '请选择采购单', trigger: 'change' }],
  inspectionDate: [{ required: true, message: '请选择检验日期', trigger: 'change' }],
  inspectionResult: [{ required: true, message: '请选择检验结果', trigger: 'change' }]
}

// ---------- 详情 ----------
const detailVisible = ref(false)
const detailData = reactive({})

// ---------- 列表加载 ----------
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: pagination.current,
      pageSize: pagination.pageSize,
      inspectionNo: searchForm.inspectionNo || undefined,
      poNo: searchForm.poNo || undefined,
      inspectionResult: searchForm.inspectionResult || undefined
    }
    const res = await iqcInspection.page(params)
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

// ---------- 加载已确认的采购单 ----------
const loadPOOptions = async () => {
  try {
    const res = await purchaseOrder.page({ page: 1, pageSize: 999, status: 'CONFIRMED,PARTIAL_RECEIVED' })
    poOptions.value = res.data?.records || res.records || []
  } catch (e) { /* ignore */ }
}

// ---------- 选择采购单 ----------
const handlePOChange = async (poId) => {
  const selected = poOptions.value.find(p => p.id === poId)
  if (!selected) return
  formData.poNo = selected.poNo
  try {
    const res = await purchaseOrder.getById(poId)
    const data = res.data || res
    formData.details = (data.details || []).map(d => ({
      materialCode: d.materialCode,
      materialName: d.materialName,
      orderQty: d.quantity,
      unit: d.unit,
      inspectionQty: d.quantity,
      qualifiedQty: d.quantity,
      defectDescription: ''
    }))
  } catch (e) {
    ElMessage.error('获取采购单明细失败')
  }
}

// ---------- 新增 ----------
const handleAdd = () => {
  resetFormData()
  dialogVisible.value = true
}

const resetFormData = () => {
  formData.poId = ''
  formData.poNo = ''
  formData.inspectorId = ''
  formData.inspectorName = ''
  formData.inspectionDate = ''
  formData.inspectionResult = 'QUALIFIED'
  formData.remark = ''
  formData.details = []
}

const handleDialogClosed = () => {
  resetFormData()
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (formData.details.length === 0) {
    ElMessage.warning('检验明细不能为空')
    return
  }
  const qualifiedQty = formData.details.reduce((s, d) => s + (d.qualifiedQty || 0), 0)
  const totalQty = formData.details.reduce((s, d) => s + (d.inspectionQty || 0), 0)

  submitLoading.value = true
  try {
    await iqcInspection.add({
      ...formData,
      qualifiedQty,
      defectQty: totalQty - qualifiedQty
    })
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('保存失败')
  } finally {
    submitLoading.value = false
  }
}

// ---------- 查看 ----------
const handleView = async (row) => {
  try {
    const res = await iqcInspection.getById(row.id)
    Object.assign(detailData, res.data || res)
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  }
}

const handleExport = () => {
  ElMessage.info('导出功能开发中')
}

onMounted(() => {
  loadPOOptions()
  fetchData()
})
</script>

<style scoped lang="scss">
.iqc-inspection-list-container {
  padding: 16px;

  .search-card,
  .action-card,
  .table-card {
    margin-bottom: 16px;
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
