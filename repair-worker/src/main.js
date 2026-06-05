import { createApp } from 'vue'
import App from './App.vue'
import Vant from 'vant';
import 'vant/lib/index.css';
import './style.css';
import router from './router';
import axios from 'axios';

const token = localStorage.getItem('token');
if (token) {
    axios.defaults.headers.common['Authorization'] = token;
}

const app = createApp(App);
app.use(Vant);
app.use(router);
app.mount('#app');
