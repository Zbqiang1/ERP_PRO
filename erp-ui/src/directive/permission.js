import { hasPerm, hasRole } from '@/utils/permission'

/**
 * v-permission 指令
 *
 * 用法:
 *   <el-button v-permission="'user:add'">新增用户</el-button>
 *   <el-button v-permission:role="'admin'">管理员操作</el-button>
 *   <div v-permission="{ perm: 'user:edit', role: 'admin' }">需要权限和角色</div>
 */
const permissionDirective = {
  mounted(el, binding) {
    const { arg, value } = binding

    let hasAccess = true

    // v-permission:role="'admin'" — 检查角色
    if (arg === 'role') {
      hasAccess = hasRole(value)
    }
    // v-permission="{ perm: 'xxx', role: 'xxx' }" — 检查权限和角色
    else if (typeof value === 'object' && value !== null) {
      if (value.perm) {
        hasAccess = hasPerm(value.perm)
      }
      if (hasAccess && value.role) {
        hasAccess = hasRole(value.role)
      }
    }
    // v-permission="'user:add'" — 只检查权限码
    else {
      hasAccess = hasPerm(value)
    }

    if (!hasAccess) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  }
}

export default permissionDirective
