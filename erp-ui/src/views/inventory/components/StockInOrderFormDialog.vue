<template>
  <el-dialog v-model="visible" :title="title" width="750px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="入库单号" prop="inNo">
            <el-input v-model="form.inNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入库类型" prop="inType">
            <el-select v-model="form.inType" placeholder="请选择入库类型" class="w-full">
              <el-option label="采购入库" value="PURCHASE" />
              <el-option label="生产入库" value="PRODUCTION" />
              <el-option label="调拨入库" value="TRANSFER" />
              <el-option label="退货入库" value="RETURN" />
              <el-option label="盘盈" value="CHECK_SURPLUS" />
              <el-option label="其他" value="OTHER" />
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
          <el-form-item label="入库日期" prop="inDate">
            <el-date-picker v-model="form.inDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </el-form-item>

      <h4 class="mb-8">入库明细</h4>
      <el-table :data="form.items" border stripe size="small" class="w-full mb-8">
        <el-table-column label="物料" min-width="150">
          <template #default="{ row }">
            <el-select v-model="row.materialId" placeholder="选择物料" filterable size="small">
              <el-option v-for="m in materialOptions" :key="m.id" :label="m.materialName" :value="m.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="库位" width="120">
          <template #default="{ row }">
            <el-select v-model="row.locationId" placeholder="库位" filterable size="small">
              <el-option v-for="l in locationOptions" :key="l.id" :label="l.locationName" :value="l.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="数量" width="100">
          <template #default="{ row }">
            <el-input-number v-model="row.quantity" :min="0" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="单价" width="100">
          <template #default="{ row }">
            <el-input-number v-model="row.unitPrice" :min="0" :precision="2" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="金额" width="100" align="right">
          <template #default="{ row }">{{ ((row.quantity || 0) * (row.unitPrice || 0)).toFixed(2) }}</template>
        </el-table-column>
        <el-table-column label="批次号" width="120">
          <template #default="{ row }">
            <el-input v-model="row.batchNo" size="small" placeholder="批次号" />
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

const emptyItem = () => ({ materialId: null, materialName: '', locationId: null, quantity: 0, unitPrice: 0, batchNo: '' })
const form = reactive({ id: null, inNo: '', inType: '', warehouseId: null, inDate: '', remark: '', items: [emptyItem()] })

const rules = {
  inType: [{ required: true, message: '请选择入库类型', trigger: 'change' }],
  warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
  inDate: [{ required: true, message: '请选择入库日期', trigger: 'change' }]
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
  Object.assign(form, { id: null, inNo: '', inType: '', warehouseId: null, inDate: '', remark: '', items: [emptyItem()] })
}

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
.w-full { width: 100%; }
.mb-8 { margin-bottom: 8px; }
</style>
