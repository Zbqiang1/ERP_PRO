<template>
  <el-dialog v-model="visible" :title="title" width="800px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="调拨单号" prop="transferNo">
            <el-input v-model="form.transferNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="调拨日期" prop="transferDate">
            <el-date-picker v-model="form.transferDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="调出仓库" prop="fromWarehouseId">
            <el-select v-model="form.fromWarehouseId" placeholder="请选择" filterable class="w-full">
              <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="调入仓库" prop="toWarehouseId">
            <el-select v-model="form.toWarehouseId" placeholder="请选择" filterable class="w-full">
              <el-option v-for="w in warehouseOptions" :key="w.id" :label="w.warehouseName" :value="w.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </el-form-item>
      <h4 class="mb-8">调拨明细</h4>
      <el-table :data="form.items" border stripe size="small" class="w-full mb-8">
        <el-table-column label="物料" min-width="150">
          <template #default="{ row: r }">
            <el-select v-model="r.materialId" placeholder="选择物料" filterable size="small">
              <el-option v-for="m in materialOptions" :key="m.id" :label="m.materialName" :value="m.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="调出库位" width="130">
          <template #default="{ row: r }">
            <el-select v-model="r.fromLocationId" placeholder="库位" filterable size="small">
              <el-option v-for="l in locationOptions" :key="l.id" :label="l.locationName" :value="l.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="调入库位" width="130">
          <template #default="{ row: r }">
            <el-select v-model="r.toLocationId" placeholder="库位" filterable size="small">
              <el-option v-for="l in locationOptions" :key="l.id" :label="l.locationName" :value="l.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="100">
          <template #default="{ row: r }">
            <el-input-number v-model="r.quantity" :min="0" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="批次号" width="120">
          <template #default="{ row: r }">
            <el-input v-model="r.batchNo" size="small" placeholder="批次号" />
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
  materialOptions: { type: Array, default: () => [] },
  locationOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const emptyItem = () => ({ materialId: null, fromLocationId: null, toLocationId: null, quantity: 0, batchNo: '' })
const form = reactive({ id: null, transferNo: '', transferDate: '', fromWarehouseId: null, toWarehouseId: null, remark: '', items: [emptyItem()] })

const rules = {
  fromWarehouseId: [{ required: true, message: '请选择调出仓库', trigger: 'change' }],
  toWarehouseId: [{ required: true, message: '请选择调入仓库', trigger: 'change' }],
  transferDate: [{ required: true, message: '请选择调拨日期', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) {
    Object.keys(form).forEach(k => { if (v[k] !== undefined) { if (k === 'items' && Array.isArray(v[k])) form.items = v[k].map(i => ({ ...emptyItem(), ...i })); else form[k] = v[k] } })
    if (!form.items.length) form.items = [emptyItem()]
  } else resetForm()
}, { immediate: true })

const resetForm = () => { Object.assign(form, { id: null, transferNo: '', transferDate: '', fromWarehouseId: null, toWarehouseId: null, remark: '', items: [emptyItem()] }) }
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
