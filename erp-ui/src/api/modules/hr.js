import request from '@/utils/request'

// organization
export function pageOrganization(params) {
  return request({ url: '/hr/organizations', method: 'get', params })
}

export function getOrganizationById(id) {
  return request({ url: `/hr/organizations/${id}`, method: 'get' })
}

export function addOrganization(data) {
  return request({ url: '/hr/organizations', method: 'post', data })
}

export function updateOrganization(id, data) {
  return request({ url: `/hr/organizations/${id}`, method: 'put', data })
}

export function deleteOrganization(id) {
  return request({ url: `/hr/organizations/${id}`, method: 'delete' })
}

export function getOrganizationTree() {
  return request({ url: '/hr/organizations/tree', method: 'get' })
}

// employee
export function pageEmployee(params) {
  return request({ url: '/hr/employees', method: 'get', params })
}

export function getEmployeeById(id) {
  return request({ url: `/hr/employees/${id}`, method: 'get' })
}

export function addEmployee(data) {
  return request({ url: '/hr/employees', method: 'post', data })
}

export function updateEmployee(id, data) {
  return request({ url: `/hr/employees/${id}`, method: 'put', data })
}

export function deleteEmployee(id) {
  return request({ url: `/hr/employees/${id}`, method: 'delete' })
}

// attendance
export function pageAttendance(params) {
  return request({ url: '/hr/attendances', method: 'get', params })
}

export function getAttendanceById(id) {
  return request({ url: `/hr/attendances/${id}`, method: 'get' })
}

export function addAttendance(data) {
  return request({ url: '/hr/attendances', method: 'post', data })
}

export function updateAttendance(id, data) {
  return request({ url: `/hr/attendances/${id}`, method: 'put', data })
}

export function deleteAttendance(id) {
  return request({ url: `/hr/attendances/${id}`, method: 'delete' })
}

export function batchImportAttendance(data) {
  return request({ url: '/hr/attendances/batch', method: 'post', data })
}

// leaveSheet
export function pageLeaveSheet(params) {
  return request({ url: '/hr/leaves', method: 'get', params })
}

export function getLeaveSheetById(id) {
  return request({ url: `/hr/leaves/${id}`, method: 'get' })
}

export function addLeaveSheet(data) {
  return request({ url: '/hr/leaves', method: 'post', data })
}

export function updateLeaveSheet(id, data) {
  return request({ url: `/hr/leaves/${id}`, method: 'put', data })
}

export function deleteLeaveSheet(id) {
  return request({ url: `/hr/leaves/${id}`, method: 'delete' })
}

export function approveLeave(id, approver) {
  return request({ url: `/hr/leaves/${id}/approve`, method: 'put', params: { approver } })
}

export function rejectLeave(id, approver) {
  return request({ url: `/hr/leaves/${id}/reject`, method: 'put', params: { approver } })
}

// salarySheet
export function pageSalarySheet(params) {
  return request({ url: '/hr/salaries', method: 'get', params })
}

export function getSalarySheetById(id) {
  return request({ url: `/hr/salaries/${id}`, method: 'get' })
}

export function addSalarySheet(data) {
  return request({ url: '/hr/salaries', method: 'post', data })
}

export function updateSalarySheet(id, data) {
  return request({ url: `/hr/salaries/${id}`, method: 'put', data })
}

export function deleteSalarySheet(id) {
  return request({ url: `/hr/salaries/${id}`, method: 'delete' })
}

export function calculateSalary(month) {
  return request({ url: '/hr/salaries/calculate', method: 'post', data: { month } })
}

// performance
export function pagePerformance(params) {
  return request({ url: '/hr/performances', method: 'get', params })
}

export function getPerformanceById(id) {
  return request({ url: `/hr/performances/${id}`, method: 'get' })
}

export function addPerformance(data) {
  return request({ url: '/hr/performances', method: 'post', data })
}

export function updatePerformance(id, data) {
  return request({ url: `/hr/performances/${id}`, method: 'put', data })
}

export function deletePerformance(id) {
  return request({ url: `/hr/performances/${id}`, method: 'delete' })
}

// ── aliases matching Vue file usage (namePattern instead of verbPattern) ──
export const employeePage = pageEmployee
export const employeeAdd = addEmployee
export const employeeUpdate = updateEmployee
export const employeeDelete = deleteEmployee
export const employeeGetById = getEmployeeById
export const employeeListAll = () => request({ url: '/hr/employees', method: 'get', params: { pageSize: 9999 } })

export const organizationPage = pageOrganization
export const organizationAdd = addOrganization
export const organizationUpdate = updateOrganization
export const organizationDelete = deleteOrganization
export const organizationGetById = getOrganizationById
export const organizationTree = getOrganizationTree
export const organizationListAll = () => request({ url: '/hr/organizations', method: 'get', params: { pageSize: 9999 } })

export const attendancePage = pageAttendance
export const attendanceAdd = addAttendance
export const attendanceUpdate = updateAttendance
export const attendanceDelete = deleteAttendance
export const attendanceGetById = getAttendanceById
export const attendanceBatchImport = batchImportAttendance

export const leaveSheetPage = pageLeaveSheet
export const leaveSheetAdd = addLeaveSheet
export const leaveSheetUpdate = updateLeaveSheet
export const leaveSheetDelete = deleteLeaveSheet
export const leaveSheetGetById = getLeaveSheetById
export const leaveSheetApprove = approveLeave
export const leaveSheetReject = rejectLeave

export const salarySheetPage = pageSalarySheet
export const salarySheetAdd = addSalarySheet
export const salarySheetUpdate = updateSalarySheet
export const salarySheetDelete = deleteSalarySheet
export const salarySheetGetById = getSalarySheetById
export const salarySheetCalculate = calculateSalary

export const performancePage = pagePerformance
export const performanceAdd = addPerformance
export const performanceUpdate = updatePerformance
export const performanceDelete = deletePerformance
export const performanceGetById = getPerformanceById
export const performanceHistoryByEmployee = (employeeId) => request({ url: `/hr/performances/history/${employeeId}`, method: 'get' })
