import dayjs from 'dayjs'

/**
 * 格式化日期
 * @param {string|Date|number} date - 日期
 * @param {string} pattern - 格式模板，默认 'YYYY-MM-DD HH:mm:ss'
 * @returns {string}
 */
export function formatDate(date, pattern = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''
  return dayjs(date).format(pattern)
}

/**
 * 格式化金额
 * @param {number} amount - 金额数值
 * @returns {string} 格式化后的金额字符串，如 ¥1,234.56
 */
export function formatMoney(amount) {
  if (amount === null || amount === undefined || amount === '') return ''
  const num = Number(amount)
  if (isNaN(num)) return ''
  const negative = num < 0
  const abs = Math.abs(num)
  const parts = abs.toFixed(2).split('.')
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  return `${negative ? '-' : ''}¥${parts.join('.')}`
}

/**
 * 格式化文件大小
 * @param {number} bytes - 字节数
 * @returns {string}
 */
export function formatFileSize(bytes) {
  if (bytes === null || bytes === undefined || bytes === '') return ''
  const num = Number(bytes)
  if (isNaN(num) || num === 0) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  const k = 1024
  const i = Math.floor(Math.log(num) / Math.log(k))
  const size = (num / Math.pow(k, i)).toFixed(2)
  return `${size} ${units[i]}`
}
