<template>
  <el-dialog v-model="visible" :title="title" width="800px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="BOM编号" prop="bomNo">
            <el-input v-model="form.bomNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="产品名称" prop="productName">
            <el-input v-model="form.productName" placeholder="请输入产品名称" :maxlength="100" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="版本" prop="version">
            <el-input v-model="form.version" placeholder="请输入版本号" :maxlength="20" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" class="w-full">
              <el-option label="草稿" value="DRAFT" />
              <el-option label="已发布" value="PUBLISHED" />
              <el-option label="已停用" value="DISABLED" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="effectiveDate">
            <el-date-picker v-model="form.effectiveDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失效日期" prop="expireDate">
            <el-date-picker v-model="form.expireDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>
      <h4 class="mb-8">物料明细</h4>
      <el-table :data="form.items" border stripe size="small" class="w-full mb-8">
        <el-table-column label="物料" min-width="150">
          <template #default="{ row: r }">
            <el-select v-model="r.materialId" placeholder="选择物料" filterable size="small">
              <el-option v-for="m in materialOptions" :key="m.id" :label="m.materialName" :value="m.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="100">
          <template #default="{ row: r }">
            <el-input-number v-model="r.quantity" :min="0" :precision="4" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="单位" width="80">
          <template #default="{ row: r }">
            <el-input v-model="r.unit" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="损耗率(%)" width="110">
          <template #default="{ row: r }">
            <el-input-number v-model="r.scrapRate" :min="0" :max="100" :precision="2" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="排序" width="80">
          <template #default="{ row: r }">
            <el-input-number v-model="r.sortOrder" :min="0" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="70" fixed="right">
          <template #default="{ $index }">
            <el-button v-if="mode !== 'detail'" type="danger" link size="small" @click="removeItem($index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button v-if="mode !== 'detail'" type="primary" size="small" icon="Plus" @click="addItem">添加物料</el-button>
    </el-form>
    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button v-if="mode !== 'detail'" type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'

const props = defineProps({
  modelValue: Boolean, title: String, mode: { type: String, default: 'add' },
  data: Object, materialOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const emptyItem = () => ({ materialId: null, materialName: '', quantity: 0, unit: '', scrapRate: 0, sortOrder: 0 })
const form = reactive({
  id: null, bomNo: '', productName: '', version: '', status: 'DRAFT',
  effectiveDate: '', expireDate: '', items: [emptyItem()]
})

const rules = {
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  version: [{ required: true, message: '请输入版本号', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) {
    Object.keys(form).forEach(k => { if (v[k] !== undefined) { if (k === 'items' && Array.isArray(v[k])) form.items = v[k].map(i => ({ ...emptyItem(), ...i })); else form[k] = v[k] } })
    if (!form.items.length) form.items = [emptyItem()]
  } else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, bomNo: '', productName: '', version: '', status: 'DRAFT', effectiveDate: '', expireDate: '', items: [emptyItem()] })
}
const addItem = () => form.items.push(emptyItem())
const removeItem = (idx) => { if (form.items.length > 1) form.items.splice(idx, 1) }
const handleCancel = () => { visible.value = false; resetForm(); emit('cancel') }
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  const data = { ...form }
  data.items = data.items.map(i => ({ ...i, scrapRate: (i.scrapRate || 0) / 100 }))
  emit('confirm', data)
}
</script>

<style scoped>
.w-full { width: 100%; } .mb-8 { margin-bottom: 8px; }
</style>
