<template>
  <div class="erp-dashboard">
    <!-- Top Stats Row -->
    <div class="stats-row">
      <div class="stat-card" v-for="card in statCards" :key="card.label">
        <div class="stat-card-inner" :style="{ background: card.bg }">
          <div class="stat-icon">
            <el-icon :size="28"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">{{ card.label }}</div>
            <div class="stat-value">
              <animated-number :value="card.value" :prefix="card.prefix" :suffix="card.suffix" />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Charts Row 1: Sales Trend + Top Products -->
    <div class="charts-row">
      <div class="chart-card chart-card-60">
        <div class="chart-header">
          <h3>月度销售趋势</h3>
          <span class="chart-subtitle">近12个月</span>
        </div>
        <div ref="salesTrendRef" class="chart-body"></div>
      </div>
      <div class="chart-card chart-card-40">
        <div class="chart-header">
          <h3>产品销量TOP10</h3>
          <span class="chart-subtitle">本月</span>
        </div>
        <div ref="topProductsRef" class="chart-body"></div>
      </div>
    </div>

    <!-- Charts Row 2: Pending Orders Table + Inventory Pie + Production Gauge -->
    <div class="charts-row">
      <div class="chart-card chart-card-40">
        <div class="chart-header">
          <h3>待交付订单</h3>
          <span class="chart-subtitle">实时更新</span>
        </div>
        <div class="chart-body-table">
          <el-table :data="pendingOrders" size="small" stripe max-height="240" class="dashboard-table">
            <el-table-column prop="orderNo" label="订单号" width="140" />
            <el-table-column prop="customerName" label="客户" width="120" />
            <el-table-column prop="amount" label="金额" width="100" align="right">
              <template #default="{ row }">{{ row.amount?.toFixed(2) }}</template>
            </el-table-column>
            <el-table-column prop="deliveryDate" label="交付日期" width="110" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag size="small" :type="row.status === 'OVERDUE' ? 'danger' : 'warning'">
                  {{ row.status === 'OVERDUE' ? '已逾期' : '待交付' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div class="chart-card chart-card-30">
        <div class="chart-header">
          <h3>库存预警分布</h3>
          <span class="chart-subtitle">实时数据</span>
        </div>
        <div ref="inventoryAlertRef" class="chart-body"></div>
      </div>
      <div class="chart-card chart-card-30">
        <div class="chart-header">
          <h3>生产完工率</h3>
          <span class="chart-subtitle">本月</span>
        </div>
        <div ref="productionGaugeRef" class="chart-body"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { dashboardGetStats, dashboardGetSalesTrend, dashboardGetTopProducts, dashboardGetPendingOrders, dashboardGetInventoryAlert, dashboardGetProductionRate } from '@/api/dashboard'

// ==================== Stat Cards ====================
const statCards = reactive([
  { label: '今日销售额', value: 0, prefix: '¥', suffix: '', icon: 'Money', bg: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
  { label: '今日订单数', value: 0, prefix: '', suffix: ' 单', icon: 'Document', bg: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
  { label: '待交付', value: 0, prefix: '', suffix: ' 单', icon: 'Truck', bg: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
  { label: '低库存预警', value: 0, prefix: '', suffix: ' 项', icon: 'Warning', bg: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' },
  { label: '待审批', value: 0, prefix: '', suffix: ' 条', icon: 'Clock', bg: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
  { label: '在线用户', value: 0, prefix: '', suffix: ' 人', icon: 'User', bg: 'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)' }
])

// ==================== Reactive Refs ====================
const salesTrendRef = ref(null)
const topProductsRef = ref(null)
const inventoryAlertRef = ref(null)
const productionGaugeRef = ref(null)
const pendingOrders = ref([])

let salesChart = null
let productsChart = null
let alertChart = null
let gaugeChart = null
let refreshTimer = null

// ==================== Fetch Data ====================
const fetchStats = async () => {
  try {
    const res = await dashboardGetStats()
    if (res.code === 200) {
      const data = res.data || {}
      statCards[0].value = data.todaySales || 0
      statCards[1].value = data.todayOrders || 0
      statCards[2].value = data.pendingDelivery || 0
      statCards[3].value = data.lowStockCount || 0
      statCards[4].value = data.pendingApproval || 0
      statCards[5].value = data.onlineUsers || 0
    }
  } catch { /* silent */ }
}

const fetchSalesTrend = async () => {
  try {
    const res = await dashboardGetSalesTrend()
    if (res.code === 200 && salesChart) {
      const data = res.data || []
      salesChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'category',
          data: data.map(d => d.month),
          axisLabel: { color: '#aab3c2' }
        },
        yAxis: {
          type: 'value',
          axisLabel: { color: '#aab3c2', formatter: '{value} 万' }
        },
        series: [{
          name: '销售额',
          type: 'line',
          smooth: true,
          data: data.map(d => d.amount),
          symbol: 'circle',
          symbolSize: 6,
          lineStyle: { width: 3, color: '#00f2fe' },
          itemStyle: { color: '#00f2fe' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(0,242,254,0.3)' },
              { offset: 1, color: 'rgba(0,242,254,0.02)' }
            ])
          }
        }]
      })
    }
  } catch { /* silent */ }
}

const fetchTopProducts = async () => {
  try {
    const res = await dashboardGetTopProducts()
    if (res.code === 200 && productsChart) {
      const data = res.data || []
      productsChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '8%', bottom: '3%', top: '10%', containLabel: true },
        xAxis: {
          type: 'value',
          axisLabel: { color: '#aab3c2' }
        },
        yAxis: {
          type: 'category',
          data: data.map(d => d.productName).reverse(),
          axisLabel: { color: '#aab3c2', width: 100, overflow: 'truncate' },
          inverse: true
        },
        series: [{
          type: 'bar',
          data: data.map(d => d.salesQty).reverse(),
          barWidth: 16,
          itemStyle: {
            borderRadius: [0, 4, 4, 0],
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#667eea' },
              { offset: 1, color: '#764ba2' }
            ])
          }
        }]
      })
    }
  } catch { /* silent */ }
}

const fetchPendingOrders = async () => {
  try {
    const res = await dashboardGetPendingOrders()
    if (res.code === 200) {
      pendingOrders.value = (res.data || []).slice(0, 10)
    }
  } catch { /* silent */ }
}

const fetchInventoryAlert = async () => {
  try {
    const res = await dashboardGetInventoryAlert()
    if (res.code === 200 && alertChart) {
      const data = res.data || []
      alertChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { color: '#aab3c2' } },
        series: [{
          type: 'pie',
          radius: ['45%', '70%'],
          center: ['40%', '50%'],
          itemStyle: { borderRadius: 6, borderColor: '#0a1628', borderWidth: 3 },
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
          data: data.map(d => ({
            value: d.count,
            name: d.alertType === 'LOW_STOCK' ? '低于安全库存' : d.alertType === 'HIGH_STOCK' ? '高于最大库存' : d.alertType || '其他'
          }))
        }]
      })
    }
  } catch { /* silent */ }
}

const fetchProductionRate = async () => {
  try {
    const res = await dashboardGetProductionRate()
    if (res.code === 200 && gaugeChart) {
      const rate = (res.data?.completionRate || 0) * 100
      gaugeChart.setOption({
        series: [{
          type: 'gauge',
          startAngle: 210,
          endAngle: -30,
          center: ['50%', '55%'],
          radius: '85%',
          min: 0,
          max: 100,
          splitNumber: 10,
          axisLine: {
            show: true,
            lineStyle: {
              width: 14,
              color: [
                [0.3, '#f56c6c'],
                [0.7, '#e6a23c'],
                [1, '#67c23a']
              ]
            }
          },
          pointer: { icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z', length: '72%', width: 8, itemStyle: { color: '#409eff' } },
          axisTick: { distance: -14, length: 8, lineStyle: { width: 2, color: '#999' } },
          splitLine: { distance: -18, length: 14, lineStyle: { width: 3, color: '#999' } },
          axisLabel: { color: '#aab3c2', distance: 20, fontSize: 10 },
          anchor: { show: true, size: 14 },
          title: { offsetCenter: [0, '80%'], fontSize: 14, color: '#aab3c2' },
          detail: {
            valueAnimation: true,
            formatter: '{value}%',
            fontSize: 28,
            fontWeight: 'bold',
            color: '#00f2fe',
            offsetCenter: [0, '55%']
          },
          data: [{ value: rate, name: '生产完工率' }]
        }]
      })
    }
  } catch { /* silent */ }
}

// ==================== Init Charts ====================
const initCharts = () => {
  if (salesTrendRef.value) {
    salesChart = echarts.init(salesTrendRef.value, null, { backgroundColor: 'transparent' })
  }
  if (topProductsRef.value) {
    productsChart = echarts.init(topProductsRef.value, null, { backgroundColor: 'transparent' })
  }
  if (inventoryAlertRef.value) {
    alertChart = echarts.init(inventoryAlertRef.value, null, { backgroundColor: 'transparent' })
  }
  if (productionGaugeRef.value) {
    gaugeChart = echarts.init(productionGaugeRef.value, null, { backgroundColor: 'transparent' })
  }
}

const resizeCharts = () => {
  salesChart?.resize()
  productsChart?.resize()
  alertChart?.resize()
  gaugeChart?.resize()
}

const loadAllData = () => {
  fetchStats()
  fetchSalesTrend()
  fetchTopProducts()
  fetchPendingOrders()
  fetchInventoryAlert()
  fetchProductionRate()
}

onMounted(() => {
  initCharts()
  loadAllData()
  window.addEventListener('resize', resizeCharts)
  refreshTimer = setInterval(loadAllData, 60000)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  if (refreshTimer) clearInterval(refreshTimer)
  salesChart?.dispose()
  productsChart?.dispose()
  alertChart?.dispose()
  gaugeChart?.dispose()
})
</script>

<script>
// AnimatedNumber component
import { defineComponent, ref, watch, onMounted } from 'vue'

const AnimatedNumber = defineComponent({
  name: 'AnimatedNumber',
  props: { value: { type: Number, default: 0 }, prefix: { type: String, default: '' }, suffix: { type: String, default: '' }, duration: { type: Number, default: 800 } },
  setup(props) {
    const displayValue = ref(0)
    let animFrame = null

    const animate = (start, end, duration) => {
      const startTime = performance.now()
      const step = (currentTime) => {
        const elapsed = currentTime - startTime
        const progress = Math.min(elapsed / duration, 1)
        const eased = 1 - Math.pow(1 - progress, 3)
        displayValue.value = start + (end - start) * eased
        if (progress < 1) { animFrame = requestAnimationFrame(step) }
      }
      animFrame = requestAnimationFrame(step)
    }

    watch(() => props.value, (newVal, oldVal) => {
      if (animFrame) cancelAnimationFrame(animFrame)
      animate(oldVal || 0, newVal, props.duration)
    }, { immediate: false })

    onMounted(() => { displayValue.value = props.value })

    return { displayValue }
  },
  template: `<span>{{ prefix }}{{ Math.round(displayValue).toLocaleString() }}{{ suffix }}</span>`
})

export default {
  components: { AnimatedNumber }
}
</script>

<style scoped>
.erp-dashboard {
  padding: 16px;
  background-color: #0a1628;
  min-height: 100vh;
  color: #e0e6ed;
}

/* Stats Row */
.stats-row {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 16px;
  margin-bottom: 16px;
}
.stat-card {
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}
.stat-card-inner {
  padding: 18px 16px;
  display: flex;
  align-items: center;
  gap: 14px;
  color: #fff;
}
.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.stat-content { flex: 1; min-width: 0; }
.stat-label { font-size: 13px; opacity: 0.85; margin-bottom: 4px; text-overflow: ellipsis; overflow: hidden; white-space: nowrap; }
.stat-value { font-size: 24px; font-weight: 700; letter-spacing: 0.5px; }

/* Charts Row */
.charts-row {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}
.chart-card {
  background: rgba(255, 255, 255, 0.04);
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}
.chart-card-60 { flex: 0 0 calc(60% - 8px); }
.chart-card-40 { flex: 0 0 calc(40% - 8px); }
.chart-card-30 { flex: 0 0 calc(30% - 10.67px); }
.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}
.chart-header h3 { margin: 0; font-size: 15px; font-weight: 600; color: #e0e6ed; }
.chart-subtitle { font-size: 12px; color: #6b7a90; }
.chart-body { height: 300px; }
.chart-body-table { padding: 12px; }

.dashboard-table {
  background: transparent !important;
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-header-bg-color: rgba(255, 255, 255, 0.04);
  --el-table-row-hover-bg-color: rgba(255, 255, 255, 0.06);
  --el-table-border-color: rgba(255, 255, 255, 0.06);
  --el-table-text-color: #c0ccda;
  --el-table-header-text-color: #aab3c2;
}

@media (max-width: 1400px) {
  .stats-row { grid-template-columns: repeat(3, 1fr); }
}
@media (max-width: 768px) {
  .stats-row { grid-template-columns: repeat(2, 1fr); }
  .charts-row {
    flex-direction: column;
  }
  .chart-card-60, .chart-card-40, .chart-card-30 {
    flex: 1 1 100%;
  }
}
</style>
