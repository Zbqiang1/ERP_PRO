<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

// 表单引用
const loginFormRef = ref(null)

// 登录表单
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 加载状态
const loading = ref(false)

// 表单校验规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 50, message: '用户名长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 4, max: 32, message: '密码长度在 4 到 32 个字符', trigger: 'blur' }
  ]
}

// 登录处理
async function handleLogin() {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
  } catch {
    return
  }

  loading.value = true

  try {
    // 调用登录接口
    await userStore.login({
      username: loginForm.username,
      password: loginForm.password
    })

    // 获取用户信息
    await userStore.getUserInfo()

    // 获取动态路由
    await permissionStore.generateRoutes()

    ElMessage.success('登录成功')

    // 跳转到重定向页面或首页
    const redirect = route.query.redirect || '/dashboard'
    router.push(redirect)
  } catch (error) {
    console.error('登录失败', error)
    ElMessage.error(error?.response?.data?.message || error?.message || '登录失败，请检查账号密码')
  } finally {
    loading.value = false
  }
}

// 回车键登录
function handleKeyDown(e) {
  if (e.key === 'Enter') {
    handleLogin()
  }
}
</script>

<template>
  <div class="login-container">
    <!-- 左侧品牌展示区 -->
    <div class="login-brand">
      <div class="login-brand-overlay"></div>
      <div class="login-brand-content">
        <div class="login-brand-logo">
          <img src="@/assets/vue.svg" alt="ERP Logo" />
        </div>
        <h1 class="login-brand-title">ERP 管理系统</h1>
        <p class="login-brand-subtitle">
          高效、智能的企业资源计划管理平台
        </p>
        <div class="login-brand-features">
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon :size="20"><Grid /></el-icon>
            </div>
            <span>一体化管理</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon :size="20"><DataAnalysis /></el-icon>
            </div>
            <span>数据驱动决策</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon">
              <el-icon :size="20"><Lock /></el-icon>
            </div>
            <span>安全可靠</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单区 -->
    <div class="login-form-section">
      <div class="login-form-wrapper">
        <div class="login-form-header">
          <h2>欢迎登录</h2>
          <p>请输入您的账号信息</p>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form-body"
          @keydown="handleKeyDown"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
              size="large"
              clearable
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              size="large"
              show-password
              clearable
            />
          </el-form-item>

          <div class="login-form-extra">
            <el-checkbox v-model="loginForm.rememberMe" label="记住密码" />
          </div>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              class="login-btn"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-form-footer">
          <span>版本 1.0.0</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

/* ========== 左侧品牌区 ========== */
.login-brand {
  flex: 1;
  position: relative;
  background: linear-gradient(135deg, #1890ff 0%, #0050b3 50%, #003a8c 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.login-brand-overlay {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle at 20% 80%, rgba(255,255,255,0.1) 0%, transparent 50%),
              radial-gradient(circle at 80% 20%, rgba(255,255,255,0.08) 0%, transparent 40%);
  animation: brandPulse 8s ease-in-out infinite;
}

@keyframes brandPulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

.login-brand-content {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 40px;
  color: #fff;
}

.login-brand-logo img {
  width: 80px;
  height: 80px;
  filter: drop-shadow(0 4px 12px rgba(0, 0, 0, 0.2));
}

.login-brand-title {
  font-size: 32px;
  font-weight: 700;
  margin-top: 24px;
  letter-spacing: 2px;
}

.login-brand-subtitle {
  font-size: 16px;
  margin-top: 12px;
  opacity: 0.85;
  line-height: 1.5;
}

.login-brand-features {
  display: flex;
  gap: 32px;
  margin-top: 48px;
  justify-content: center;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  opacity: 0.9;
}

.feature-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
}

/* ========== 右侧表单区 ========== */
.login-form-section {
  width: 480px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  padding: 40px;
}

.login-form-wrapper {
  width: 100%;
  max-width: 360px;
}

.login-form-header {
  margin-bottom: 40px;
}

.login-form-header h2 {
  font-size: 28px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 8px;
}

.login-form-header p {
  font-size: 14px;
  color: #999;
}

.login-form-body {
  margin-top: 8px;
}

.login-form-extra {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
  margin-top: -8px;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 4px;
}

.login-form-footer {
  text-align: center;
  color: #bbb;
  font-size: 12px;
  margin-top: 32px;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .login-brand {
    display: none;
  }
  .login-form-section {
    width: 100%;
  }
}
</style>
