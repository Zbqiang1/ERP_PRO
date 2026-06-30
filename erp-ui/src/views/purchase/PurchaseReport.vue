<template>
  <div class="purchase-report-container">
    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="filterForm" inline>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="filterForm.supplierId" placeholder="全部供应商" clearable filterable>
            <el-option v-for="s in supplierOptions" :key="s.id" :label="s.supplierName" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 汇总卡片 -->
    <el-row :gutter="16" class="summary-row">
      <el-col :span="8">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-item">
            <div class="summary-label">采购订单总数</div>
            <div class="summary-value">{{ summaryData.totalOrderCount }}</div>
            <div class="summary-trend">
              环比 <span :class="summaryData.orderTrend > 0 ? 'up' : 'down'">
                {{ summaryData.orderTrend > 0 ? '+' : '' }}{{ summaryData.orderTrend }}%
              </span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-item">
            <div class="summary-label">采购总金额</div>
            <div class="summary-value">{{ formatMoney(summaryData.totalAmount) }}</div>
            <div class="summary-trend">
              环比 <span :class="summaryData.amountTrend > 0 ? 'up' : 'down'">
                {{ summaryData.amountTrend > 0 ? '+' : '' }}{{ summaryData.amountTrend }}%
              </span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-item">
            <div class="summary-label">待交付订单数</div>
            <div class="summary-value">{{ summaryData.pendingDeliveryCount }}</div>
            <el-tag type="warning" size="small" style="margin-top: 8px;">需要跟进</el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16">
      <el-col :span="16">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>月度采购金额趋势</span>
          </template>
          <div ref="barChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>供应商采购占比</span>
          </template>
          <div ref="pieChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 采购排行表格 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <span>采购物料Top排行</span>
      </template>
      <el-table :data="topMaterialData" border stripe style="width: 100%" empty-text="暂无数据">
        <el-table-column type="index" label="排名" width="60" align="center" />
        <el-table-column prop="materialCode" label="物料编码" width="140" />
        <el-table-column prop="materialName" label="物料名称" min-width="180" />
        <el-table-column prop="totalQty" label="采购数量" width="120" sortable />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="totalAmount" label="采购金额" width="140" sortable>
          <template #default="{ row }">
            {{ formatMoney(row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="supplierName" label="主要供应商" min-width="160" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { purchaseOrder } from '@/api/modules/purchase'
import { pageSupplier } from '@/api/modules/supplier'
import * as echarts from 'echarts'

// ---------- 工具 ----------
const formatMoney = (val) => {
  if (val == null) return '¥0.00'
  return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// ---------- 筛选 ----------
const filterForm = reactive({
  dateRange: [],
  supplierId: ''
})

const supplierOptions = ref([])

// ---------- 汇总 ----------
const summaryData = reactive({
  totalOrderCount: 0,
  totalAmount: 0,
  pendingDeliveryCount: 0,
  orderTrend: 0,
  amountTrend: 0
})

// ---------- 图表 ----------
const barChartRef = ref(null)
const pieChartRef = ref(null)
let barChart = null
let pieChart = null

// ---------- 排行表格 ----------
const topMaterialData = ref([])

// ---------- 加载供应商 ----------
const loadSuppliers = async () => {
  try {
    const res = await pageSupplier({ page: 1, pageSize: 999, status: 1 })
    supplierOptions.value = res.data?.records || res.records || []
  } catch (e) { /* ignore */ }
}

// ---------- 加载报表数据 ----------
const fetchReportData = async () => {
  try {
    const params = {
      dateStart: filterForm.dateRange?.[0] || undefined,
      dateEnd: filterForm.dateRange?.[1] || undefined,
      supplierId: filterForm.supplierId || undefined
    }

    // 实际项目中调用后端报表接口
    // const res = await purchaseOrder.report(params)

    // 模拟数据
    const mockMonthlyData = [
      { month: '1月', amount: 120000 },
      { month: '2月', amount: 98000 },
      { month: '3月', amount: 156000 },
      { month: '4月', amount: 134000 },
      { month: '5月', amount: 178000 },
      { month: '6月', amount: 165000 }
    ]
    const mockSupplierData = [
      { name: '供应商A', value: 350000 },
      { name: '供应商B', value: 280000 },
      { name: '供应商C', value: 150000 },
      { name: '供应商D', value: 71000 }
    ]

    summaryData.totalOrderCount = 156
    summaryData.totalAmount = 851000
    summaryData.pendingDeliveryCount = 23
    summaryData.orderTrend = 12.5
    summaryData.amountTrend = -3.2

    topMaterialData.value = [
      { materialCode: 'MAT-001', materialName: '不锈钢板304', totalQty: 5000, unit: 'kg', totalAmount: 125000, supplierName: '供应商A' },
      { materialCode: 'MAT-002', materialName: '轴承6205', totalQty: 3000, unit: '个', totalAmount: 98000, supplierName: '供应商B' },
      { materialCode: 'MAT-003', materialName: '密封圈DN50', totalQty: 8000, unit: '个', totalAmount: 76000, supplierName: '供应商C' },
      { materialCode: 'MAT-004', materialName: '螺栓M12x50', totalQty: 20000, unit: '个', totalAmount: 52000, supplierName: '供应商A' },
      { materialCode: 'MAT-005', materialName: '电机220V/1.5KW', totalQty: 200, unit: '台', totalAmount: 68000, supplierName: '供应商B' }
    ]

    await nextTick()
    renderBarChart(mockMonthlyData)
    renderPieChart(mockSupplierData)
  } catch (error) {
    ElMessage.error('加载报表数据失败')
  }
}

// ---------- 渲染柱状图 ----------
const renderBarChart = (data) => {
  if (!barChartRef.value) return
  if (barChart) barChart.dispose()

  barChart = echarts.init(barChartRef.value)
  barChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.map(d => d.month),
      axisLabel: { color: '#666' }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        color: '#666',
        formatter: (val) => (val / 10000).toFixed(0) + '万'
      }
    },
    series: [{
      name: '采购金额',
      type: 'bar',
      data: data.map(d => d.amount),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#409EFF' },
          { offset: 1, color: '#79bbff' }
        ]),
        borderRadius: [4, 4, 0, 0]
      },
      barWidth: '50%'
    }]
  })
}

// ---------- 渲染饼图 ----------
const renderPieChart = (data) => {
  if (!pieChartRef.value) return
  if (pieChart) pieChart.dispose()

  pieChart = echarts.init(pieChartRef.value)
  pieChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}: ¥{c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'center'
    },
    series: [{
      name: '供应商',
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['60%', '50%'],
      avoidLabelOverlap: false,
      label: {
        show: false
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold'
        }
      },
      data: data,
      itemStyle: {
        borderRadius: 4,
        borderColor: '#fff',
        borderWidth: 2
      }
    }]
  })
}

// ---------- 窗口大小改变 重绘 ----------
const handleResize = () => {
  barChart?.resize()
  pieChart?.resize()
}

// ---------- 搜索 ----------
const handleQuery = () => {
  fetchReportData()
}

const handleReset = () => {
  filterForm.dateRange = []
  filterForm.supplierId = ''
  fetchReportData()
}

onMounted(() => {
  loadSuppliers()
  fetchReportData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  barChart?.dispose()
  pieChart?.dispose()
})
</script>

<style scoped lang="scss">
.purchase-report-container {
  padding: 16px;

  .filter-card {
    margin-bottom: 16px;
  }

  .summary-row {
    margin-bottom: 16px;
  }

  .summary-card {
    .summary-item {
      text-align: center;
      padding: 8px 0;

      .summary-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .summary-value {
        font-size: 32px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 8px;
      }

      .summary-trend {
        font-size: 12px;
        color: #909399;

        .up { color: #67c23a; }
        .down { color: #f56c6c; }
      }
    }
  }

  .chart-card {
    margin-bottom: 16px;

    .chart-container {
      width: 100%;
      height: 350px;
    }
  }

  .table-card {
    margin-bottom: 16px;
  }
}
</style>
