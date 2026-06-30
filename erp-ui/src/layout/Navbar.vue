<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'
import Breadcrumb from './Breadcrumb.vue'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()
const permissionStore = usePermissionStore()

const isCollapsed = computed(() => appStore.sidebarCollapsed)
const avatarUrl = computed(() => {
  return userStore.avatar || ''
})

function toggleSidebar() {
  appStore.toggleSidebar()
}

function handleFullscreen() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

function handleUserCommand(command) {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      handleLogout()
      break
  }
}

async function handleLogout() {
  await userStore.logout()
  permissionStore.resetRoutes()
  router.push('/login')
}

function handleThemeSwitch() {
  const themes = ['default', 'dark']
  const current = appStore.activeTheme
  const next = themes[(themes.indexOf(current) + 1) % themes.length]
  appStore.setTheme(next)
}
</script>

<template>
  <div class="navbar">
    <!-- 左侧：折叠按钮 + 面包屑 -->
    <div class="navbar-left">
      <el-icon class="navbar-toggle" @click="toggleSidebar">
        <Fold v-if="!isCollapsed" />
        <Expand v-else />
      </el-icon>
      <Breadcrumb class="navbar-breadcrumb" />
    </div>

    <!-- 右侧：功能区和用户信息 -->
    <div class="navbar-right">
      <!-- 全屏切换 -->
      <el-tooltip content="全屏切换" effect="dark">
        <el-icon class="navbar-icon" @click="handleFullscreen">
          <FullScreen />
        </el-icon>
      </el-tooltip>

      <!-- 主题切换 -->
      <el-tooltip content="主题切换" effect="dark">
        <el-icon class="navbar-icon" @click="handleThemeSwitch">
          <Switch />
        </el-icon>
      </el-tooltip>

      <!-- 用户下拉 -->
      <el-dropdown trigger="click" @command="handleUserCommand">
        <div class="navbar-user">
          <el-avatar :size="32" :src="avatarUrl">
            <el-icon :size="18"><UserFilled /></el-icon>
          </el-avatar>
          <span class="navbar-username">{{ userStore.realName || userStore.username }}</span>
          <el-icon class="navbar-arrow"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="settings">
              <el-icon><Setting /></el-icon>
              系统设置
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<style scoped>
.navbar {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.navbar-toggle {
  font-size: 20px;
  cursor: pointer;
  color: #555;
  flex-shrink: 0;
}

.navbar-toggle:hover {
  color: var(--primary-color);
}

.navbar-breadcrumb {
  flex-shrink: 0;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.navbar-icon {
  font-size: 18px;
  cursor: pointer;
  color: #555;
  display: flex;
  align-items: center;
}

.navbar-icon:hover {
  color: var(--primary-color);
}

.navbar-user {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background 0.2s;
}

.navbar-user:hover {
  background: #f5f5f5;
}

.navbar-username {
  font-size: 14px;
  color: #333;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.navbar-arrow {
  font-size: 12px;
  color: #999;
}
</style>
