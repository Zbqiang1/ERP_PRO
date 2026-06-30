<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()

// 统计数据
const stats = ref([
  { title: '用户总数', value: 0, icon: 'UserFilled', color: '#1890ff', bg: '#e6f7ff', path: '/system/user' },
  { title: '订单总数', value: 0, icon: 'Document', color: '#52c41a', bg: '#f6ffed', path: '/sales/order' },
  { title: '产品总数', value: 0, icon: 'Goods', color: '#faad14', bg: '#fffbe6', path: '/inventory/product' },
  { title: '今日收入', value: '¥0', icon: 'Money', color: '#ff4d4f', bg: '#fff2f0', path: '/finance/income' }
])

// 最近动态
const activities = ref([
  { type: 'success', content: '系统已成功启动', time: new Date().toLocaleString(), icon: 'CircleCheck' },
  { type: 'info', content: '欢迎登录 ERP 管理系统', time: new Date().toLocaleString(), icon: 'InfoFilled' },
  { type: 'warning', content: '请及时完善企业信息', time: new Date().toLocaleString(), icon: 'WarningFilled' }
])

// 快捷入口
const shortcuts = [
  { title: '销售订单', icon: 'Sell', path: '/sales/order', desc: '管理销售订单' },
  { title: '采购入库', icon: 'ShoppingCart', path: '/purchase/order', desc: '采购入库管理' },
  { title: '库存查询', icon: 'Box', path: '/inventory/stock', desc: '实时库存查询' },
  { title: '财务管理', icon: 'Coin', path: '/finance', desc: '财务收支管理' }
]

// 获取统计数据
async function fetchStats() {
  try {
    // 逐个获取统计，失败时使用默认值
    const endpoints = [
      { url: '/users', key: 'user' },
      { url: '/sales/orders', key: 'order' },
      { url: '/inventory/materials', key: 'product' },
      { url: '/finance/receivables', key: 'income' }
    ]

    const results = await Promise.allSettled(
      endpoints.map((ep) => request({ url: ep.url, method: 'get', params: { pageSize: 1 } }))
    )

    if (results[0].status === 'fulfilled' && results[0].value?.data?.total !== undefined) {
      stats.value[0].value = results[0].value.data.total
    }
    if (results[1].status === 'fulfilled' && results[1].value?.data?.total !== undefined) {
      stats.value[1].value = results[1].value.data.total
    }
    if (results[2].status === 'fulfilled' && results[2].value?.data?.total !== undefined) {
      stats.value[2].value = results[2].value.data.total
    }
    if (results[3].status === 'fulfilled' && results[3].value?.data) {
      stats.value[3].value = `¥${results[3].value.data.toLocaleString()}`
    }
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

function navigateTo(path) {
  router.push(path)
}

const currentTime = new Date()
const hour = currentTime.getHours()
const greeting = hour < 6 ? '凌晨好' : hour < 12 ? '上午好' : hour < 18 ? '下午好' : '晚上好'

onMounted(() => {
  fetchStats()
})
</script>

<template>
  <div class="dashboard">
    <!-- 欢迎区域 -->
    <div class="dashboard-welcome">
      <div class="welcome-text">
        <h2>{{ greeting }}，{{ userStore.realName || userStore.username }}

</h2>
        <p>欢迎使用 ERP 管理系统，祝您工作愉快</p>
      </div>
      <div class="welcome-date">
        {{ new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }) }}
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-row">
      <el-col v-for="(item, index) in stats" :key="index" :xs="24" :sm="12" :md="6">
        <div class="stat-card" @click="navigateTo(item.path)">
          <div class="stat-card-content">
            <div class="stat-card-info">
              <span class="stat-card-title">{{ item.title }}</span>
              <span class="stat-card-value">{{ item.value || '--' }}</span>
            </div>
            <div class="stat-card-icon" :style="{ background: item.bg, color: item.color }">
              <el-icon :size="28">
                <component :is="item.icon" />
              </el-icon>
            </div>
          </div>
          <div class="stat-card-footer" :style="{ borderColor: item.color }">
            <span :style="{ color: item.color }">查看详情 →</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快捷入口 + 最近动态 -->
    <el-row :gutter="16" class="content-row">
      <!-- 快捷入口 -->
      <el-col :xs="24" :md="14">
        <el-card class="section-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>快捷入口</span>
            </div>
          </template>
          <el-row :gutter="12">
            <el-col v-for="item in shortcuts" :key="item.path" :xs="12" :sm="6">
              <div class="shortcut-item" @click="navigateTo(item.path)">
                <div class="shortcut-icon">
                  <el-icon :size="24">
                    <component :is="item.icon" />
                  </el-icon>
                </div>
                <span class="shortcut-title">{{ item.title }}</span>
                <span class="shortcut-desc">{{ item.desc }}</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <!-- 最近动态 -->
      <el-col :xs="24" :md="10">
        <el-card class="section-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span>系统通知</span>
              <el-button text size="small">查看全部</el-button>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="(activity, index) in activities"
              :key="index"
              :timestamp="activity.time"
              placement="top"
              :color="activity.type === 'success' ? '#52c41a' : activity.type === 'warning' ? '#faad14' : '#1890ff'"
            >
              <div class="activity-item">
                <el-icon v-if="activity.icon" class="activity-icon" :size="14">
                  <component :is="activity.icon" />
                </el-icon>
                <span>{{ activity.content }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard {
  max-width: 1400px;
}

/* ========== 欢迎区域 ========== */
.dashboard-welcome {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 24px 32px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.welcome-text h2 {
  font-size: 20px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 4px;
}

.welcome-text p {
  font-size: 14px;
  color: #999;
}

.welcome-date {
  font-size: 14px;
  color: #666;
}

/* ========== 统计卡片 ========== */
.stats-row {
  margin-bottom: 16px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
}

.stat-card-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stat-card-title {
  font-size: 13px;
  color: #999;
}

.stat-card-value {
  font-size: 26px;
  font-weight: 600;
  color: #333;
}

.stat-card-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-card-footer {
  border-top: 1px solid transparent;
  padding: 10px 20px;
  font-size: 12px;
  background: #fafafa;
  border-top-width: 1px;
  border-top-style: solid;
}

/* ========== 内容区域 ========== */
.content-row {
  margin-bottom: 16px;
}

.section-card {
  border-radius: 8px;
  margin-bottom: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
}

/* 快捷入口 */
.shortcut-item {
  text-align: center;
  padding: 16px 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
  margin-bottom: 8px;
}

.shortcut-item:hover {
  background: #f5f7fa;
}

.shortcut-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto 8px;
  border-radius: 12px;
  background: linear-gradient(135deg, #e6f7ff, #bae7ff);
  color: #1890ff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.shortcut-title {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.shortcut-desc {
  display: block;
  font-size: 12px;
  color: #999;
}

/* ========== 动态 ========== */
.activity-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #555;
}

.activity-icon {
  color: #1890ff;
}
</style>
