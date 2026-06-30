<template>
  <div class="mrp-result-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="计划">
          <el-select v-model="searchForm.planId" placeholder="请选择计划" clearable filterable>
            <el-option v-for="p in planOptions" :key="p.id" :label="p.planNo" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button type="success" icon="TrendCharts" @click="handleGenerate">生成MRP</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据，请先生成MRP" class="w-full">
        <el-table-column prop="materialName" label="物料" width="150" />
        <el-table-column prop="grossRequirement" label="毛需求" width="110" align="right" />
        <el-table-column prop="onHandQty" label="库存量" width="100" align="right" />
        <el-table-column prop="onOrderQty" label="在途量" width="100" align="right" />
        <el-table-column prop="netRequirement" label="净需求" width="100" align="right">
          <template #default="{ row }">
            <span :style="{ color: (row.netRequirement || 0) < 0 ? '#f56c6c' : '#67c23a' }">
              {{ row.netRequirement || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="plannedOrderRelease" label="计划订单下达" width="140" />
        <el-table-column prop="plannedOrderReceipt" label="计划订单接收" width="140" />
      </el-table>
      <div class="flex justify-end mt-16">
        <el-pagination
          v-model:current-page="pagination.pageNo" v-model:page-size="pagination.pageSize"
          :total="pagination.total" :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { mrpResultPage, mrpResultGenerate } from '@/api/modules/production'
import { mpsPlanListAll } from '@/api/modules/production'

const loading = ref(false)
const searchForm = ref({ planId: null })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })
const planOptions = ref([])

const fetchData = async () => {
  if (!searchForm.value.planId) { tableData.value = []; return }
  loading.value = true
  try {
    const params = { planId: searchForm.value.planId, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await mrpResultPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取MRP结果失败') } finally { loading.value = false }
}

const fetchPlans = async () => {
  try { const res = await mpsPlanListAll(); planOptions.value = res.code === 200 ? (res.data || []) : [] } catch { /* */ }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }

const handleGenerate = async () => {
  if (!searchForm.value.planId) { ElMessage.warning('请先选择计划'); return }
  try {
    await ElMessageBox.confirm('确定要生成MRP结果吗？', '生成MRP', { type: 'info' })
    loading.value = true
    const res = await mrpResultGenerate({ planId: searchForm.value.planId })
    if (res.code === 200) { ElMessage.success('MRP生成成功'); fetchData() } else { ElMessage.error(res.msg || 'MRP生成失败') }
  } catch { /* cancelled */ } finally { loading.value = false }
}

onMounted(() => { fetchPlans() })
</script>

<style scoped>
.mrp-result-list { padding: 16px; }
.mt-16 { margin-top: 16px; }
.w-full { width: 100%; }
.flex { display: flex; }
.justify-end { justify-content: flex-end; }
</style>
