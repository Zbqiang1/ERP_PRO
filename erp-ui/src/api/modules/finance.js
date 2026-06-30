import request from '@/utils/request'

// voucher
export function pageVoucher(params) {
  return request({ url: '/finance/ledger/vouchers', method: 'get', params })
}

export function getVoucherById(id) {
  return request({ url: `/finance/ledger/vouchers/${id}`, method: 'get' })
}

export function addVoucher(data) {
  return request({ url: '/finance/ledger/vouchers', method: 'post', data })
}

export function updateVoucher(id, data) {
  return request({ url: `/finance/ledger/vouchers/${id}`, method: 'put', data })
}

export function deleteVoucher(id) {
  return request({ url: `/finance/ledger/vouchers/${id}`, method: 'delete' })
}

export function auditVoucher(id) {
  return request({ url: `/finance/ledger/vouchers/${id}/audit`, method: 'post' })
}

export function postVoucher(id) {
  return request({ url: `/finance/ledger/vouchers/${id}/post`, method: 'post' })
}

// receivable
export function pageReceivable(params) {
  return request({ url: '/finance/receivables', method: 'get', params })
}

export function getReceivableById(id) {
  return request({ url: `/finance/receivables/${id}`, method: 'get' })
}

export function addReceivable(data) {
  return request({ url: '/finance/receivables', method: 'post', data })
}

export function updateReceivable(id, data) {
  return request({ url: `/finance/receivables/${id}`, method: 'put', data })
}

export function deleteReceivable(id) {
  return request({ url: `/finance/receivables/${id}`, method: 'delete' })
}

export function collectReceivable(data) {
  return request({ url: '/finance/receivables/collect', method: 'post', data })
}

// payable
export function pagePayable(params) {
  return request({ url: '/finance/payables', method: 'get', params })
}

export function getPayableById(id) {
  return request({ url: `/finance/payables/${id}`, method: 'get' })
}

export function addPayable(data) {
  return request({ url: '/finance/payables', method: 'post', data })
}

export function updatePayable(id, data) {
  return request({ url: `/finance/payables/${id}`, method: 'put', data })
}

export function deletePayable(id) {
  return request({ url: `/finance/payables/${id}`, method: 'delete' })
}

export function payPayable(data) {
  return request({ url: '/finance/payables/pay', method: 'post', data })
}

// fixedAsset
export function pageFixedAsset(params) {
  return request({ url: '/finance/fixed-assets', method: 'get', params })
}

export function getFixedAssetById(id) {
  return request({ url: `/finance/fixed-assets/${id}`, method: 'get' })
}

export function addFixedAsset(data) {
  return request({ url: '/finance/fixed-assets', method: 'post', data })
}

export function updateFixedAsset(id, data) {
  return request({ url: `/finance/fixed-assets/${id}`, method: 'put', data })
}

export function deleteFixedAsset(id) {
  return request({ url: `/finance/fixed-assets/${id}`, method: 'delete' })
}

// costSheet
export function pageCostSheet(params) {
  return request({ url: '/finance/cost-sheets', method: 'get', params })
}

export function getCostSheetById(id) {
  return request({ url: `/finance/cost-sheets/${id}`, method: 'get' })
}

export function addCostSheet(data) {
  return request({ url: '/finance/cost-sheets', method: 'post', data })
}

export function updateCostSheet(id, data) {
  return request({ url: `/finance/cost-sheets/${id}`, method: 'put', data })
}

export function deleteCostSheet(id) {
  return request({ url: `/finance/cost-sheets/${id}`, method: 'delete' })
}

// taxInvoice
export function pageTaxInvoice(params) {
  return request({ url: '/finance/tax-invoices', method: 'get', params })
}

export function getTaxInvoiceById(id) {
  return request({ url: `/finance/tax-invoices/${id}`, method: 'get' })
}

export function addTaxInvoice(data) {
  return request({ url: '/finance/tax-invoices', method: 'post', data })
}

export function updateTaxInvoice(id, data) {
  return request({ url: `/finance/tax-invoices/${id}`, method: 'put', data })
}

export function deleteTaxInvoice(id) {
  return request({ url: `/finance/tax-invoices/${id}`, method: 'delete' })
}

// accountSubject
export function pageAccountSubject(params) {
  return request({ url: '/finance/account-subjects', method: 'get', params })
}

export function getAccountSubjectById(id) {
  return request({ url: `/finance/account-subjects/${id}`, method: 'get' })
}

export function addAccountSubject(data) {
  return request({ url: '/finance/account-subjects', method: 'post', data })
}

export function updateAccountSubject(id, data) {
  return request({ url: `/finance/account-subjects/${id}`, method: 'put', data })
}

export function deleteAccountSubject(id) {
  return request({ url: `/finance/account-subjects/${id}`, method: 'delete' })
}

export function getAccountSubjectTree() {
  return request({ url: '/finance/account-subjects/tree', method: 'get' })
}

// ── aliases matching Vue file usage (namePattern instead of verbPattern) ──
export const receivablePage = pageReceivable
export const receivableAdd = addReceivable
export const receivableUpdate = updateReceivable
export const receivableDelete = deleteReceivable
export const receivableGetById = getReceivableById
export const receivableCollect = collectReceivable

export const payablePage = pagePayable
export const payableAdd = addPayable
export const payableUpdate = updatePayable
export const payableDelete = deletePayable
export const payableGetById = getPayableById
export const payablePay = payPayable

export const fixedAssetPage = pageFixedAsset
export const fixedAssetAdd = addFixedAsset
export const fixedAssetUpdate = updateFixedAsset
export const fixedAssetDelete = deleteFixedAsset
export const fixedAssetGetById = getFixedAssetById
export const fixedAssetDepreciation = (data) => request({ url: '/finance/fixed-assets/depreciation', method: 'post', data })

export const costSheetPage = pageCostSheet
export const costSheetAdd = addCostSheet
export const costSheetUpdate = updateCostSheet
export const costSheetDelete = deleteCostSheet
export const costSheetGetById = getCostSheetById

export const taxInvoicePage = pageTaxInvoice
export const taxInvoiceAdd = addTaxInvoice
export const taxInvoiceUpdate = updateTaxInvoice
export const taxInvoiceDelete = deleteTaxInvoice
export const taxInvoiceGetById = getTaxInvoiceById

export const voucherPage = pageVoucher
export const voucherAdd = addVoucher
export const voucherUpdate = updateVoucher
export const voucherDelete = deleteVoucher
export const voucherGetById = getVoucherById
export const voucherAudit = auditVoucher
export const voucherPost = postVoucher

export const accountSubjectTree = getAccountSubjectTree
export const accountSubjectAdd = addAccountSubject
export const accountSubjectUpdate = updateAccountSubject
export const accountSubjectDelete = deleteAccountSubject
export const accountSubjectGetById = getAccountSubjectById
