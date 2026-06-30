<template>
  <el-dialog v-model="visible" :title="title" width="550px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="检验单号" prop="inspectionNo">
        <el-input v-model="form.inspectionNo" placeholder="自动生成" disabled />
      </el-form-item>
      <el-form-item label="工单号" prop="woNo">
        <el-input v-model="form.woNo" placeholder="请输入工单号" :maxlength="50" />
      </el-form-item>
      <el-form-item label="检验员" prop="inspectorName">
        <el-input v-model="form.inspectorName" placeholder="请输入检验员" :maxlength="50" />
      </el-form-item>
      <el-form-item label="检验日期" prop="inspectionDate">
        <el-date-picker v-model="form.inspectionDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
      </el-form-item>
      <el-form-item label="检验类型" prop="inspectionType">
        <el-select v-model="form.inspectionType" placeholder="请选择检验类型" class="w-full">
          <el-option label="首检" value="FIRST_CHECK" />
          <el-option label="巡检" value="PATROL_CHECK" />
          <el-option label="终检" value="FINAL_CHECK" />
        </el-select>
      </el-form-item>
      <el-form-item label="检验结果" prop="inspectionResult">
        <el-select v-model="form.inspectionResult" placeholder="请选择检验结果" class="w-full">
          <el-option label="合格" value="PASS" />
          <el-option label="不合格" value="FAIL" />
          <el-option label="让步接收" value="CONCESSION" />
        </el-select>
      </el-form-item>
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="合格数量" prop="qualifiedQty">
            <el-input-number v-model="form.qualifiedQty" :min="0" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷数量" prop="defectQty">
            <el-input-number v-model="form.defectQty" :min="0" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="缺陷描述">
        <el-input v-model="form.defectDesc" type="textarea" :rows="3" placeholder="请输入缺陷描述" />
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
  modelValue: Boolean, title: String, mode: { type: String, default: 'add' }, data: Object
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({
  id: null, inspectionNo: '', woNo: '', inspectorName: '', inspectionDate: '',
  inspectionType: '', inspectionResult: '', qualifiedQty: 0, defectQty: 0, defectDesc: ''
})

const rules = {
  woNo: [{ required: true, message: '请输入工单号', trigger: 'blur' }],
  inspectorName: [{ required: true, message: '请输入检验员', trigger: 'blur' }],
  inspectionDate: [{ required: true, message: '请选择检验日期', trigger: 'change' }],
  inspectionType: [{ required: true, message: '请选择检验类型', trigger: 'change' }],
  inspectionResult: [{ required: true, message: '请选择检验结果', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, inspectionNo: '', woNo: '', inspectorName: '', inspectionDate: '', inspectionType: '', inspectionResult: '', qualifiedQty: 0, defectQty: 0, defectDesc: '' })
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
