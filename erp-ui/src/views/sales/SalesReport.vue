<template>
  <div class="sales-report-container">
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
        <el-form-item label="客户">
          <el-select v-model="filterForm.customerId" placeholder="全部客户" clearable filterable>
            <el-option v-for="c in customerOptions" :key="c.id" :label="c.customerName" :value="c.id" />
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
            <div class="summary-label">销售总额</div>
            <div class="summary-value">{{ formatMoney(summaryData.totalSales) }}</div>
            <div class="summary-trend">
              环比 <span :class="summaryData.salesTrend > 0 ? 'up' : 'down'">
                {{ summaryData.salesTrend > 0 ? '+' : '' }}{{ summaryData.salesTrend }}%
              </span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="summary-card">
          <div class="summary-item">
            <div class="summary-label">订单数量</div>
            <div class="summary-value">{{ summaryData.orderCount }}</div>
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
            <div class="summary-label">平均客单价</div>
            <div class="summary-value">{{ formatMoney(summaryData.avgOrderValue) }}</div>
            <el-tag type="info" size="small" style="margin-top: 8px;">客单价分析</el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域：月度趋势 + Top客户 -->
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>月度销售趋势</span>
          </template>
          <div ref="lineChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>Top客户销售排行</span>
          </template>
          <div ref="barChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 产品类别饼图 + 销售排行表 -->
    <el-row :gutter="16">
      <el-col :span="10">
        <el-card shadow="never" class="chart-card">
          <template #header>
            <span>产品销售类别占比</span>
          </template>
          <div ref="pieChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never" class="table-card">
          <template #header>
            <span>销售业绩排行</span>
          </template>
          <el-table :data="rankingData" border stripe style="width: 100%" empty-text="暂无数据">
            <el-table-column type="index" label="排名" width="60" align="center" />
            <el-table-column prop="salesperson" label="销售人员" width="100" />
            <el-table-column prop="orderCount" label="订单数" width="80" sortable />
            <el-table-column prop="totalAmount" label="销售金额" width="140" sortable>
              <template #default="{ row }">
                <span style="color: #f56c6c; font-weight: 500;">{{ formatMoney(row.totalAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="customerCount" label="客户数" width="80" />
            <el-table-column prop="avgOrderValue" label="客单价" width="120">
              <template #default="{ row }">
                {{ formatMoney(row.avgOrderValue) }}
              </template>
            </el-table-column>
            <el-table-column prop="completionRate" label="目标完成率" width="120" sortable>
              <template #default="{ row }">
                <el-progress :percentage="row.completionRate" :color="row.completionRate >= 100 ? '#67c23a' : '#409EFF'" />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { pageCustomer } from '@/api/modules/customer'
import * as echarts from 'echarts'

// ---------- 工具 ----------
const formatMoney = (val) => {
  if (val == null) return '¥0.00'
  return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// ---------- 筛选 ----------
const filterForm = reactive({
  dateRange: [],
  customerId: ''
})

const customerOptions = ref([])

// ---------- 汇总 ----------
const summaryData = reactive({
  totalSales: 0,
  orderCount: 0,
  avgOrderValue: 0,
  salesTrend: 0,
  orderTrend: 0
})

// ---------- 图表 ----------
const lineChartRef = ref(null)
const barChartRef = ref(null)
const pieChartRef = ref(null)
let lineChart = null
let barChart = null
let pieChart = null

// ---------- 排行 ----------
const rankingData = ref([])

// ---------- 加载客户 ----------
const loadCustomers = async () => {
  try {
    const res = await pageCustomer({ page: 1, pageSize: 999, status: 1 })
    customerOptions.value = res.data?.records || res.records || []
  } catch (e) { /* ignore */ }
}

// ---------- 加载报表数据 ----------
const fetchReportData = async () => {
  try {
    // 实际环境调用后端接口
    // const res = await salesOrder.report(params)

    // 模拟月度数据
    const mockMonthly = [
      { month: '1月', amount: 280000 }, { month: '2月', amount: 230000 },
      { month: '3月', amount: 320000 }, { month: '4月', amount: 350000 },
      { month: '5月', amount: 410000 }, { month: '6月', amount: 380000 }
    ]

    const mockTopCustomer = [
      { name: '客户A', value: 520000 }, { name: '客户B', value: 380000 },
      { name: '客户C', value: 280000 }, { name: '客户D', value: 210000 },
      { name: '客户E', value: 150000 }
    ]

    const mockCategory = [
      { name: '电子元器件', value: 450000 }, { name: '机械设备', value: 320000 },
      { name: '原材料', value: 280000 }, { name: '五金配件', value: 180000 },
      { name: '其他', value: 120000 }
    ]

    summaryData.totalSales = 1350000
    summaryData.orderCount = 286
    summaryData.avgOrderValue = 4720.28
    summaryData.salesTrend = 8.5
    summaryData.orderTrend = 5.2

    rankingData.value = [
      { salesperson: '张三', orderCount: 48, totalAmount: 280000, customerCount: 15, avgOrderValue: 5833, completionRate: 112 },
      { salesperson: '李四', orderCount: 52, totalAmount: 260000, customerCount: 18, avgOrderValue: 5000, completionRate: 104 },
      { salesperson: '王五', orderCount: 42, totalAmount: 245000, customerCount: 12, avgOrderValue: 5833, completionRate: 98 },
      { salesperson: '赵六', orderCount: 38, totalAmount: 210000, customerCount: 10, avgOrderValue: 5526, completionRate: 84 },
      { salesperson: '孙七', orderCount: 35, totalAmount: 180000, customerCount: 9, avgOrderValue: 5143, completionRate: 72 }
    ]

    await nextTick()
    renderLineChart(mockMonthly)
    renderBarChart(mockTopCustomer)
    renderPieChart(mockCategory)
  } catch (error) {
    ElMessage.error('加载报表数据失败')
  }
}

// ---------- 渲染折线图 ----------
const renderLineChart = (data) => {
  if (!lineChartRef.value) return
  if (lineChart) lineChart.dispose()
  lineChart = echarts.init(lineChartRef.value)
  lineChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: data.map(d => d.month), boundaryGap: false },
    yAxis: {
      type: 'value',
      axisLabel: { formatter: (val) => (val / 10000).toFixed(0) + '万' }
    },
    series: [{
      name: '销售额', type: 'line', data: data.map(d => d.amount),
      smooth: true, lineStyle: { color: '#409EFF', width: 3 },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64,158,255,0.3)' },
          { offset: 1, color: 'rgba(64,158,255,0.05)' }
        ])
      },
      itemStyle: { color: '#409EFF' },
      symbol: 'circle', symbolSize: 8
    }]
  })
}

// ---------- 渲染柱状图 ----------
const renderBarChart = (data) => {
  if (!barChartRef.value) return
  if (barChart) barChart.dispose()
  barChart = echarts.init(barChartRef.value)
  barChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '10%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value', axisLabel: { formatter: (val) => (val / 10000).toFixed(0) + '万' } },
    yAxis: {
      type: 'category',
      data: data.map(d => d.name).reverse(),
      axisLabel: { color: '#666' }
    },
    series: [{
      name: '销售额', type: 'bar',
      data: data.map(d => d.value).reverse(),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          { offset: 0, color: '#67c23a' }, { offset: 1, color: '#b3e19d' }
        ]),
        borderRadius: [0, 4, 4, 0]
      },
      barWidth: '45%'
    }]
  })
}

// ---------- 渲染饼图 ----------
const renderPieChart = (data) => {
  if (!pieChartRef.value) return
  if (pieChart) pieChart.dispose()
  pieChart = echarts.init(pieChartRef.value)
  pieChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: ¥{c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left', top: 'center' },
    series: [{
      name: '产品类别', type: 'pie',
      radius: ['40%', '65%'],
      center: ['55%', '50%'],
      avoidLabelOverlap: false,
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: data,
      itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 }
    }]
  })
}

// ---------- resize ----------
const handleResize = () => {
  lineChart?.resize(); barChart?.resize(); pieChart?.resize()
}

const handleQuery = () => { fetchReportData() }

const handleReset = () => {
  filterForm.dateRange = []; filterForm.customerId = ''
  fetchReportData()
}

onMounted(() => {
  loadCustomers(); fetchReportData()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  lineChart?.dispose(); barChart?.dispose(); pieChart?.dispose()
})
</script>

<style scoped lang="scss">
.sales-report-container {
  padding: 16px;

  .filter-card { margin-bottom: 16px; }

  .summary-row { margin-bottom: 16px; }

  .summary-card {
    .summary-item {
      text-align: center; padding: 8px 0;

      .summary-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
      .summary-value { font-size: 32px; font-weight: bold; color: #303133; margin-bottom: 8px; }
      .summary-trend { font-size: 12px; color: #909399;
        .up { color: #67c23a; } .down { color: #f56c6c; }
      }
    }
  }

  .chart-card {
    margin-bottom: 16px;
    .chart-container { width: 100%; height: 350px; }
  }

  .table-card { margin-bottom: 16px; }
}
</style>
