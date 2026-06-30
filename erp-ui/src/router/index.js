import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

// 公开路由（不需要动态加载）
const publicRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '404', hidden: true }
  }
]

// 主布局路由 — 其 children 动态注入
const mainLayoutRoute = {
  path: '/',
  name: 'Layout',
  component: () => import('@/layout/index.vue'),
  redirect: '/dashboard',
  meta: { title: '首页' },
  children: [
    {
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index.vue'),
      meta: { title: '首页', icon: 'HomeFilled', affix: true }
    }
  ]
}

const router = createRouter({
  history: createWebHistory(),
  routes: [
    ...publicRoutes,
    mainLayoutRoute,
    {
      path: '/:pathMatch(.*)*',
      redirect: '/404',
      meta: { hidden: true }
    }
  ],
  scrollBehavior: () => ({ top: 0 })
})

// 是否已加载动态路由
let hasAddedRoutes = false

// 简易 loading bar
function startLoading() {
  const bar = document.getElementById('nprogress-bar')
  if (bar) {
    bar.style.width = '0%'
    bar.style.opacity = '1'
  }
}

function doneLoading() {
  const bar = document.getElementById('nprogress-bar')
  if (bar) {
    bar.style.width = '100%'
    bar.style.opacity = '0'
  }
}

// 路由守卫
router.beforeEach(async (to, from, next) => {
  startLoading()

  // 设置页面标题
  if (to.meta && to.meta.title) {
    document.title = `${to.meta.title} - ERP系统`
  }

  const token = getToken()

  // 已登录
  if (token) {
    // 如果访问登录页，直接跳转到首页
    if (to.path === '/login') {
      return next({ path: '/' })
    }

    // 如果还没有加载动态路由
    if (!hasAddedRoutes) {
      const userStore = useUserStore()
      const permissionStore = usePermissionStore()

      try {
        // 获取用户信息
        if (!userStore.userId) {
          await userStore.getUserInfo()
        }

        // 获取动态路由
        const dynamicRoutes = await permissionStore.generateRoutes()

        // 将动态路由添加为 Layout 的 children
        const layoutRoute = router.getRoutes().find((r) => r.name === 'Layout')
        if (layoutRoute) {
          dynamicRoutes.forEach((route) => {
            router.addRoute('Layout', route)
          })
        }

        hasAddedRoutes = true
        // 重新解析当前路径，匹配动态路由
        next({ ...to, replace: true })
        return
      } catch (error) {
        console.error('获取用户信息或路由失败', error)
        // 清除 token 并跳转到登录页
        userStore.resetState()
        permissionStore.resetRoutes()
        hasAddedRoutes = false
        next(`/login?redirect=${to.path}`)
        return
      }
    }

    next()
  } else {
    // 未登录
    if (to.path === '/login') {
      next()
    } else {
      // 记住要跳转的页面
      next(`/login?redirect=${to.path}`)
    }
  }
})

router.afterEach(() => {
  doneLoading()
})

export default router
