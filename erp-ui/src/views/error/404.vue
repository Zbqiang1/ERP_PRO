<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function goHome() {
  router.push('/')
}
</script>

<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-code">404</div>
      <div class="error-divider"></div>
      <div class="error-info">
        <h2>抱歉，页面未找到</h2>
        <p>您访问的页面不存在或已被移除</p>
      </div>
    </div>
    <div class="error-actions">
      <el-button type="primary" @click="goHome">返回首页</el-button>
      <el-button @click="$router.go(-1)">返回上页</el-button>
    </div>
    <div class="error-illustration">
      <img src="@/assets/hero.png" alt="404" />
    </div>
  </div>
</template>

<style scoped>
.error-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  padding: 40px;
}

.error-content {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 32px;
}

.error-code {
  font-size: 80px;
  font-weight: 700;
  color: var(--primary-color);
  line-height: 1;
}

.error-divider {
  width: 1px;
  height: 64px;
  background: #e0e0e0;
}

.error-info h2 {
  font-size: 22px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.error-info p {
  font-size: 14px;
  color: #999;
}

.error-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.error-illustration {
  opacity: 0.4;
}

.error-illustration img {
  max-width: 300px;
}
</style>
