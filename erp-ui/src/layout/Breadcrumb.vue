<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 从 route.matched 生成面包屑数据
const breadcrumbs = computed(() => {
  const matched = route.matched.filter((r) => r.meta?.title)
  // 如果有 Layout（即最外层），跳过它
  return matched
    .filter((r) => r.name !== 'Layout')
    .map((r) => ({
      title: r.meta?.title || r.name || '',
      path: r.path,
      redirect: r.redirect
    }))
    .filter((b) => b.title)
})
</script>

<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">
      <el-icon><HomeFilled /></el-icon>
    </el-breadcrumb-item>
    <el-breadcrumb-item
      v-for="item in breadcrumbs"
      :key="item.path"
      :to="item.redirect ? undefined : { path: item.path }"
    >
      {{ item.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<style scoped>
.el-breadcrumb {
  display: flex;
  align-items: center;
}
</style>
