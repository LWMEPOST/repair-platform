<template>
  <div class="create-order-page">
    <div class="header-section">
      <van-nav-bar
        title="发布需求"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
      <div class="header-title">
        <h2>填写维修需求</h2>
        <p>详细描述您的问题，以便师傅更好地为您服务</p>
      </div>
    </div>

    <div class="form-wrapper">
      <div class="glass-card form-card">
        <van-form @submit="onSubmit">
          <div class="input-group">
            <div class="label">服务内容</div>
            <van-field
              v-model="form.content"
              name="content"
              rows="4"
              autosize
              type="textarea"
              placeholder="请详细描述您遇到的问题，例如：水管漏水、电路跳闸等..."
              :rules="[{ required: true, message: '请填写服务内容' }]"
              class="custom-textarea"
            />
          </div>

          <div class="input-group">
            <div class="label">服务地址</div>
            <van-field
              v-model="form.address"
              name="address"
              placeholder="请输入详细上门地址"
              :rules="[{ required: true, message: '请填写地址' }]"
              class="custom-input"
            >
              <template #left-icon>
                <van-icon name="location-o" class="input-icon" />
              </template>
            </van-field>
          </div>

          <div class="input-group">
            <div class="label">联系电话</div>
            <van-field
              v-model="form.phone"
              name="phone"
              type="tel"
              placeholder="请输入联系电话"
              :rules="[{ required: true, message: '请填写电话' }]"
              class="custom-input"
            >
              <template #left-icon>
                <van-icon name="phone-o" class="input-icon" />
              </template>
            </van-field>
          </div>

          <div class="input-group">
            <div class="label">预估费用 (元)</div>
            <van-field
              v-model="form.price"
              name="price"
              type="number"
              placeholder="请输入愿意支付的费用"
              :rules="[{ required: true, message: '请填写金额' }]"
              class="custom-input price-input"
            >
              <template #left-icon>
                <van-icon name="gold-coin-o" class="input-icon" />
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
              立即发布
            </van-button>
          </div>
        </van-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { showToast, showSuccessToast, showFailToast } from 'vant';
import axios from 'axios';

const router = useRouter();
const loading = ref(false);
const form = ref({
  content: '',
  address: '',
  phone: '',
  price: ''
});

const userInfo = ref(null);

onMounted(() => {
  const userStr = localStorage.getItem('userInfo');
  if (!userStr) {
    showToast('未登录，请先登录');
    router.push('/login');
    return;
  }
  try {
    userInfo.value = JSON.parse(userStr);
  } catch (e) {
    console.error('解析用户信息失败', e);
    router.push('/login');
  }
});

const onSubmit = async () => {
  if (!userInfo.value) return;

  const payload = {
    userId: userInfo.value.id,
    serviceContent: form.value.content,
    address: form.value.address,
    contactPhone: form.value.phone,
    price: Number(form.value.price)
  };

  loading.value = true;

  try {
    const res = await axios.post('/api/orders', payload);
    // 后端返回字符串 "下单成功" 或 "下单失败"
    if (res.data === '下单成功') {
        showSuccessToast('发布成功');
        setTimeout(() => {
            router.push('/orders');
        }, 1000);
    } else {
        showFailToast(res.data);
    }
  } catch (error) {
    console.error(error);
    showFailToast('发布失败: ' + (error.response?.data?.message || '未知错误'));
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.create-order-page {
  min-height: 100vh;
  background-color: #f8fafc;
}

.header-section {
  background: var(--primary-gradient);
  padding-bottom: 80px;
  border-bottom-left-radius: 32px;
  border-bottom-right-radius: 32px;
}

:deep(.custom-nav) {
  background: transparent;
  --van-nav-bar-title-text-color: #fff;
  --van-nav-bar-icon-color: #fff;
}

.header-title {
  padding: 20px 24px;
  color: white;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

.header-title h2 {
  font-size: 24px;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.header-title p {
  margin: 0;
  opacity: 1;
  font-size: 14px;
}

.form-wrapper {
  padding: 0 16px;
  margin-top: -60px;
}

.form-card {
  padding: 24px 20px;
}

.input-group {
  margin-bottom: 20px;
}

.input-group .label {
  font-size: 14px;
  color: #374151;
  font-weight: 500;
  margin-bottom: 8px;
  margin-left: 4px;
}

.custom-input, .custom-textarea {
  background: #F8FAFC;
  border-radius: 12px;
  padding: 12px 16px;
  border: 1px solid transparent;
  transition: all 0.3s;
}

.custom-input:focus-within, .custom-textarea:focus-within {
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
