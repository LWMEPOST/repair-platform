<template>
  <div class="register-page">
    <div class="register-content">
      <div class="brand-section">
        <div class="logo-circle">
          <van-icon name="friends" size="40" color="#2563EB" />
        </div>
        <h1>创建账号</h1>
        <p>加入我们，体验专业维修服务</p>
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
              v-model="name"
              name="name"
              placeholder="请输入您的称呼"
              :rules="[{ required: true, message: '请填写姓名' }]"
              class="custom-input"
            >
              <template #left-icon>
                <van-icon name="user-o" class="input-icon" />
              </template>
            </van-field>
          </div>
          
          <div class="input-group">
            <van-field
              v-model="password"
              type="password"
              name="password"
              placeholder="设置密码"
              :rules="[{ required: true, message: '请填写密码' }]"
              class="custom-input"
            >
              <template #left-icon>
                <van-icon name="lock" class="input-icon" />
              </template>
            </van-field>
          </div>

          <div class="input-group">
            <van-field
              v-model="confirmPassword"
              type="password"
              name="confirmPassword"
              placeholder="再次输入密码"
              :rules="[{ required: true, message: '请确认密码' }]"
              class="custom-input"
            >
              <template #left-icon>
                <van-icon name="shield-o" class="input-icon" />
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
              立即注册
            </van-button>
          </div>
        </van-form>

        <div class="form-footer">
          <span class="tip">已有账号？</span>
          <span class="link" @click="$router.push('/login')">直接登录</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showToast } from 'vant';

const username = ref('');
const name = ref('');
const password = ref('');
const confirmPassword = ref('');
const loading = ref(false);
const router = useRouter();

const onSubmit = async (values) => {
    if (values.password !== values.confirmPassword) {
        showToast('两次密码不一致');
        return;
    }
    
    loading.value = true;
    try {
        const res = await axios.post('/api/auth/register', {
            username: values.username,
            name: values.name,
            password: values.password,
            role: 'USER'
        });
        
        if (res.data === '注册成功') {
            showToast('注册成功，请登录');
            router.push('/login');
        } else {
            showToast(res.data);
        }
    } catch (err) {
        console.error(err);
        showToast('注册失败: ' + (err.response?.data?.message || err.message));
    } finally {
        loading.value = false;
    }
};
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-gradient);
  padding: 20px;
}

.register-content {
  width: 100%;
  max-width: 400px;
}

.brand-section {
  text-align: center;
  margin-bottom: 30px;
}

.logo-circle {
  width: 70px;
  height: 70px;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  box-shadow: 0 10px 25px rgba(37, 99, 235, 0.15);
}

.brand-section h1 {
  font-size: 24px;
  color: #1E293B;
  margin-bottom: 6px;
}

.brand-section p {
  color: #64748B;
  font-size: 14px;
}

.form-card {
  padding: 30px 20px;
}

.input-group {
  margin-bottom: 16px;
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
</style>
