<template>
  <el-dialog v-model="visible" :title="title" width="800px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="盘点单号" prop="checkNo">
            <el-input v-model="form.checkNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="盘点类型" prop="checkType">
            <el-select v-model="form.checkType" placeholder="请选择盘点类型" class="w-full">
              <el-option label="周期盘点" value="PERIODIC" />
              <el-option label="随机盘点" value="RANDOM" />
              <el-option label="全面盘点" value="FULL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仓库" prop="warehouseId">
            <el-select v-model="form.warehouseId" placeholder="请选择仓库" filterable class="w-full">
              <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="盘点日期" prop="checkDate">
            <el-date-picker v-model="form.checkDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </el-form-item>
      <h4 class="mb-8">盘点明细</h4>
      <el-table :data="form.items" border stripe size="small" class="w-full mb-8">
        <el-table-column label="物料" min-width="150">
          <template #default="{ row: r }">
            <el-select v-model="r.materialId" placeholder="选择物料" filterable size="small">
              <el-option v-for="m in materialOptions" :key="m.id" :label="m.materialName" :value="m.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="账面数量" width="100">
          <template #default="{ row: r }">
            <el-input-number v-model="r.bookQty" :min="0" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="实际数量" width="100">
          <template #default="{ row: r }">
            <el-input-number v-model="r.actualQty" :min="0" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="差异数量" width="100" align="right">
          <template #default="{ row: r }">{{ (r.actualQty || 0) - (r.bookQty || 0) }}</template>
        </el-table-column>
        <el-table-column label="差异原因" min-width="150">
          <template #default="{ row: r }">
            <el-input v-model="r.diffReason" size="small" placeholder="差异原因" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="70" fixed="right">
          <template #default="{ $index }">
            <el-button v-if="mode !== 'detail'" type="danger" link size="small" @click="removeItem($index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button v-if="mode !== 'detail'" type="primary" size="small" icon="Plus" @click="addItem">添加明细行</el-button>
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
  data: Object, warehouseOptions: { type: Array, default: () => [] },
  materialOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const emptyItem = () => ({ materialId: null, bookQty: 0, actualQty: 0, diffReason: '' })
const form = reactive({ id: null, checkNo: '', checkType: '', warehouseId: null, checkDate: '', remark: '', items: [emptyItem()] })

const rules = {
  checkType: [{ required: true, message: '请选择盘点类型', trigger: 'change' }],
  warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
  checkDate: [{ required: true, message: '请选择盘点日期', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) {
    Object.keys(form).forEach(k => { if (v[k] !== undefined) { if (k === 'items' && Array.isArray(v[k])) form.items = v[k].map(i => ({ ...emptyItem(), ...i })); else form[k] = v[k] } })
    if (!form.items.length) form.items = [emptyItem()]
  } else resetForm()
}, { immediate: true })

const resetForm = () => { Object.assign(form, { id: null, checkNo: '', checkType: '', warehouseId: null, checkDate: '', remark: '', items: [emptyItem()] }) }
const addItem = () => form.items.push(emptyItem())
const removeItem = (idx) => { if (form.items.length > 1) form.items.splice(idx, 1) }
const handleCancel = () => { visible.value = false; resetForm(); emit('cancel') }
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  emit('confirm', { ...form })
}
</script>

<style scoped>
.w-full { width: 100%; } .mb-8 { margin-bottom: 8px; }
</style>
