<template>
  <el-dialog v-model="visible" :title="title" width="650px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="工单号" prop="woNo">
            <el-input v-model="form.woNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="产品" prop="productName">
            <el-input v-model="form.productName" placeholder="请输入产品名称" :maxlength="100" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划数量" prop="quantity">
            <el-input-number v-model="form.quantity" :min="1" :precision="0" placeholder="输入数量" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车间" prop="workshop">
            <el-input v-model="form.workshop" placeholder="请输入车间" :maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划开始" prop="startDate">
            <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划结束" prop="endDate">
            <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="form.priority" placeholder="请选择" class="w-full">
              <el-option label="高" value="HIGH" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="低" value="LOW" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联BOM">
            <el-select v-model="form.bomId" placeholder="请选择BOM" filterable class="w-full">
              <el-option v-for="b in bomOptions" :key="b.id" :label="b.bomNo + ' - ' + b.productName" :value="b.id" />
            </el-select>
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
import { ref, reactive, watch } from 'vue'

const props = defineProps({
  modelValue: Boolean, title: String, mode: { type: String, default: 'add' },
  data: Object, bomOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({
  id: null, woNo: '', productName: '', quantity: 0, workshop: '',
  startDate: '', endDate: '', priority: 'MEDIUM', bomId: null, remark: ''
})

const rules = {
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入计划数量', trigger: 'blur' }],
  workshop: [{ required: true, message: '请输入车间', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择计划开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择计划结束日期', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, woNo: '', productName: '', quantity: 0, workshop: '', startDate: '', endDate: '', priority: 'MEDIUM', bomId: null, remark: '' })
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
