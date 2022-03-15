import TabsView from '@/layouts/tabs/TabsView'
import BlankView from '@/layouts/BlankView'
// import PageView from '@/layouts/PageView'

// 路由配置
const options = {
  routes: [
    {
      path: '/login',
      name: '登录页',
      component: () => import('@/pages/login')
    },
    {
      path: '*',
      name: '404',
      component: () => import('@/pages/exception/404'),
    },
    {
      path: '/403',
      name: '403',
      component: () => import('@/pages/exception/403'),
    },
    {
      path: '/',
      name: '首页',
      component: TabsView,
      redirect: '/login',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          meta: {
            icon: 'dashboard'
          },
          component: BlankView,
          children: [
            {
              path: 'console',
              name: '控制台',
              component: () => import('@/pages/dashboard/console'),
            },
            {
              path: 'connections',
              name: '连接信息',
              component: () => import('@/pages/dashboard/connections'),
            },
            {
              path: 'subscribes',
              name: '订阅信息',
              component: () => import('@/pages/dashboard/subscribes'),
            },
            {
              path: 'publish',
              name: '推送信息',
              component: () => import('@/pages/dashboard/publish'),
            },
            {
              path: 'acl',
              name: '权限校验',
              component: () => import('@/pages/dashboard/acl'),
            }

          ]
        }
      ]
    },
  ]
}

export default options
