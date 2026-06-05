import { createRouter, createWebHistory } from 'vue-router'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Home from './views/Home.vue'
import OrderList from './views/OrderList.vue'
import Profile from './views/Profile.vue'
import WorkerAuth from './views/worker/Auth.vue'
import WorkerSkills from './views/worker/Skills.vue'
import WorkerEarnings from './views/worker/Earnings.vue'

const routes = [
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/', component: Home, meta: { requiresAuth: true } },
  { path: '/orders', component: OrderList, meta: { requiresAuth: true } },
  { path: '/profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/worker/auth', component: WorkerAuth, meta: { requiresAuth: true } },
  { path: '/worker/skills', component: WorkerSkills, meta: { requiresAuth: true } },
  { path: '/worker/earnings', component: WorkerEarnings, meta: { requiresAuth: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.meta.requiresAuth && !token) {
        next('/login');
    } else {
        next();
    }
});

export default router
