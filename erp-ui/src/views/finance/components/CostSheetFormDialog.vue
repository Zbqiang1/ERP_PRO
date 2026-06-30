<template>
  <el-dialog v-model="visible" :title="title" width="550px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="成本单号" prop="costNo">
            <el-input v-model="form.costNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="产品名称" prop="productName">
            <el-input v-model="form.productName" placeholder="请输入产品名称" :maxlength="100" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成本期间" prop="costPeriod">
            <el-date-picker v-model="form.costPeriod" type="month" placeholder="选择月份" value-format="YYYY-MM" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="物料成本" prop="materialCost">
            <el-input-number v-model="form.materialCost" :min="0" :precision="2" placeholder="物料成本" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="人工成本" prop="laborCost">
            <el-input-number v-model="form.laborCost" :min="0" :precision="2" placeholder="人工成本" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制造费用" prop="overheadCost">
            <el-input-number v-model="form.overheadCost" :min="0" :precision="2" placeholder="制造费用" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button v-if="mode !== 'detail'" type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'

const props = defineProps({
  modelValue: Boolean, title: String, mode: { type: String, default: 'add' }, data: Object
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({
  id: null, costNo: '', productName: '', costPeriod: '',
  materialCost: 0, laborCost: 0, overheadCost: 0, remark: ''
})

const rules = {
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  costPeriod: [{ required: true, message: '请选择成本期间', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, costNo: '', productName: '', costPeriod: '', materialCost: 0, laborCost: 0, overheadCost: 0, remark: '' })
}
const handleCancel = () => { visible.value = false; resetForm(); emit('cancel') }
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  const data = { ...form, totalCost: (form.materialCost || 0) + (form.laborCost || 0) + (form.overheadCost || 0) }
  emit('confirm', data)
}
</script>

<style scoped>
.w-full { width: 100%; }
</style>
