<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  // 对话框是否可见
  visible: {
    type: Boolean,
    default: false
  },
  // 模式: create / edit
  mode: {
    type: String,
    default: 'create',
    validator: (val) => ['create', 'edit'].includes(val)
  },
  // 表单数据
  formData: {
    type: Object,
    default: () => ({})
  },
  // 表单校验规则
  rules: {
    type: Object,
    default: () => ({})
  },
  // 表单宽度
  labelWidth: {
    type: String,
    default: '100px'
  },
  // 对话框标题
  title: {
    type: String,
    default: ''
  },
  // 对话框宽度
  width: {
    type: String,
    default: '600px'
  },
  // 是否显示全屏按钮
  fullscreen: {
    type: Boolean,
    default: false
  },
  // 确认按钮 loading
  confirmLoading: {
    type: Boolean,
    default: false
  },
  // 是否可拖拽
  draggable: {
    type: Boolean,
    default: false
  },
  // 关闭时是否重置
  resetOnClose: {
    type: Boolean,
    default: true
  },
  // 是否在关闭前询问
  closeOnClickModal: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:visible', 'confirm', 'cancel', 'closed'])

const formRef = ref(null)

// 对话框标题
const dialogTitle = computed(() => {
  if (props.title) return props.title
  return props.mode === 'create' ? '新增' : '编辑'
})

// 对话框可见性双向绑定
const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

// 确认
async function handleConfirm() {
  if (!formRef.value) {
    emit('confirm')
    return
  }
  try {
    await formRef.value.validate()
    emit('confirm')
  } catch (error) {
    ElMessage.warning('请完善表单信息')
  }
}

// 取消
function handleCancel() {
  emit('cancel')
  dialogVisible.value = false
}

// 关闭
function handleClosed() {
  if (props.resetOnClose && formRef.value) {
    formRef.value.resetFields()
  }
  emit('closed')
}

// 暴露 validate / resetFields 方法
function validate() {
  return formRef.value ? formRef.value.validate() : Promise.resolve()
}

function resetFields() {
  formRef.value && formRef.value.resetFields()
}

defineExpose({ validate, resetFields })
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    :width="width"
    :fullscreen="fullscreen"
    :draggable="draggable"
    :close-on-click-modal="closeOnClickModal"
    destroy-on-close
    @closed="handleClosed"
  >
    <!-- 表单内容 -->
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      :label-width="labelWidth"
      class="form-dialog-content"
    >
      <!-- 默认插槽：表单字段 -->
      <slot />
    </el-form>

    <!-- 底部按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" :loading="confirmLoading" @click="handleConfirm">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.form-dialog-content {
  padding: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
