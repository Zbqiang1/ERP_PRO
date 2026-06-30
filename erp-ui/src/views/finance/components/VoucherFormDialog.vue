<template>
  <el-dialog v-model="visible" :title="title" width="850px" :close-on-click-modal="false" destroy-on-close @close="handleCancel">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" :disabled="mode === 'detail'">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-form-item label="凭证号" prop="voucherNo">
            <el-input v-model="form.voucherNo" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="凭证日期" prop="voucherDate">
            <el-date-picker v-model="form.voucherDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" class="w-full" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="凭证类型" prop="voucherType">
            <el-select v-model="form.voucherType" placeholder="请选择类型" class="w-full">
              <el-option label="记账" value="JOURNAL" />
              <el-option label="转账" value="TRANSFER" />
              <el-option label="调整" value="ADJUST" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </el-form-item>

      <h4 class="mb-8">分录明细</h4>
      <el-table :data="form.entries" border stripe size="small" class="w-full mb-8">
        <el-table-column label="科目" min-width="150">
          <template #default="{ row: r }">
            <el-select v-model="r.subjectId" placeholder="选择科目" filterable size="small">
              <el-option v-for="s in subjectOptions" :key="s.id" :label="s.subjectName" :value="s.id" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="摘要" min-width="180">
          <template #default="{ row: r }">
            <el-input v-model="r.summary" size="small" placeholder="摘要" />
          </template>
        </el-table-column>
        <el-table-column label="借方金额" width="130">
          <template #default="{ row: r }">
            <el-input-number v-model="r.debitAmount" :min="0" :precision="2" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="贷方金额" width="130">
          <template #default="{ row: r }">
            <el-input-number v-model="r.creditAmount" :min="0" :precision="2" size="small" controls-position="right" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="70" fixed="right">
          <template #default="{ $index }">
            <el-button v-if="mode !== 'detail'" type="danger" link size="small" @click="removeEntry($index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="mb-8">借方合计: {{ debitTotal.toFixed(2) }} | 贷方合计: {{ creditTotal.toFixed(2) }}</div>
      <el-button v-if="mode !== 'detail'" type="primary" size="small" icon="Plus" @click="addEntry">添加分录</el-button>
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
  data: Object, subjectOptions: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])
const visible = ref(false); const formRef = ref(null)

const emptyEntry = () => ({ subjectId: null, summary: '', debitAmount: 0, creditAmount: 0 })
const form = reactive({
  id: null, voucherNo: '', voucherDate: '', voucherType: '', remark: '', entries: [emptyEntry()]
})

const rules = {
  voucherDate: [{ required: true, message: '请选择凭证日期', trigger: 'change' }],
  voucherType: [{ required: true, message: '请选择凭证类型', trigger: 'change' }]
}

const debitTotal = computed(() => form.entries.reduce((s, e) => s + (e.debitAmount || 0), 0))
const creditTotal = computed(() => form.entries.reduce((s, e) => s + (e.creditAmount || 0), 0))

watch(() => props.modelValue, (v) => { visible.value = v })
watch(visible, (v) => { emit('update:modelValue', v) })
watch(() => props.data, (v) => {
  if (v) {
    Object.keys(form).forEach(k => { if (v[k] !== undefined) { if (k === 'entries' && Array.isArray(v[k])) form.entries = v[k].map(i => ({ ...emptyEntry(), ...i })); else form[k] = v[k] } })
    if (!form.entries.length) form.entries = [emptyEntry()]
  } else resetForm()
}, { immediate: true })

const resetForm = () => {
  Object.assign(form, { id: null, voucherNo: '', voucherDate: '', voucherType: '', remark: '', entries: [emptyEntry()] })
}
const addEntry = () => form.entries.push(emptyEntry())
const removeEntry = (idx) => { if (form.entries.length > 1) form.entries.splice(idx, 1) }
const handleCancel = () => { visible.value = false; resetForm(); emit('cancel') }
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  emit('confirm', { ...form, totalDebit: debitTotal.value, totalCredit: creditTotal.value })
}
</script>

<style scoped>
.w-full { width: 100%; } .mb-8 { margin-bottom: 8px; }
</style>
