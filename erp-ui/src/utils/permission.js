import { useUserStore } from '@/stores/user'

/**
 * 检查是否有指定权限码
 * @param {string} permCode - 权限码
 * @returns {boolean}
 */
export function hasPerm(permCode) {
  if (!permCode) return true
  const userStore = useUserStore()
  const permissions = userStore.permissions || []
  // 管理员拥有所有权限
  if (userStore.roles && userStore.roles.includes('admin')) return true
  return permissions.includes(permCode)
}

/**
 * 检查是否有指定角色
 * @param {string} roleCode - 角色编码
 * @returns {boolean}
 */
export function hasRole(roleCode) {
  if (!roleCode) return true
  const userStore = useUserStore()
  const roles = userStore.roles || []
  // 管理员拥有所有角色
  if (roles.includes('admin')) return true
  return roles.includes(roleCode)
}
