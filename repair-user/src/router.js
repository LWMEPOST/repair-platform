import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import CreateOrder from './views/CreateOrder.vue'
import OrderList from './views/OrderList.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import ArticleDetail from './views/article/Detail.vue'
import Profile from './views/Profile.vue'
import EditProfile from './views/EditProfile.vue'
import OrderDetail from './views/order/Detail.vue'
import WorkerProfile from './views/order/WorkerProfile.vue'
import RateOrder from './views/order/Rate.vue'
import CustomerService from './views/common/CustomerService.vue'
import AboutUs from './views/common/AboutUs.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/create-order', component: CreateOrder, meta: { requiresAuth: true } },
  { path: '/orders', component: OrderList, meta: { requiresAuth: true } },
  { path: '/order/:id/rate', component: RateOrder, meta: { requiresAuth: true } },
  { path: '/order/:id', component: OrderDetail, meta: { requiresAuth: true } },
  { path: '/worker/profile/:id', component: WorkerProfile, meta: { requiresAuth: true } },
  { path: '/service', component: CustomerService },
  { path: '/about', component: AboutUs },
  { path: '/article/:id', component: ArticleDetail },
  { path: '/profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/edit-profile', component: EditProfile, meta: { requiresAuth: true } }
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
