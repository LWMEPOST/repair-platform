<template>
  <div class="home-page page-container">
    <div class="header-section">
      <div class="user-welcome">
        <div class="text">
          <h2>抢单大厅</h2>
          <p>海量订单，等你来抢</p>
        </div>
        <van-icon name="fire-o" size="32" color="rgba(255,255,255,0.8)" />
      </div>
      
      <!-- 统计概览 (模拟数据或后续对接) -->
      <div class="stats-row">
        <div class="stat-item">
          <span class="num">{{ list.length }}</span>
          <span class="label">待抢订单</span>
        </div>
        <div class="stat-item">
          <span class="num">--</span>
          <span class="label">今日收益</span>
        </div>
      </div>
    </div>

    <div class="order-list-wrapper">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="暂无更多待抢订单"
          @load="onLoad"
        >
          <div v-if="list.length === 0 && !loading" class="empty-state">
            <van-empty description="暂时没有新订单，休息一下吧" />
          </div>

          <div v-for="order in list" :key="order.id" class="glass-card order-card">
            <div class="card-header">
              <div class="header-left">
                <van-tag type="primary" size="medium" class="service-tag">维修</van-tag>
                <span class="time">{{ formatTime(order.createTime) }}</span>
              </div>
              <span class="price">¥{{ order.price }}</span>
            </div>
            
            <div class="card-body">
              <div class="info-item">
                <van-icon name="location-o" class="icon" />
                <span class="text">{{ order.address }}</span>
              </div>
              <div class="info-item content-item">
                <van-icon name="description" class="icon" />
                <span class="text">{{ order.serviceContent }}</span>
              </div>
            </div>

            <div class="card-footer">
              <div class="distance-info">
                <van-icon name="guide-o" />
                <span>距离计算中...</span>
              </div>
              <van-button 
                type="primary" 
                round 
                class="btn-gradient grab-btn"
                @click="handleGrab(order)"
              >
                立即抢单
              </van-button>
            </div>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showSuccessToast, showFailToast } from 'vant';

const list = ref([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const router = useRouter();
const userInfo = ref(null);

onMounted(() => {
  const userStr = localStorage.getItem('userInfo');
  if (!userStr) {
    router.push('/login');
    return;
  }
  userInfo.value = JSON.parse(userStr);
});

const onLoad = async () => {
  if (refreshing.value) {
    list.value = [];
    refreshing.value = false;
  }
  
  try {
    const res = await axios.get('/api/orders/waiting');
    const data = res.data || [];
    list.value = data;
    loading.value = false;
    finished.value = true; // 一次性加载完
  } catch (err) {
    loading.value = false;
    finished.value = true;
    showFailToast('加载失败');
  }
};

const onRefresh = () => {
  finished.value = false;
  loading.value = true;
  onLoad();
};

const handleGrab = async (order) => {
  try {
    const res = await axios.post(`/api/orders/${order.id}/grab`, null, {
      params: { workerId: userInfo.value.id }
    });
    if (res.data.includes('成功')) {
      showSuccessToast('抢单成功');
      onRefresh();
      setTimeout(() => {
        router.push('/orders'); // 跳转到我的订单
      }, 1000);
    } else {
      showFailToast(res.data);
    }
  } catch (err) {
    showFailToast('抢单失败');
  }
};

const formatTime = (time) => {
  return time ? time.replace('T', ' ').substring(5, 16) : '刚刚';
};
</script>

<style scoped>
.home-page {
  background-color: #f8fafc;
  padding: 0;
}

.header-section {
  background: var(--primary-gradient);
  padding: 40px 20px 80px;
  color: white;
  border-bottom-left-radius: 32px;
  border-bottom-right-radius: 32px;
  position: relative;
}

.user-welcome {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.user-welcome h2 {
  font-size: 24px;
  margin-bottom: 4px;
}

.user-welcome p {
  opacity: 0.8;
  font-size: 14px;
  margin: 0;
}

.stats-row {
  display: flex;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 16px;
}

.stat-item {
  flex: 1;
  text-align: center;
  border-right: 1px solid rgba(255, 255, 255, 0.2);
}

.stat-item:last-child {
  border-right: none;
}

.stat-item .num {
  display: block;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-item .label {
  font-size: 12px;
  opacity: 0.8;
}

.order-list-wrapper {
  margin-top: -60px;
  padding: 0 16px;
  min-height: 60vh;
}

.order-card {
  padding: 0;
  overflow: hidden;
  transition: transform 0.2s;
}

.order-card:active {
  transform: scale(0.98);
}

.card-header {
  padding: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0,0,0,0.03);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.time {
  color: #9CA3AF;
  font-size: 12px;
}

.price {
  color: var(--accent-color);
  font-weight: 700;
  font-size: 18px;
}

.card-body {
  padding: 16px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
  color: #4B5563;
  font-size: 14px;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-item .icon {
  margin-top: 3px;
  margin-right: 8px;
  color: var(--primary-color);
}

.content-item .text {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  padding: 12px 16px;
  background: rgba(249, 250, 251, 0.5);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.distance-info {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #6B7280;
}

.grab-btn {
  height: 36px;
  padding: 0 24px;
  font-size: 14px;
}

.empty-state {
  padding: 40px 0;
}
</style>