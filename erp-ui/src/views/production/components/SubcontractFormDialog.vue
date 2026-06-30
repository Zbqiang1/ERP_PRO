<template>
  <el-dialog v-model="visible" :title="title" width="600px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="委外单号" prop="contractNo">
            <el-input v-model="form.contractNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供应商" prop="supplierId">
            <el-select v-model="form.supplierId" placeholder="请选择供应商" filterable class="w-full">
              <el-option v-for="s in supplierOptions" :key="s.id" :label="s.supplierName" :value="s.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="物料名称" prop="materialName">
            <el-input v-model="form.materialName" placeholder="请输入物料名称" :maxlength="100" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数量" prop="quantity">
            <el-input-number v-model="form.quantity" :min="1" :precision="0" placeholder="请输入数量" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同价格" prop="contractPrice">
            <el-input-number v-model="form.contractPrice" :min="0" :precision="2" placeholder="请输入合同价格" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发出日期" prop="sendDate">
            <el-date-picker v-model="form.sendDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计返回" prop="expectedReturnDate">
            <el-date-picker v-model="form.expectedReturnDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
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
  data: Object, supplierOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({
  id: null, contractNo: '', supplierId: null, materialName: '',
  quantity: 0, contractPrice: 0, sendDate: '', expectedReturnDate: '', remark: ''
})

const rules = {
  supplierId: [{ required: true, message: '请选择供应商', trigger: 'change' }],
  materialName: [{ required: true, message: '请输入物料名称', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  contractPrice: [{ required: true, message: '请输入合同价格', trigger: 'blur' }],
  sendDate: [{ required: true, message: '请选择发出日期', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, contractNo: '', supplierId: null, materialName: '', quantity: 0, contractPrice: 0, sendDate: '', expectedReturnDate: '', remark: '' })
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
