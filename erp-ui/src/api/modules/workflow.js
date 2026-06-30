import request from '@/utils/request'

// processDefinition
export function listProcessDefinitions() {
  return request({ url: '/workflow/definitions', method: 'get' })
}

export function deployProcess(data) {
  return request({ url: '/workflow/definitions/deploy', method: 'post', data })
}

export function suspendProcess(id) {
  return request({ url: `/workflow/definitions/${id}/suspend`, method: 'post' })
}

// processInstance
export function startProcess(data) {
  return request({ url: '/workflow/instances/start', method: 'post', data })
}

export function myTasks(params) {
  return request({ url: '/workflow/tasks', method: 'get', params })
}

export function myDone(params) {
  return request({ url: '/workflow/tasks/done', method: 'get', params })
}

export function claimTask(taskId) {
  return request({ url: `/workflow/tasks/${taskId}/claim`, method: 'post' })
}

export function completeTask(taskId, data) {
  return request({ url: `/workflow/tasks/${taskId}/complete`, method: 'post', data })
}

export function delegateTask(taskId, userId) {
  return request({ url: `/workflow/tasks/${taskId}/delegate`, method: 'post', data: { userId } })
}

// taskHistory
export function listTaskHistoryByInstance(instanceId) {
  return request({ url: `/workflow/instances/${instanceId}/history`, method: 'get' })
}
