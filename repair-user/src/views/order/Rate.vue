<template>
  <div class="rate-page">
    <div class="header-section">
      <van-nav-bar
        title="评价订单"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
      <div class="header-title">
        <h2>服务评价</h2>
        <p>您的评价将帮助我们提供更好的服务</p>
      </div>
    </div>
    
    <div class="rate-container">
      <div class="glass-card rate-card">
        <div class="score-section">
          <div class="label">服务评分</div>
          <van-rate 
            v-model="score" 
            :size="32" 
            color="#F59E0B" 
            void-icon="star" 
            void-color="#E5E7EB"
            class="custom-rate"
          />
          <div class="score-text">{{ scoreText }}</div>
        </div>

        <div class="input-section">
          <van-field
            v-model="content"
            rows="4"
            autosize
            type="textarea"
            placeholder="师傅的服务如何？是否专业？快来评价一下吧~"
            show-word-limit
            maxlength="200"
            class="custom-textarea"
          />
        </div>

        <div class="submit-btn-wrapper">
          <van-button 
            round 
            block 
            type="primary" 
            class="btn-gradient submit-btn"
            @click="onSubmit"
            :loading="loading"
          >
            提交评价
          </van-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { showSuccessToast, showFailToast } from 'vant';

const route = useRoute();
const router = useRouter();
const score = ref(5);
const content = ref('');
const loading = ref(false);

const scoreText = computed(() => {
  const texts = ['非常差', '差', '一般', '好', '非常满意'];
  return texts[score.value - 1] || '';
});

const onSubmit = async () => {
  loading.value = true;
  try {
    const orderId = route.params.id;
    await axios.post(`/api/orders/${orderId}/rate`, null, {
      params: {
        score: score.value,
        content: content.value || '用户默认好评'
      }
    });
    
    showSuccessToast('评价成功');
    setTimeout(() => {
      router.back();
    }, 1000);
  } catch (error) {
    showFailToast('评价失败');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.rate-page {
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
}

.header-title h2 {
  font-size: 24px;
  margin: 0 0 8px 0;
  font-weight: 600;
}

.header-title p {
  margin: 0;
  opacity: 0.9;
  font-size: 14px;
}

.rate-container {
  padding: 0 16px;
  margin-top: -60px;
}

.rate-card {
  padding: 32px 24px;
  text-align: center;
}

.score-section {
  margin-bottom: 32px;
}

.label {
  font-size: 16px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
}

.custom-rate {
  margin-bottom: 12px;
}

.score-text {
  font-size: 14px;
  color: #F59E0B;
  font-weight: 500;
  min-height: 20px;
}

.input-section {
  margin-bottom: 32px;
}

.custom-textarea {
  background: #F8FAFC;
  border-radius: 12px;
  padding: 16px;
  border: 1px solid transparent;
  transition: all 0.3s;
}

.custom-textarea:focus-within {
  background: #fff;
  border-color: var(--primary-light);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.submit-btn-wrapper {
  margin-top: 24px;
}

.submit-btn {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.2);
}
</style>
