<template>
  <el-dialog v-model="visible" :title="title" width="500px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="工序号" prop="operationSeq">
        <el-input-number v-model="form.operationSeq" :min="1" :precision="0" placeholder="请输入工序号" class="w-full" />
      </el-form-item>
      <el-form-item label="工序名称" prop="operationName">
        <el-input v-model="form.operationName" placeholder="请输入工序名称" :maxlength="100" />
      </el-form-item>
      <el-form-item label="工作中心" prop="workcenter">
        <el-input v-model="form.workcenter" placeholder="请输入工作中心" :maxlength="100" />
      </el-form-item>
      <el-form-item label="计划工时(h)" prop="plannedHours">
        <el-input-number v-model="form.plannedHours" :min="0" :precision="1" placeholder="请输入计划工时" class="w-full" />
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
  data: Object, workOrderId: [String, Number]
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({ id: null, operationSeq: 1, operationName: '', workcenter: '', plannedHours: 0 })

const rules = {
  operationSeq: [{ required: true, message: '请输入工序号', trigger: 'blur' }],
  operationName: [{ required: true, message: '请输入工序名称', trigger: 'blur' }],
  workcenter: [{ required: true, message: '请输入工作中心', trigger: 'blur' }],
  plannedHours: [{ required: true, message: '请输入计划工时', trigger: 'blur' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => { Object.assign(form, { id: null, operationSeq: 1, operationName: '', workcenter: '', plannedHours: 0 }) }
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
