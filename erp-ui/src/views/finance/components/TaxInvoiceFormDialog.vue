<template>
  <el-dialog v-model="visible" :title="title" width="600px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="发票号" prop="invoiceNo">
            <el-input v-model="form.invoiceNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票类型" prop="invoiceType">
            <el-select v-model="form.invoiceType" placeholder="请选择类型" class="w-full">
              <el-option label="增值税专用发票" value="VAT_SPECIAL" />
              <el-option label="增值税普通发票" value="VAT_NORMAL" />
              <el-option label="电子发票" value="E_INVOICE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票日期" prop="invoiceDate">
            <el-date-picker v-model="form.invoiceDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="购方名称" prop="buyerName">
            <el-input v-model="form.buyerName" placeholder="请输入购方名称" :maxlength="100" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="销方名称" prop="sellerName">
            <el-input v-model="form.sellerName" placeholder="请输入销方名称" :maxlength="100" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="金额" prop="amount">
            <el-input-number v-model="form.amount" :min="0" :precision="2" placeholder="金额" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税率(%)" prop="taxRate">
            <el-input-number v-model="form.taxRate" :min="0" :max="100" :precision="2" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="税额" prop="taxAmount">
            <el-input-number v-model="form.taxAmount" :min="0" :precision="2" :disabled="true" class="w-full" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="价税合计">
        <strong class="total-amount">{{ totalAmount.toFixed(2) }}</strong>
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
  modelValue: Boolean, title: String, mode: { type: String, default: 'add' }, data: Object
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const form = reactive({
  id: null, invoiceNo: '', invoiceType: '', invoiceDate: '',
  buyerName: '', sellerName: '', amount: 0, taxRate: 0, taxAmount: 0
})

const totalAmount = computed(() => (form.amount || 0) + (form.taxAmount || 0))

watch([() => form.amount, () => form.taxRate], () => {
  form.taxAmount = Number(((form.amount || 0) * (form.taxRate || 0) / 100).toFixed(2))
})

const rules = {
  invoiceType: [{ required: true, message: '请选择发票类型', trigger: 'change' }],
  invoiceDate: [{ required: true, message: '请选择开票日期', trigger: 'change' }],
  buyerName: [{ required: true, message: '请输入购方名称', trigger: 'blur' }],
  sellerName: [{ required: true, message: '请输入销方名称', trigger: 'blur' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  taxRate: [{ required: true, message: '请输入税率', trigger: 'blur' }]
}

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) Object.keys(form).forEach(k => { if (v[k] !== undefined) form[k] = v[k] })
  else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, invoiceNo: '', invoiceType: '', invoiceDate: '', buyerName: '', sellerName: '', amount: 0, taxRate: 0, taxAmount: 0 })
}
const handleCancel = () => { visible.value = false; resetForm(); emit('cancel') }
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  emit('confirm', { ...form, taxAmount: form.taxAmount, totalAmount: totalAmount.value })
}
</script>

<style scoped>
.w-full { width: 100%; }
.total-amount { font-size: 18px; color: #409eff; }
</style>
