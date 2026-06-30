<template>
  <el-dialog v-model="visible" :title="title" width="550px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="请假单号" prop="leaveNo">
        <el-input v-model="form.leaveNo" placeholder="自动生成" disabled />
      </el-form-item>
      <el-form-item label="员工" prop="employeeId">
        <el-select v-model="form.employeeId" placeholder="请选择员工" filterable class="w-full">
          <el-option v-for="e in employeeOptions" :key="e.id" :label="e.realName" :value="e.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="请假类型" prop="leaveType">
        <el-select v-model="form.leaveType" placeholder="请选择请假类型" class="w-full">
          <el-option label="事假" value="PERSONAL" />
          <el-option label="病假" value="SICK" />
          <el-option label="年假" value="ANNUAL" />
          <el-option label="婚假" value="MARRIAGE" />
          <el-option label="产假" value="MATERNITY" />
          <el-option label="调休" value="COMPENSATORY" />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" class="w-full" />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" value-format="YYYY-MM-DD HH:mm:ss" class="w-full" />
      </el-form-item>
      <el-form-item label="请假原因" prop="reason">
        <el-input v-model="form.reason" type="textarea" :rows="3" placeholder="请输入请假原因" />
      </el-form-item>
      <el-form-item label="总时数(h)">
        <strong>{{ totalHours }}</strong>
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
  modelValue: Boolean, title: String, mode: { type: String, default: 'add' },
  data: Object, employeeOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({
  id: null, leaveNo: '', employeeId: null, leaveType: '',
  startTime: '', endTime: '', reason: ''
})

const totalHours = computed(() => {
  if (!form.startTime || !form.endTime) return 0
  const diff = new Date(form.endTime) - new Date(form.startTime)
  return diff > 0 ? Number((diff / (1000 * 60 * 60)).toFixed(1)) : 0
})

const rules = {
  employeeId: [{ required: true, message: '请选择员工', trigger: 'change' }],
  leaveType: [{ required: true, message: '请选择请假类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  reason: [{ required: true, message: '请输入请假原因', trigger: 'blur' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, leaveNo: '', employeeId: null, leaveType: '', startTime: '', endTime: '', reason: '' })
}
const handleCancel = () => { visible.value = false; resetForm(); emit('cancel') }
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  emit('confirm', { ...form, totalHours: totalHours.value })
}
</script>

<style scoped>
.w-full { width: 100%; }
</style>
