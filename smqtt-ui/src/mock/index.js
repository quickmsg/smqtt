import Mock from 'mockjs'
import '@/mock/user/current'

// 设置全局延时
Mock.setup({
  timeout: '200-400'
})
