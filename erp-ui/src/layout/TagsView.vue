<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

const tagsViewList = computed(() => appStore.tagsViewList)

// 右键菜单
const contextMenuVisible = ref(false)
const contextMenuLeft = ref(0)
const contextMenuTop = ref(0)
const selectedTag = ref({})

// 监听路由变化，添加标签
function addTag() {
  if (route.name) {
    appStore.addTagView({
      path: route.path,
      name: route.name,
      title: route.meta?.title || route.name,
      meta: route.meta || {}
    })
  }
}

// 初始化添加当前标签
addTag()

// 后期路由变化需要外部调用，这里用 watch 处理
// 实际在 router.afterEach 里触发，这里注册一个 computed 即可

function isActive(tag) {
  return tag.path === route.path
}

function handleTagClick(tag) {
  router.push(tag.path)
}

function handleContextMenu(e, tag) {
  e.preventDefault()
  contextMenuLeft.value = e.clientX
  contextMenuTop.value = e.clientY
  contextMenuVisible.value = true
  selectedTag.value = tag
}

function closeContextMenu() {
  contextMenuVisible.value = false
}

function handleClose(tag) {
  appStore.removeTagView(tag.path)
  if (isActive(tag)) {
    // 跳转到最后一个标签页
    const lastTag = tagsViewList.value[tagsViewList.value.length - 1]
    if (lastTag) {
      router.push(lastTag.path)
    } else {
      router.push('/')
    }
  }
}

function handleCloseCurrent() {
  handleClose(selectedTag.value)
  closeContextMenu()
}

function handleCloseOthers() {
  appStore.closeOtherTags(selectedTag.value.path)
  closeContextMenu()
}

function handleCloseAll() {
  appStore.closeAllTags()
  closeContextMenu()
  // 跳转到首页
  router.push('/')
}
</script>

<template>
  <div class="tags-view-container" @click="closeContextMenu">
    <el-scrollbar>
      <div class="tags-view-wrapper">
        <router-link
          v-for="tag in tagsViewList"
          :key="tag.path"
          :to="tag.path"
          class="tags-view-item"
          :class="{ active: isActive(tag) }"
          @click.prevent="handleTagClick(tag)"
          @contextmenu.prevent="handleContextMenu($event, tag)"
        >
          <span class="tags-view-item-title">{{ tag.title }}</span>
          <el-icon
            v-if="!tag.meta?.affix"
            class="tags-view-item-close"
            @click.prevent.stop="handleClose(tag)"
          >
            <Close />
          </el-icon>
        </router-link>
      </div>
    </el-scrollbar>

    <!-- 右键菜单 -->
    <teleport to="body">
      <transition name="fade">
        <ul
          v-show="contextMenuVisible"
          class="context-menu"
          :style="{ left: contextMenuLeft + 'px', top: contextMenuTop + 'px' }"
        >
          <li @click="handleCloseCurrent">关闭当前</li>
          <li @click="handleCloseOthers">关闭其他</li>
          <li @click="handleCloseAll">关闭所有</li>
        </ul>
      </transition>
    </teleport>
  </div>
</template>

<style scoped>
.tags-view-container {
  height: 40px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.tags-view-wrapper {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 8px;
  gap: 2px;
}

.tags-view-item {
  display: inline-flex;
  align-items: center;
  height: 28px;
  padding: 0 10px;
  font-size: 12px;
  color: #666;
  background: #fafafa;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  cursor: pointer;
  text-decoration: none;
  white-space: nowrap;
  flex-shrink: 0;
  transition: all 0.2s;
}

.tags-view-item:hover {
  color: var(--primary-color);
  background: #e6f7ff;
}

.tags-view-item.active {
  color: #fff;
  background: var(--primary-color);
  border-color: var(--primary-color);
}

.tags-view-item.active .tags-view-item-close {
  color: #fff;
}

.tags-view-item-title {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tags-view-item-close {
  font-size: 12px;
  margin-left: 6px;
  border-radius: 50%;
  transition: background 0.2s;
}

.tags-view-item-close:hover {
  background: rgba(0, 0, 0, 0.15);
}

/* 右键菜单 */
.context-menu {
  position: fixed;
  z-index: 9999;
  min-width: 120px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  padding: 4px 0;
  list-style: none;
  margin: 0;
}

.context-menu li {
  padding: 8px 16px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
}

.context-menu li:hover {
  background: #f5f5f5;
  color: var(--primary-color);
}
</style>
