import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // State
  const sidebarCollapsed = ref(false)
  const activeTheme = ref(localStorage.getItem('ERP_THEME') || 'default')
  const tagsViewList = ref([])
  const language = ref(localStorage.getItem('ERP_LANG') || 'zh-cn')

  // Actions
  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  function addTagView(tag) {
    const exists = tagsViewList.value.find((t) => t.path === tag.path)
    if (!exists) {
      tagsViewList.value.push({
        path: tag.path,
        name: tag.name,
        title: tag.title || tag.name,
        query: tag.query || {},
        meta: tag.meta || {}
      })
    }
  }

  function removeTagView(path) {
    const index = tagsViewList.value.findIndex((t) => t.path === path)
    if (index !== -1) {
      tagsViewList.value.splice(index, 1)
    }
  }

  function closeOtherTags(path) {
    tagsViewList.value = tagsViewList.value.filter((t) => t.path === path || t.meta?.affix)
  }

  function closeAllTags() {
    tagsViewList.value = tagsViewList.value.filter((t) => t.meta?.affix)
  }

  function setTheme(theme) {
    activeTheme.value = theme
    localStorage.setItem('ERP_THEME', theme)
    document.documentElement.setAttribute('data-theme', theme)
  }

  function setLanguage(lang) {
    language.value = lang
    localStorage.setItem('ERP_LANG', lang)
  }

  return {
    sidebarCollapsed,
    activeTheme,
    tagsViewList,
    language,
    toggleSidebar,
    addTagView,
    removeTagView,
    closeOtherTags,
    closeAllTags,
    setTheme,
    setLanguage
  }
})
