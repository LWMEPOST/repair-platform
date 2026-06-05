<template>
  <div class="order-list-page">
    <div class="nav-header">
      <van-nav-bar
        title="我的订单"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
    </div>

    <van-tabs 
      v-model:active="activeTab" 
      sticky 
      offset-top="46"
      class="custom-tabs"
      color="#2563EB"
      title-active-color="#2563EB"
    >
      <van-tab title="全部" name="all"></van-tab>
      <van-tab title="待支付" name="CREATED"></van-tab>
      <van-tab title="待抢单" name="WAITING_GRAB"></van-tab>
      <van-tab title="待确认" name="WAITING_USER_CONFIRM"></van-tab>
      <van-tab title="待上门" name="WAITING_START"></van-tab>
      <van-tab title="服务中" name="SERVICE_ING"></van-tab>
      <van-tab title="已完成" name="FINISHED"></van-tab>
    </van-tabs>

    <van-pull-refresh v-model="refreshing" @refresh="onRefresh" class="list-container">
      <van-list
        v-model:loading="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <div v-for="order in list" :key="order.id" class="glass-card order-card" @click="goDetail(order.id)">
          <div class="card-header">
            <span class="order-no">
              <van-icon name="clock-o" class="icon" />
              {{ formatTime(order.createTime) }}
            </span>
            <van-tag :type="getStatusType(order.status)" class="status-tag">
              {{ getStatusText(order.status) }}
            </van-tag>
          </div>

          <div class="card-body">
            <div class="service-title">{{ order.serviceContent }}</div>
            
            <div class="info-grid">
              <div class="info-item">
                <van-icon name="location-o" />
                <span>{{ order.address }}</span>
              </div>
            </div>
            
            <div class="price-row">
              <span class="label">预估费用</span>
              <span class="price">¥{{ order.price }}</span>
            </div>
          </div>

          <div class="card-footer" v-if="order.status === 'CREATED' || order.status === 'FINISHED'">
            <van-button 
              v-if="order.status === 'CREATED'" 
              size="small" 
              round 
              type="primary"
              class="action-btn pay-btn"
              @click.stop="handlePay(order)"
            >
              立即支付
            </van-button>
            <van-button 
              v-if="order.status === 'FINISHED'" 
              size="small" 
              round
              plain 
              type="primary"
              class="action-btn rate-btn"
              @click.stop="handleRate(order)"
            >
              去评价
            </van-button>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>

    <!-- 模拟支付弹窗 -->
    <van-dialog v-model:show="showPayDialog" :show-confirm-button="false" title="正在支付" class="glass-dialog">
      <div class="payment-content">
        <van-loading color="#2563EB" size="36px" vertical>支付处理中...</van-loading>
      </div>
    </van-dialog>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { showToast, showSuccessToast, showFailToast } from 'vant';
import axios from 'axios';

const router = useRouter();
const list = ref([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const activeTab = ref('all');
const showPayDialog = ref(false);
const fullList = ref([]);

const userInfo = ref(null);

onMounted(() => {
  const userStr = localStorage.getItem('userInfo');
  if (!userStr) {
    router.push('/login');
    return;
  }
  try {
    userInfo.value = JSON.parse(userStr);
  } catch (e) {
    router.push('/login');
  }
});

const onLoad = async () => {
  if (!userInfo.value) {
    loading.value = false;
    finished.value = true;
    return;
  }
  
  if (refreshing.value) {
    list.value = [];
    refreshing.value = false;
  }

  try {
    const res = await axios.get(`/api/orders/user/${userInfo.value.id}`);
    // 兼容可能的分页结构或直接List结构
    const data = Array.isArray(res.data) ? res.data : (res.data.records || []);
    
    fullList.value = data;
    filterList();
    
    loading.value = false;
    finished.value = true;
  } catch (error) {
    loading.value = false;
    finished.value = true;
    console.error(error);
    showFailToast('获取数据失败');
  }
};

const onRefresh = () => {
  finished.value = false;
  loading.value = true;
  onLoad();
};

const filterList = () => {
  if (activeTab.value === 'all') {
    list.value = fullList.value;
  } else {
    list.value = fullList.value.filter(item => item.status === activeTab.value);
  }
};

watch(activeTab, () => {
  filterList();
});

const goDetail = (id) => {
  router.push(`/order/${id}`);
};

const getStatusText = (status) => {
  const map = {
    'CREATED': '待支付',
    'WAITING_GRAB': '待抢单',
    'WAITING_USER_CONFIRM': '待确认师傅',
    'WAITING_START': '师傅赶往中',
    'SERVICE_ING': '服务中',
    'FINISHED': '已完成',
    'RATED': '已评价'
  };
  return map[status] || status;
};

const getStatusType = (status) => {
  if (status === 'FINISHED' || status === 'RATED') return 'success';
  if (status === 'SERVICE_ING') return 'primary';
  if (status === 'WAITING_USER_CONFIRM') return 'danger';
  return 'warning';
};

const formatTime = (time) => {
  return time ? time.replace('T', ' ').substring(0, 16) : '暂无时间';
};

const handlePay = (order) => {
  showPayDialog.value = true;
  
  setTimeout(async () => {
    try {
      await axios.post(`/api/orders/${order.id}/pay`);
      showPayDialog.value = false;
      showSuccessToast('支付成功');
      onRefresh(); 
    } catch (error) {
      showPayDialog.value = false;
      showFailToast('支付失败');
    }
  }, 1500);
};

const handleRate = (order) => {
    router.push(`/order/${order.id}/rate`);
};
</script>

<style scoped>
.order-list-page {
  min-height: 100vh;
  background-color: #f8fafc;
}

.nav-header {
  background: white;
  position: sticky;
  top: 0;
  z-index: 99;
}

:deep(.custom-nav) {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
}

:deep(.van-nav-bar__title) {
  font-weight: 600;
  color: #1f2937;
}

:deep(.van-nav-bar__arrow) {
  color: #1f2937;
  font-size: 18px;
}

.custom-tabs {
  background: white;
  margin-bottom: 12px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
}

:deep(.van-tabs__nav) {
  background: transparent;
}

.list-container {
  padding: 12px 16px;
}

.order-card {
  margin-bottom: 16px;
  padding: 0;
  overflow: hidden;
  transition: transform 0.2s;
}

.order-card:active {
  transform: scale(0.99);
}

.card-header {
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.order-no {
  font-size: 14px;
  color: #6b7280;
  display: flex;
  align-items: center;
}

.order-no .icon {
  margin-right: 6px;
  font-size: 16px;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 6px;
  font-weight: 500;
}

.card-body {
  padding: 20px;
}

.service-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 12px;
}

.info-grid {
  display: grid;
  gap: 8px;
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #6b7280;
}

.info-item .van-icon {
  margin-right: 6px;
  color: #9ca3af;
}

.price-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed rgba(0, 0, 0, 0.05);
}

.price-row .label {
  font-size: 14px;
  color: #6b7280;
}

.price-row .price {
  font-size: 20px;
  font-weight: 700;
  color: var(--accent-color);
}

.card-footer {
  padding: 12px 20px;
  background: rgba(249, 250, 251, 0.5);
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

.action-btn {
  min-width: 80px;
}

.pay-btn {
  background: var(--primary-gradient);
  border: none;
}

.rate-btn {
  color: var(--primary-color);
  border-color: var(--primary-color);
}

.payment-content {
  padding: 40px 20px;
  text-align: center;
}
</style>