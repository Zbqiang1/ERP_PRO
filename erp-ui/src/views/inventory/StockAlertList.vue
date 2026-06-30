<template>
  <div class="stock-alert-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="预警类型">
          <el-select v-model="searchForm.alertType" placeholder="请选择预警类型" clearable>
            <el-option label="低于安全库存" value="LOW_STOCK" />
            <el-option label="高于最大库存" value="HIGH_STOCK" />
            <el-option label="呆滞物料" value="SLOW_MOVING" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择仓库" clearable filterable>
            <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="未处理" value="PENDING" />
            <el-option label="已处理" value="HANDLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="alertNo" label="预警编号" width="150" />
        <el-table-column prop="materialName" label="物料" width="150" />
        <el-table-column prop="warehouseName" label="仓库" width="150" />
        <el-table-column prop="currentStock" label="当前库存" width="100" align="right" />
        <el-table-column prop="safetyStock" label="安全库存" width="100" align="right" />
        <el-table-column prop="alertType" label="预警类型" width="130">
          <template #default="{ row }">
            <el-tag :type="alertTypeTag(row.alertType)">{{ alertTypeLabel(row.alertType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="alertDate" label="预警日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'HANDLED' ? 'success' : 'danger'">
              {{ row.status === 'HANDLED' ? '已处理' : '未处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 'PENDING'" type="primary" link size="small" @click="handleHandle(row)">处理</el-button>
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
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

    <el-dialog v-model="handleVisible" title="处理预警" width="500px" destroy-on-close @close="handleForm = { id: null, result: '', remark: '' }">
      <el-form ref="handleFormRef" :model="handleForm" :rules="handleRules" label-width="100px">
        <el-form-item label="预警编号">{{ handleRow?.alertNo }}</el-form-item>
        <el-form-item label="处理结果" prop="result">
          <el-select v-model="handleForm.result" placeholder="请选择处理结果" class="w-full">
            <el-option label="已采购补货" value="已采购补货" />
            <el-option label="已调拨补充" value="已调拨补充" />
            <el-option label="已报废处理" value="已报废处理" />
            <el-option label="已恢复正常" value="已恢复正常" />
            <el-option label="暂不处理" value="暂不处理" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="handleForm.remark" type="textarea" :rows="3" placeholder="请输入处理备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定处理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { stockAlertPage, stockAlertHandle, stockAlertGetById } from '@/api/modules/inventory'
import { warehouseListAll } from '@/api/modules/inventory'

const loading = ref(false)
const searchForm = ref({ alertType: '', warehouseId: null, status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const warehouseOptions = ref([])

const handleVisible = ref(false)
const handleRow = ref(null)
const handleForm = reactive({ id: null, result: '', remark: '' })
const handleFormRef = ref(null)
const handleRules = { result: [{ required: true, message: '请选择处理结果', trigger: 'change' }] }

const alertTypeMap = { LOW_STOCK: '低于安全库存', HIGH_STOCK: '高于最大库存', SLOW_MOVING: '呆滞物料' }
const alertTypeTagMap = { LOW_STOCK: 'danger', HIGH_STOCK: 'warning', SLOW_MOVING: 'info' }
const alertTypeLabel = (t) => alertTypeMap[t] || t
const alertTypeTag = (t) => alertTypeTagMap[t] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await stockAlertPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取预警列表失败') } finally { loading.value = false }
}

const fetchWarehouses = async () => {
  try { const res = await warehouseListAll(); warehouseOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { alertType: '', warehouseId: null, status: '' }; handleSearch() }

const handleDetail = async (row) => {
  await stockAlertGetById(row.id)
  ElMessage.info('详情功能开发中')
}

const handleHandle = (row) => {
  handleRow.value = row
  handleForm.id = row.id
  handleForm.result = ''
  handleForm.remark = ''
  handleVisible.value = true
}

const handleSubmit = async () => {
  const valid = await handleFormRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    const res = await stockAlertHandle({ id: handleForm.id, result: handleForm.result, remark: handleForm.remark })
    res.code === 200 ? (ElMessage.success('处理成功'), handleVisible.value = false, fetchData()) : ElMessage.error(res.msg || '处理失败')
  } catch { ElMessage.error('处理失败') }
}

onMounted(() => { fetchWarehouses(); fetchData() })
</script>

<style scoped>
.stock-alert-list { padding: 16px; }
.mt-16 { margin-top: 16px; }
.w-full { width: 100%; }
.flex { display: flex; }
.justify-end { justify-content: flex-end; }
</style>
