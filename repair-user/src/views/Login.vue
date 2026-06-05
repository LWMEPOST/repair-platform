<template>
  <div class="login-page">
    <div class="login-content">
      <div class="brand-section">
        <div class="logo-circle">
          <van-icon name="wap-home" size="40" color="#2563EB" />
        </div>
        <h1>欢迎回来</h1>
        <p>上门维修 · 专业 · 可信赖</p>
      </div>

      <div class="glass-card form-card">
        <van-form @submit="onSubmit">
          <div class="input-group">
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
              class="btn-gradient"
              :loading="loading"
            >
              登 录
            </van-button>
          </div>
        </van-form>

        <div class="form-footer">
          <span class="tip">没有账号？</span>
          <span class="link" @click="$router.push('/register')">立即注册</span>
        </div>
      </div>

      <div class="debug-info">
        测试账号: 13800138000 / 123456
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showToast } from 'vant';

const username = ref('13800138000');
const password = ref('123456');
const loading = ref(false);
const router = useRouter();

const onSubmit = async (values) => {
    loading.value = true;
    try {
        const res = await axios.post('/api/auth/login', {
            username: values.username,
            password: values.password,
            role: 'USER'
        });
        
        if (res.data && res.data.token) {
            showToast('登录成功');
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('userInfo', JSON.stringify(res.data));
            axios.defaults.headers.common['Authorization'] = res.data.token;
            router.push('/');
        } else {
            showToast('登录失败');
        }
    } catch (err) {
        console.error(err);
        showToast('登录失败: ' + (err.response?.data?.message || err.message));
    } finally {
        loading.value = false;
    }
};
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-gradient);
  padding: 20px;
}

.login-content {
  width: 100%;
  max-width: 400px;
}

.brand-section {
  text-align: center;
  margin-bottom: 40px;
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
  box-shadow: 0 10px 25px rgba(37, 99, 235, 0.15);
}

.brand-section h1 {
  font-size: 28px;
  color: #1E293B;
  margin-bottom: 8px;
}

.brand-section p {
  color: #64748B;
  font-size: 14px;
}

.form-card {
  padding: 30px 20px;
}

.input-group {
  margin-bottom: 20px;
  background: #F1F5F9;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
}

.input-group:focus-within {
  background: #fff;
  box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.2);
}

.custom-input {
  background: transparent;
  padding: 12px 16px;
}

.input-icon {
  font-size: 18px;
  color: #94A3B8;
  margin-right: 8px;
}

.submit-btn-wrapper {
  margin-top: 30px;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.tip {
  color: #94A3B8;
}

.link {
  color: var(--primary-color);
  font-weight: 600;
  cursor: pointer;
  margin-left: 5px;
}

.debug-info {
  text-align: center;
  margin-top: 40px;
  font-size: 12px;
  color: #CBD5E1;
}
</style>
