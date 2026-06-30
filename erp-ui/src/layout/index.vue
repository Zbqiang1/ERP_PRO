<script setup>
import { computed } from 'vue'
import { useAppStore } from '@/stores/app'
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'
import TagsView from './TagsView.vue'

const appStore = useAppStore()
const isCollapsed = computed(() => appStore.sidebarCollapsed)
</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧边栏 -->
    <el-aside :width="isCollapsed ? '64px' : '220px'" class="layout-aside">
      <Sidebar />
    </el-aside>

    <!-- 右侧主体 -->
    <el-container class="layout-main">
      <!-- 顶部导航 -->
      <el-header height="56px" class="layout-header">
        <Navbar />
      </el-header>

      <!-- 标签页 -->
      <div class="layout-tags">
        <TagsView />
      </div>

      <!-- 主内容区域 -->
      <el-main class="layout-content">
        <router-view v-slot="{ Component, route }">
          <keep-alive :include="cachedViews">
            <component :is="Component" :key="route.path" />
          </keep-alive>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'LayoutIndex'
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  overflow: hidden;
}

.layout-aside {
  background-color: var(--sidebar-bg);
  transition: width 0.3s ease;
  overflow: hidden;
  flex-shrink: 0;
}

.layout-main {
  flex-direction: column;
  overflow: hidden;
}

.layout-header {
  padding: 0;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  z-index: 10;
  flex-shrink: 0;
}

.layout-tags {
  flex-shrink: 0;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
}

.layout-content {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  background: var(--bg-color);
  padding: 16px;
}
</style>
