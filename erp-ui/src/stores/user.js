import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { setToken, getToken, removeToken } from '@/utils/auth'
import request from '@/utils/request'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(getToken() || '')
  const userId = ref('')
  const username = ref('')
  const realName = ref('')
  const avatar = ref('')
  const roles = ref([])
  const permissions = ref([])

  // Getters
  const isLoggedIn = computed(() => !!token.value)

  // Actions
  async function login(loginForm) {
    try {
      const res = await request({
        url: '/auth/login',
        method: 'post',
        data: loginForm
      })
      token.value = res.data.token
      setToken(res.data.token)
      return res
    } catch (error) {
      throw error
    }
  }

  async function getUserInfo() {
    try {
      const res = await request({
        url: '/auth/info',
        method: 'get'
      })
      const data = res.data
      userId.value = String(data.userId || data.id || '')
      username.value = data.username
      realName.value = data.realName || data.nickname || data.username
      avatar.value = data.avatar || ''
      roles.value = data.roles || []
      permissions.value = data.permissions || []
      return res
    } catch (error) {
      throw error
    }
  }

  async function logout() {
    try {
      await request({
        url: '/auth/logout',
        method: 'post'
      })
    } catch (error) {
      // 即使后端接口失败也要清除本地状态
      console.error('退出登录请求失败', error)
    } finally {
      resetState()
    }
  }

  function resetState() {
    token.value = ''
    userId.value = ''
    username.value = ''
    realName.value = ''
    avatar.value = ''
    roles.value = []
    permissions.value = []
    removeToken()
  }

  return {
    token,
    userId,
    username,
    realName,
    avatar,
    roles,
    permissions,
    isLoggedIn,
    login,
    getUserInfo,
    logout,
    resetState
  }
})
