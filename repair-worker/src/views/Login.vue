<template>
  <div class="login-page">
    <div class="brand-section">
      <div class="logo-circle">
        <van-icon name="manager" size="40" color="#2563EB" />
      </div>
      <h1>师傅端登录</h1>
      <p>专业服务 · 值得信赖</p>
    </div>

    <div class="login-card glass-card">
      <van-form @submit="onSubmit">
        <div class="input-group">
          <div class="label">手机号</div>
          <van-field
            v-model="username"
            name="username"
            placeholder="请输入手机号"
            :rules="[{ required: true, message: '请填写手机号' }]"
            class="custom-input"
          >
            <template #left-icon>
              <van-icon name="phone-o" class="input-icon" />
            </template>
          </van-field>
        </div>

        <div class="input-group">
          <div class="label">密码</div>
          <van-field
            v-model="password"
            type="password"
            name="password"
            placeholder="请输入密码"
            :rules="[{ required: true, message: '请填写密码' }]"
            class="custom-input"
          >
            <template #left-icon>
              <van-icon name="lock" class="input-icon" />
            </template>
          </van-field>
        </div>

        <div class="submit-btn-wrapper">
          <van-button 
            round 
            block 
            type="primary" 
            native-type="submit"
            class="btn-gradient submit-btn"
            :loading="loading"
          >
            登录
          </van-button>
        </div>
        
        <div class="form-footer">
          <router-link to="/register" class="link">注册新账号</router-link>
        </div>
      </van-form>

      <div class="debug-info">
        测试账号: 13900139000 / 123456
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showToast, showSuccessToast, showFailToast } from 'vant';

const username = ref('13900139000');
const password = ref('123456');
const loading = ref(false);
const router = useRouter();

const onSubmit = async (values) => {
  loading.value = true;
  try {
    const res = await axios.post('/api/auth/login', {
      username: values.username,
      password: values.password,
      role: 'WORKER'
    });
    
    if (res.data && res.data.token) {
      showSuccessToast('登录成功');
      localStorage.setItem('token', res.data.token);
      localStorage.setItem('userInfo', JSON.stringify(res.data));
      axios.defaults.headers.common['Authorization'] = res.data.token;
      setTimeout(() => {
        router.push('/');
      }, 500);
    } else {
      showFailToast('登录失败');
    }
  } catch (err) {
    console.error(err);
    showFailToast('登录失败: ' + (err.response?.data?.message || err.message));
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background-color: #f8fafc;
  padding: 0 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-image: url('https://images.unsplash.com/photo-1581092921461-eab62e97a783?ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80');
  background-size: cover;
  background-position: center;
  position: relative;
}

.login-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(37, 99, 235, 0.85);
  backdrop-filter: blur(5px);
}

.brand-section {
  position: relative;
  z-index: 1;
  text-align: center;
  margin-bottom: 40px;
  color: white;
}

.logo-circle {
  width: 80px;
  height: 80px;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
}

.brand-section h1 {
  font-size: 28px;
  margin-bottom: 8px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.brand-section p {
  opacity: 0.9;
  font-size: 16px;
  margin: 0;
}

.login-card {
  position: relative;
  z-index: 1;
  padding: 32px 24px;
}

.input-group {
  margin-bottom: 20px;
}

.input-group .label {
  font-size: 14px;
  color: #4B5563;
  margin-bottom: 8px;
  font-weight: 500;
  margin-left: 4px;
}

.custom-input {
  background: #F3F4F6;
  border-radius: 12px;
  padding: 12px 16px;
  border: 1px solid transparent;
  transition: all 0.3s;
}

.custom-input:focus-within {
  background: #fff;
  border-color: var(--primary-light);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.input-icon {
  font-size: 18px;
  color: #9CA3AF;
  margin-right: 8px;
}

.submit-btn-wrapper {
  margin-top: 32px;
}

.submit-btn {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.2);
}

.form-footer {
  margin-top: 20px;
  text-align: center;
}

.link {
  color: var(--primary-color);
  font-size: 14px;
  text-decoration: none;
  font-weight: 500;
}

.debug-info {
  margin-top: 30px;
  text-align: center;
  font-size: 12px;
  color: #9CA3AF;
  background: rgba(0,0,0,0.02);
  padding: 10px;
  border-radius: 8px;
}
</style>