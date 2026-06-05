<template>
  <div class="register-page">
    <div class="header-section">
      <van-nav-bar
        title="注册师傅"
        left-arrow
        @click-left="onClickLeft"
        :border="false"
        class="custom-nav"
      />
    </div>
    
    <div class="brand-section">
      <h1>加入我们</h1>
      <p>开启您的专业接单之旅</p>
    </div>

    <div class="register-card glass-card">
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
          <div class="label">姓名</div>
          <van-field
            v-model="name"
            name="name"
            placeholder="请输入真实姓名"
            :rules="[{ required: true, message: '请填写姓名' }]"
            class="custom-input"
          >
            <template #left-icon>
              <van-icon name="user-o" class="input-icon" />
            </template>
          </van-field>
        </div>

        <div class="input-group">
          <div class="label">密码</div>
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
          <div class="label">确认密码</div>
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
            class="btn-gradient submit-btn"
            :loading="loading"
          >
            立即注册
          </van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showToast, showSuccessToast, showFailToast } from 'vant';

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
      role: 'WORKER'
    });
    
    if (res.data === '注册成功') {
      showSuccessToast('注册成功');
      setTimeout(() => {
        router.push('/login');
      }, 1000);
    } else {
      showFailToast(res.data);
    }
  } catch (err) {
    console.error(err);
    showFailToast('注册失败: ' + (err.response?.data?.message || err.message));
  } finally {
    loading.value = false;
  }
};

const onClickLeft = () => {
  history.back();
};
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background-color: #f8fafc;
  padding: 0 20px;
  background-image: linear-gradient(135deg, #2563EB 0%, #60A5FA 100%);
}

.header-section {
  padding-top: 10px;
}

:deep(.custom-nav) {
  background: transparent;
  --van-nav-bar-title-text-color: #fff;
  --van-nav-bar-icon-color: #fff;
}

.brand-section {
  margin: 30px 0 40px;
  color: white;
}

.brand-section h1 {
  font-size: 28px;
  margin-bottom: 8px;
}

.brand-section p {
  opacity: 0.9;
  font-size: 16px;
  margin: 0;
}

.register-card {
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
</style>