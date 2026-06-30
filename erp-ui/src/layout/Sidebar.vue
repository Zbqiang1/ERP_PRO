<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { usePermissionStore } from '@/stores/permission'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const permissionStore = usePermissionStore()

const isCollapsed = computed(() => appStore.sidebarCollapsed)

// 菜单数据：从动态路由中提取 Layout 的 children
const menuList = computed(() => {
  const layoutRoute = router.getRoutes().find((r) => r.name === 'Layout')
  if (!layoutRoute || !layoutRoute.children) return []
  return layoutRoute.children
    .filter((r) => !r.meta?.hidden)
    .sort((a, b) => (a.meta?.orderNo || 0) - (b.meta?.orderNo || 0))
    .map((r) => ({
      path: r.path,
      name: r.name,
      title: r.meta?.title || r.name || '',
      icon: r.meta?.icon || '',
      children: (r.children || [])
        .filter((c) => !c.meta?.hidden)
        .map((c) => ({
          path: c.path,
          name: c.name,
          title: c.meta?.title || c.name || '',
          icon: c.meta?.icon || ''
        }))
    }))
    .filter((m) => !m.meta?.hidden)
})

const activeMenu = computed(() => {
  const { path, meta } = route
  // 如果当前路由有 activeMenu，使用它
  if (meta?.activeMenu) return meta.activeMenu
  return path
})

function handleSelect(index) {
  // 路由跳转由 el-menu 的 router 模式自动处理
}
</script>

<template>
  <div class="sidebar-container">
    <!-- Logo 区域 -->
    <div class="sidebar-logo" :class="{ collapsed: isCollapsed }">
      <img src="@/assets/vue.svg" alt="logo" class="sidebar-logo-img" />
      <transition name="fade">
        <span v-show="!isCollapsed" class="sidebar-logo-title">ERP管理系统</span>
      </transition>
    </div>

    <!-- 菜单区域 -->
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        :collapse-transition="false"
        :unique-opened="true"
        background-color="var(--sidebar-bg)"
        text-color="var(--sidebar-text)"
        active-text-color="#fff"
        router
        @select="handleSelect"
      >
        <template v-for="menu in menuList" :key="menu.path">
          <!-- 有子菜单 -->
          <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.path">
            <template #title>
              <el-icon v-if="menu.icon">
                <component :is="menu.icon" />
              </el-icon>
              <span>{{ menu.title }}</span>
            </template>
            <el-menu-item
              v-for="child in menu.children"
              :key="child.path"
              :index="menu.path + '/' + child.path"
            >
              <el-icon v-if="child.icon">
                <component :is="child.icon" />
              </el-icon>
              <span>{{ child.title }}</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 无子菜单 -->
          <el-menu-item v-else :index="menu.path">
            <el-icon v-if="menu.icon">
              <component :is="menu.icon" />
            </el-icon>
            <template #title>
              <span>{{ menu.title }}</span>
            </template>
          </el-menu-item>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<style scoped>
.sidebar-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  gap: 8px;
  flex-shrink: 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-logo.collapsed {
  padding: 0;
}

.sidebar-logo-img {
  width: 28px;
  height: 28px;
  flex-shrink: 0;
}

.sidebar-logo-title {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
}

.el-scrollbar {
  flex: 1;
  overflow: hidden;
}

.el-menu {
  border-right: none;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 220px;
}
</style>
