<template>
  <el-dialog v-model="visible" :title="title" width="650px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="物料编码" prop="materialCode">
        <el-input v-model="form.materialCode" placeholder="请输入物料编码" :maxlength="50" />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input v-model="form.materialName" placeholder="请输入物料名称" :maxlength="100" />
      </el-form-item>
      <el-form-item label="物料类别" prop="categoryId">
        <el-select v-model="form.categoryId" placeholder="请选择类别" filterable class="w-full">
          <el-option v-for="item in categoryOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="规格" prop="spec">
        <el-input v-model="form.spec" placeholder="请输入规格" :maxlength="100" />
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-input v-model="form.unit" placeholder="请输入单位" :maxlength="20" />
      </el-form-item>
      <el-form-item label="安全库存" prop="safetyStock">
        <el-input-number v-model="form.safetyStock" :min="0" :precision="0" placeholder="请输入安全库存" class="w-full" />
      </el-form-item>
      <el-form-item label="最大库存" prop="maxStock">
        <el-input-number v-model="form.maxStock" :min="0" :precision="0" placeholder="请输入最大库存" class="w-full" />
      </el-form-item>
      <el-form-item label="最小库存" prop="minStock">
        <el-input-number v-model="form.minStock" :min="0" :precision="0" placeholder="请输入最小库存" class="w-full" />
      </el-form-item>
      <el-form-item label="单价" prop="unitPrice">
        <el-input-number v-model="form.unitPrice" :min="0" :precision="2" placeholder="请输入单价" class="w-full" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
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
  modelValue: { type: Boolean, default: false },
  title: { type: String, default: '' },
  mode: { type: String, default: 'add' },
  data: { type: Object, default: null },
  categoryOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null, materialCode: '', materialName: '', categoryId: null,
  spec: '', unit: '', safetyStock: 0, maxStock: 0, minStock: 0,
  unitPrice: 0, status: 1
})

const rules = {
  materialCode: [{ required: true, message: '请输入物料编码', trigger: 'blur' }],
  materialName: [{ required: true, message: '请输入物料名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择物料类别', trigger: 'change' }],
  unit: [{ required: true, message: '请输入单位', trigger: 'blur' }],
  safetyStock: [{ required: true, message: '请输入安全库存', trigger: 'blur' }],
  unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

watch(() => props.modelValue, (val) => { visible.value = val })
watch(visible, (val) => { emit('update:modelValue', val) })
watch(() => props.data, (val) => {
  if (val) Object.keys(form).forEach(k => { if (val[k] !== undefined) form[k] = val[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, materialCode: '', materialName: '', categoryId: null, spec: '', unit: '', safetyStock: 0, maxStock: 0, minStock: 0, unitPrice: 0, status: 1 })
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
