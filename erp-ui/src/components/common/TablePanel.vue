<script setup>
import { ref, computed, useSlots } from 'vue'
import Pagination from './Pagination.vue'

const props = defineProps({
  // 表格列配置
  columns: {
    type: Array,
    required: true
  },
  // 表格数据
  data: {
    type: Array,
    default: () => []
  },
  // 是否显示搜索区域
  showSearch: {
    type: Boolean,
    default: true
  },
  // 是否显示多选
  showSelection: {
    type: Boolean,
    default: false
  },
  // 加载状态
  loading: {
    type: Boolean,
    default: false
  },
  // 分页参数
  pagination: {
    type: Object,
    default: () => ({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
  },
  // 是否显示操作列
  showAction: {
    type: Boolean,
    default: true
  },
  // 操作列宽度
  actionWidth: {
    type: [String, Number],
    default: 200
  },
  // 操作列 label
  actionLabel: {
    type: String,
    default: '操作'
  },
  // 行键
  rowKey: {
    type: String,
    default: 'id'
  },
  // 最大高度
  maxHeight: {
    type: [String, Number],
    default: ''
  },
  // 是否显示分页
  showPagination: {
    type: Boolean,
    default: true
  },
  // 多级表头模式
  border: {
    type: Boolean,
    default: true
  },
  stripe: {
    type: Boolean,
    default: true
  },
  // 搜索表单相关
  searchModel: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits([
  'search',
  'reset',
  'selection-change',
  'page-change',
  'row-click',
  'sort-change',
  'refresh'
])

const slots = useSlots()

// 搜索展开收起
const searchCollapsed = ref(false)

function toggleSearch() {
  searchCollapsed.value = !searchCollapsed.value
}

// 选中的行
const selectedRows = ref([])

function handleSelectionChange(selection) {
  selectedRows.value = selection
  emit('selection-change', selection)
}

function handlePageChange(params) {
  emit('page-change', params)
}

function handleReset() {
  emit('reset')
}

function handleSearch() {
  emit('search')
}

function handleRefresh() {
  emit('refresh')
}

// 分页 props 计算
const paginationProps = computed(() => ({
  currentPage: props.pagination.currentPage,
  pageSize: props.pagination.pageSize,
  total: props.pagination.total
}))

// 最终列配置
const finalColumns = computed(() => {
  let cols = [...props.columns]
  if (props.showSelection) {
    cols.unshift({ type: 'selection', width: 55 })
  }
  cols.unshift({ type: 'index', label: '序号', width: 60, align: 'center' })
  return cols
})
</script>

<template>
  <div class="table-panel">
    <!-- Slot: 如果有外部传入的 search 插槽则使用，否则渲染内置 search 区域 -->
    <div v-if="showSearch" class="table-panel-search">
      <slot name="search">
        <!-- 内置搜索区域 -->
        <div class="search-bar">
          <div>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </div>
        </div>
      </slot>
      <div class="search-tools">
        <el-button text @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
    </div>

    <!-- 操作按钮栏 -->
    <div v-if="showAction" class="table-panel-actions">
      <div class="table-panel-actions-left">
        <slot name="actions" />
      </div>
      <div class="table-panel-actions-right">
        <slot name="actions-right" />
      </div>
    </div>

    <!-- 表格 -->
    <el-table
      :data="data"
      :border="border"
      :stripe="stripe"
      :row-key="rowKey"
      :max-height="maxHeight"
      :loading="loading"
      @selection-change="handleSelectionChange"
      @row-click="(row) => emit('row-click', row)"
      @sort-change="(sort) => emit('sort-change', sort)"
    >
      <template v-for="col in finalColumns" :key="col.prop || col.label">
        <el-table-column
          v-if="col.type === 'selection'"
          type="selection"
          width="55"
        />
        <el-table-column
          v-else-if="col.type === 'index'"
          type="index"
          :label="col.label"
          :width="col.width"
          :align="col.align"
        />
        <el-table-column
          v-else
          :prop="col.prop"
          :label="col.label"
          :width="col.width"
          :min-width="col.minWidth"
          :align="col.align || 'left'"
          :sortable="col.sortable"
          :fixed="col.fixed"
          :show-overflow-tooltip="col.showOverflowTooltip !== false"
        >
          <template v-if="col.slot" #default="scope">
            <slot :name="col.slot" :row="scope.row" :column="scope.column" :index="scope.$index" />
          </template>
        </el-table-column>
      </template>

      <!-- 操作列 -->
      <el-table-column
        v-if="showAction && slots.action"
        :label="actionLabel"
        :width="actionWidth"
        align="center"
        fixed="right"
      >
        <template #default="scope">
          <slot name="action" :row="scope.row" :index="scope.$index" />
        </template>
      </el-table-column>

      <!-- 空数据 -->
      <template #empty>
        <slot name="empty">
          <div class="empty-data">
            <el-empty :image-size="80" description="暂无数据" />
          </div>
        </slot>
      </template>
    </el-table>

    <!-- 分页 -->
    <Pagination
      v-if="showPagination"
      v-model:current-page="paginationProps.currentPage"
      v-model:page-size="paginationProps.pageSize"
      :total="pagination.total"
      @change="handlePageChange"
    />
  </div>
</template>

<style scoped>
.table-panel {
  background: #fff;
  border-radius: 4px;
  padding: 16px;
}

.table-panel-search {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.table-panel-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.table-panel-actions-left,
.table-panel-actions-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-bar {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-tools {
  display: flex;
  align-items: center;
}

.empty-data {
  padding: 40px 0;
}
</style>
