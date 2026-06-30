<template>
  <div class="warehouse-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="仓库编码">
          <el-input v-model="searchForm.warehouseCode" placeholder="请输入仓库编码" clearable />
        </el-form-item>
        <el-form-item label="仓库名称">
          <el-input v-model="searchForm.warehouseName" placeholder="请输入仓库名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增仓库</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        empty-text="暂无数据"
        class="w-full"
      >
        <el-table-column prop="warehouseCode" label="仓库编码" width="120" />
        <el-table-column prop="warehouseName" label="仓库名称" width="150" />
        <el-table-column prop="address" label="仓库地址" min-width="200" />
        <el-table-column prop="manager" label="负责人" width="100" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="primary" link size="small" @click="handleDetail(row)">详情</el-button>
            <el-popconfirm title="确定要删除该仓库吗？" confirm-button-text="确定" cancel-button-text="取消" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="flex justify-end mt-16">
        <el-pagination
          v-model:current-page="pagination.pageNo"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </el-card>

    <FormDialog
      v-model="dialogVisible"
      :title="dialogTitle"
      :mode="dialogMode"
      :data="currentRow"
      @confirm="handleConfirm"
      @cancel="dialogVisible = false"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { warehousePage, warehouseAdd, warehouseUpdate, warehouseDelete, warehouseGetById } from '@/api/modules/inventory'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ warehouseCode: '', warehouseName: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogMode = ref('add')
const currentRow = ref(null)

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await warehousePage(params)
    if (res.code === 200) {
      tableData.value = res.data.records || res.data.list || []
      pagination.total = res.data.total || 0
    }
  } catch (e) {
    ElMessage.error('获取仓库列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { warehouseCode: '', warehouseName: '' }; handleSearch() }

const handleAdd = () => {
  dialogMode.value = 'add'
  dialogTitle.value = '新增仓库'
  currentRow.value = null
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogMode.value = 'edit'
  dialogTitle.value = '编辑仓库'
  try {
    const res = await warehouseGetById(row.id)
    currentRow.value = res.code === 200 ? res.data : row
  } catch {
    currentRow.value = row
  }
  dialogVisible.value = true
}

const handleDetail = async (row) => {
  dialogMode.value = 'detail'
  dialogTitle.value = '仓库详情'
  try {
    const res = await warehouseGetById(row.id)
    currentRow.value = res.code === 200 ? res.data : row
  } catch {
    currentRow.value = row
  }
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    const res = await warehouseDelete(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch {
    ElMessage.error('删除失败')
  }
}

const handleConfirm = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? warehouseAdd : warehouseUpdate
  try {
    const res = await apiMethod(formData)
    if (res.code === 200) {
      ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch {
    ElMessage.error('操作失败')
  }
}

onMounted(() => fetchData())
</script>

<style scoped>
.warehouse-list { padding: 16px; }
.mt-16 { margin-top: 16px; }
.mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; }
.flex { display: flex; }
.justify-end { justify-content: flex-end; }
</style>
