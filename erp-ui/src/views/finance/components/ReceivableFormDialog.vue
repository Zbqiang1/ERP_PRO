<template>
  <el-dialog v-model="visible" :title="title" width="500px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="应收单号" prop="receivableNo">
        <el-input v-model="form.receivableNo" placeholder="自动生成" disabled />
      </el-form-item>
      <el-form-item label="客户" prop="customerId">
        <el-select v-model="form.customerId" placeholder="请选择客户" filterable class="w-full">
          <el-option v-for="c in customerOptions" :key="c.id" :label="c.customerName" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="应收金额" prop="amount">
        <el-input-number v-model="form.amount" :min="0" :precision="2" placeholder="请输入应收金额" class="w-full" />
      </el-form-item>
      <el-form-item label="到期日期" prop="dueDate">
        <el-date-picker v-model="form.dueDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
      </el-form-item>
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
import { ref, reactive, watch } from 'vue'

const props = defineProps({
  modelValue: Boolean, title: String, mode: { type: String, default: 'add' },
  data: Object, customerOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({ id: null, receivableNo: '', customerId: null, amount: 0, dueDate: '', remark: '' })

const rules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  amount: [{ required: true, message: '请输入应收金额', trigger: 'blur' }],
  dueDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => { Object.assign(form, { id: null, receivableNo: '', customerId: null, amount: 0, dueDate: '', remark: '' }) }
const handleCancel = () => { visible.value = false; resetForm(); emit('cancel') }
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  emit('confirm', { ...form })
}
</script>

<style scoped>
.w-full { width: 100%; }
</style>
