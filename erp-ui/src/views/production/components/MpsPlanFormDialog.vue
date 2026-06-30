<template>
  <el-dialog v-model="visible" :title="title" width="550px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="计划单号" prop="planNo">
        <el-input v-model="form.planNo" placeholder="自动生成" disabled />
      </el-form-item>
      <el-form-item label="计划月份" prop="planMonth">
        <el-date-picker v-model="form.planMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" class="w-full" />
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select v-model="form.productId" placeholder="请选择产品" filterable class="w-full">
          <el-option v-for="p in productOptions" :key="p.id" :label="p.name" :value="p.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="计划数量" prop="plannedQty">
        <el-input-number v-model="form.plannedQty" :min="1" :precision="0" placeholder="请输入计划数量" class="w-full" />
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
  data: Object, productOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({ id: null, planNo: '', planMonth: '', productId: null, plannedQty: 0, remark: '' })

const rules = {
  planMonth: [{ required: true, message: '请选择计划月份', trigger: 'change' }],
  productId: [{ required: true, message: '请选择产品', trigger: 'change' }],
  plannedQty: [{ required: true, message: '请输入计划数量', trigger: 'blur' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => { Object.assign(form, { id: null, planNo: '', planMonth: '', productId: null, plannedQty: 0, remark: '' }) }

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
