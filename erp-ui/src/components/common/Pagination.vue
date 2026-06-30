<script setup>
import { computed } from 'vue'

const props = defineProps({
  // 当前页码
  currentPage: {
    type: Number,
    default: 1
  },
  // 每页条数
  pageSize: {
    type: Number,
    default: 10
  },
  // 总条数
  total: {
    type: Number,
    default: 0
  },
  // 每页条数选项
  pageSizes: {
    type: Array,
    default: () => [10, 20, 50, 100]
  },
  // 布局
  layout: {
    type: String,
    default: 'total, sizes, prev, pager, next, jumper'
  },
  // 是否显示背景
  background: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:currentPage', 'update:pageSize', 'change'])

const paginationCurrentPage = computed({
  get: () => props.currentPage,
  set: (val) => emit('update:currentPage', val)
})

const paginationPageSize = computed({
  get: () => props.pageSize,
  set: (val) => emit('update:pageSize', val)
})

function handleSizeChange(val) {
  emit('change', { page: props.currentPage, size: val })
}

function handleCurrentChange(val) {
  emit('change', { page: val, size: props.pageSize })
}
</script>

<template>
  <div class="common-pagination">
    <el-pagination
      v-model:current-page="paginationCurrentPage"
      v-model:page-size="paginationPageSize"
      :total="total"
      :page-sizes="pageSizes"
      :layout="layout"
      :background="background"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<style scoped>
.common-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 16px 0 0;
}
</style>
