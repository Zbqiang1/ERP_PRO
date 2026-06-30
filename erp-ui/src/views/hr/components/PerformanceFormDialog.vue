<template>
  <el-dialog v-model="visible" :title="title" width="550px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="员工" prop="employeeId">
        <el-select v-model="form.employeeId" placeholder="请选择员工" filterable class="w-full">
          <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="考核期间" prop="assessmentPeriod">
        <el-date-picker v-model="form.assessmentPeriod" type="month" placeholder="选择月份" value-format="YYYY-MM" class="w-full" />
      </el-form-item>
      <el-form-item label="考核人" prop="assessorName">
        <el-input v-model="form.assessorName" placeholder="请输入考核人" :maxlength="50" />
      </el-form-item>
      <el-form-item label="KPI得分" prop="kpiScore">
        <el-input-number v-model="form.kpiScore" :min="0" :max="100" :precision="1" placeholder="KPI得分" class="w-full" />
      </el-form-item>
      <el-form-item label="评价" prop="evaluation">
        <el-input v-model="form.evaluation" type="textarea" :rows="3" placeholder="请输入评价" />
      </el-form-item>
      <el-form-item label="等级" prop="grade">
        <el-select v-model="form.grade" placeholder="请选择等级" class="w-full">
          <el-option label="A - 优秀" value="A" />
          <el-option label="B - 良好" value="B" />
          <el-option label="C - 合格" value="C" />
          <el-option label="D - 待改进" value="D" />
        </el-select>
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
  data: Object, employeeOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({
  id: null, employeeId: null, assessmentPeriod: '', assessorName: '',
  kpiScore: 0, evaluation: '', grade: 'B'
})

const rules = {
  employeeId: [{ required: true, message: '请选择员工', trigger: 'change' }],
  assessmentPeriod: [{ required: true, message: '请选择考核期间', trigger: 'change' }],
  assessorName: [{ required: true, message: '请输入考核人', trigger: 'blur' }],
  kpiScore: [{ required: true, message: '请输入KPI得分', trigger: 'blur' }],
  grade: [{ required: true, message: '请选择等级', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, employeeId: null, assessmentPeriod: '', assessorName: '', kpiScore: 0, evaluation: '', grade: 'B' })
}
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
