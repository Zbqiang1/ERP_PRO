<template>
  <el-dialog v-model="visible" :title="title" width="550px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="资产编号" prop="assetNo">
            <el-input v-model="form.assetNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产名称" prop="assetName">
            <el-input v-model="form.assetName" placeholder="请输入资产名称" :maxlength="100" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产类别" prop="category">
            <el-select v-model="form.category" placeholder="请选择类别" class="w-full">
              <el-option label="电子设备" value="ELECTRONIC" />
              <el-option label="机械设备" value="MACHINERY" />
              <el-option label="房屋建筑" value="BUILDING" />
              <el-option label="运输工具" value="VEHICLE" />
              <el-option label="办公设备" value="OFFICE" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用部门" prop="deptName">
            <el-input v-model="form.deptName" placeholder="请输入使用部门" :maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原值" prop="originalValue">
            <el-input-number v-model="form.originalValue" :min="0" :precision="2" placeholder="请输入原值" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="净值" prop="currentValue">
            <el-input-number v-model="form.currentValue" :min="0" :precision="2" placeholder="请输入净值" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="折旧率(%)" prop="depreciationRate">
            <el-input-number v-model="form.depreciationRate" :min="0" :max="100" :precision="2" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="购入日期" prop="purchaseDate">
            <el-date-picker v-model="form.purchaseDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>
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
  id: null, assetNo: '', assetName: '', category: '', deptName: '',
  originalValue: 0, currentValue: 0, depreciationRate: 0, purchaseDate: ''
})

const rules = {
  assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择资产类别', trigger: 'change' }],
  deptName: [{ required: true, message: '请输入使用部门', trigger: 'blur' }],
  originalValue: [{ required: true, message: '请输入原值', trigger: 'blur' }],
  currentValue: [{ required: true, message: '请输入净值', trigger: 'blur' }],
  purchaseDate: [{ required: true, message: '请选择购入日期', trigger: 'change' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, assetNo: '', assetName: '', category: '', deptName: '', originalValue: 0, currentValue: 0, depreciationRate: 0, purchaseDate: '' })
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
