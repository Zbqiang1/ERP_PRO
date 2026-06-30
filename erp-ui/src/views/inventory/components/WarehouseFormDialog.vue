<template>
  <el-dialog
    v-model="visible"
    :title="title"
    width="600px"
    :close-on-click-modal="false"
    destroy-on-close
    @close="handleCancel"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-form-item label="仓库编码" prop="warehouseCode">
        <el-input v-model="form.warehouseCode" placeholder="请输入仓库编码" :maxlength="50" />
      </el-form-item>
      <el-form-item label="仓库名称" prop="warehouseName">
        <el-input v-model="form.warehouseName" placeholder="请输入仓库名称" :maxlength="100" />
      </el-form-item>
      <el-form-item label="仓库地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入仓库地址" :maxlength="200" />
      </el-form-item>
      <el-form-item label="负责人" prop="manager">
        <el-input v-model="form.manager" placeholder="请输入负责人" :maxlength="50" />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系电话" :maxlength="20" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button v-if="mode !== 'detail'" type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  title: { type: String, default: '' },
  mode: { type: String, default: 'add' },
  data: { type: Object, default: null }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])

const visible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  warehouseCode: '',
  warehouseName: '',
  address: '',
  manager: '',
  phone: '',
  status: 1
})

const rules = {
  warehouseCode: [{ required: true, message: '请输入仓库编码', trigger: 'blur' }],
  warehouseName: [{ required: true, message: '请输入仓库名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入仓库地址', trigger: 'blur' }],
  manager: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$|^\d{3,4}-?\d{7,8}$/, message: '请输入正确的联系电话', trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

watch(() => props.modelValue, (val) => { visible.value = val })
watch(visible, (val) => { emit('update:modelValue', val) })

watch(() => props.data, (val) => {
  if (val) {
    form.id = val.id
    form.warehouseCode = val.warehouseCode || ''
    form.warehouseName = val.warehouseName || ''
    form.address = val.address || ''
    form.manager = val.manager || ''
    form.phone = val.phone || ''
    form.status = val.status ?? 1
  }
}, { immediate: true })

const resetForm = () => {
  form.id = null
  form.warehouseCode = ''
  form.warehouseName = ''
  form.address = ''
  form.manager = ''
  form.phone = ''
  form.status = 1
}

const handleCancel = () => {
  visible.value = false
  resetForm()
  emit('cancel')
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  emit('confirm', { ...form })
}
</script>
