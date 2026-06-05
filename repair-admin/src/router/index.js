import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Layout from '../layout/index.vue'

const routes = [
  { 
    path: '/login', 
    component: Login,
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('../views/Home.vue'),
        meta: { title: '控制台', icon: 'Odometer' }
      },
      {
        path: 'users',
        component: () => import('../views/user/List.vue'),
        meta: { title: '用户列表', icon: 'User' }
      },
      {
        path: 'workers',
        component: () => import('../views/worker/Audit.vue'),
        meta: { title: '师傅审核', icon: 'Avatar' }
      },
      {
        path: 'orders',
        component: () => import('../views/order/List.vue'),
        meta: { title: '订单列表', icon: 'List' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router