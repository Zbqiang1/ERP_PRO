<template>
  <div class="fixed-asset-list">
    <el-card shadow="never">
      <el-form :model="searchForm" :inline="true" size="default">
        <el-form-item label="资产编号">
          <el-input v-model="searchForm.assetNo" placeholder="请输入资产编号" clearable />
        </el-form-item>
        <el-form-item label="资产名称">
          <el-input v-model="searchForm.assetName" placeholder="请输入资产名称" clearable />
        </el-form-item>
        <el-form-item label="资产类别">
          <el-select v-model="searchForm.category" placeholder="请选择类别" clearable>
            <el-option label="电子设备" value="ELECTRONIC" />
            <el-option label="机械设备" value="MACHINERY" />
            <el-option label="房屋建筑" value="BUILDING" />
            <el-option label="运输工具" value="VEHICLE" />
            <el-option label="办公设备" value="OFFICE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="使用中" value="IN_USE" />
            <el-option label="已折旧" value="DEPRECIATED" />
            <el-option label="已报废" value="DISPOSED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">查询</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt-16">
      <div class="mb-16">
        <el-button type="primary" icon="Plus" @click="handleAdd">新增固定资产</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe empty-text="暂无数据" class="w-full">
        <el-table-column prop="assetNo" label="资产编号" width="130" />
        <el-table-column prop="assetName" label="资产名称" width="150" />
        <el-table-column prop="category" label="类别" width="100">
          <template #default="{ row }">{{ categoryLabel(row.category) }}</template>
        </el-table-column>
        <el-table-column prop="deptName" label="使用部门" width="120" />
        <el-table-column prop="originalValue" label="原值" width="120" align="right">
          <template #default="{ row }">{{ row.originalValue?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="currentValue" label="净值" width="120" align="right">
          <template #default="{ row }">{{ row.currentValue?.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="depreciationRate" label="折旧率(%)" width="100" align="right" />
        <el-table-column prop="purchaseDate" label="购入日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 'IN_USE'" type="success" link size="small" @click="handleDepreciation(row)">计提折旧</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row)">
              <template #reference>
                <el-button type="danger" link size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="flex justify-end mt-16">
        <el-pagination
          v-model:current-page="pagination.pageNo" v-model:page-size="pagination.pageSize"
          :total="pagination.total" :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper" @size-change="fetchData" @current-change="fetchData"
        />
      </div>
    </el-card>

    <FormDialog
      v-model="dialogVisible" :title="dialogTitle" :mode="dialogMode"
      :data="currentRow" @confirm="handleSave"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { fixedAssetPage, fixedAssetAdd, fixedAssetUpdate, fixedAssetDelete, fixedAssetGetById, fixedAssetDepreciation } from '@/api/modules/finance'
import FormDialog from '@/components/common/FormDialog.vue'

const loading = ref(false)
const searchForm = ref({ assetNo: '', assetName: '', category: '', status: '' })
const tableData = ref([])
const pagination = reactive({ pageNo: 1, pageSize: 10, total: 0 })

const dialogVisible = ref(false); const dialogTitle = ref(''); const dialogMode = ref('add'); const currentRow = ref(null)

const categoryMap = { ELECTRONIC: '电子设备', MACHINERY: '机械设备', BUILDING: '房屋建筑', VEHICLE: '运输工具', OFFICE: '办公设备', OTHER: '其他' }
const statusMap = { IN_USE: '使用中', DEPRECIATED: '已折旧', DISPOSED: '已报废' }
const statusTagMap = { IN_USE: 'success', DEPRECIATED: 'warning', DISPOSED: 'danger' }
const categoryLabel = (c) => categoryMap[c] || c
const statusLabel = (s) => statusMap[s] || s
const statusTagType = (s) => statusTagMap[s] || ''

const fetchData = async () => {
  loading.value = true
  try {
    const params = { ...searchForm.value, pageNo: pagination.pageNo, pageSize: pagination.pageSize }
    const res = await fixedAssetPage(params)
    if (res.code === 200) { tableData.value = res.data.records || res.data.list || []; pagination.total = res.data.total || 0 }
  } catch { ElMessage.error('获取资产列表失败') } finally { loading.value = false }
}

const handleSearch = () => { pagination.pageNo = 1; fetchData() }
const handleReset = () => { searchForm.value = { assetNo: '', assetName: '', category: '', status: '' }; handleSearch() }

const handleAdd = () => { dialogMode.value = 'add'; dialogTitle.value = '新增固定资产'; currentRow.value = null; dialogVisible.value = true }
const handleEdit = async (row) => {
  dialogMode.value = 'edit'; dialogTitle.value = '编辑固定资产'
  try { const res = await fixedAssetGetById(row.id); currentRow.value = res.code === 200 ? res.data : row } catch { currentRow.value = row }
  dialogVisible.value = true
}
const handleDepreciation = async (row) => {
  try {
    await ElMessageBox.confirm(`确定计提折旧？当前净值: ${row.currentValue?.toFixed(2)}`, '计提折旧', { type: 'info' })
    const res = await fixedAssetDepreciation(row.id)
    res.code === 200 ? (ElMessage.success('折旧计提成功'), fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { /* */ }
}
const handleDelete = async (row) => {
  try {
    const res = await fixedAssetDelete(row.id)
    res.code === 200 ? (ElMessage.success('删除成功'), fetchData()) : ElMessage.error(res.msg || '删除失败')
  } catch { ElMessage.error('删除失败') }
}
const handleSave = async (formData) => {
  const apiMethod = dialogMode.value === 'add' ? fixedAssetAdd : fixedAssetUpdate
  try {
    const res = await apiMethod(formData)
    res.code === 200 ? (ElMessage.success(dialogMode.value === 'add' ? '新增成功' : '更新成功'), dialogVisible.value = false, fetchData()) : ElMessage.error(res.msg || '操作失败')
  } catch { ElMessage.error('操作失败') }
}

onMounted(() => fetchData())
</script>

<style scoped>
.fixed-asset-list { padding: 16px; }
.mt-16 { margin-top: 16px; } .mb-16 { margin-bottom: 16px; }
.w-full { width: 100%; } .flex { display: flex; } .justify-end { justify-content: flex-end; }
</style>
