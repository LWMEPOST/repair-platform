<template>
  <div class="order-list-page page-container">
    <div class="header-fixed">
      <van-nav-bar title="我的任务" :border="false" class="custom-nav" />
      <van-tabs v-model:active="activeTab" background="transparent" color="#2563EB" title-active-color="#2563EB">
        <van-tab title="全部" name="all"></van-tab>
        <van-tab title="待上门" name="WAITING_START"></van-tab>
        <van-tab title="待确认" name="WAITING_USER_CONFIRM"></van-tab>
        <van-tab title="服务中" name="SERVICE_ING"></van-tab>
        <van-tab title="已完成" name="FINISHED"></van-tab>
      </van-tabs>
    </div>

    <div class="list-content">
      <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
        >
          <div v-for="order in list" :key="order.id" class="glass-card order-card">
            <div class="card-header">
              <span class="order-time">{{ formatTime(order.createTime) }}</span>
              <van-tag :type="getStatusType(order.status)" size="medium" class="status-tag">
                {{ getStatusText(order.status) }}
              </van-tag>
            </div>
            
            <div class="card-body">
              <div class="info-group">
                <div class="label">客户信息</div>
                <div class="value-row">
                  <span class="name">{{ order.name || '客户' }}</span>
                  <a :href="'tel:' + order.contactPhone" class="phone-link" @click.stop>
                    <van-icon name="phone" /> {{ order.contactPhone }}
                  </a>
                </div>
              </div>
              
              <div class="info-group">
                <div class="label">服务地址</div>
                <div class="value">{{ order.address }}</div>
              </div>

              <div class="info-group">
                <div class="label">服务内容</div>
                <div class="value content">{{ order.serviceContent }}</div>
              </div>
            </div>

            <div class="card-footer" v-if="['WAITING_START', 'SERVICE_ING'].includes(order.status)">
              <van-button 
                v-if="order.status === 'WAITING_START'" 
                type="primary" 
                round 
                block
                class="btn-gradient"
                @click="handleStart(order)"
              >
                开始服务
              </van-button>
              <van-button 
                v-if="order.status === 'SERVICE_ING'" 
                type="success" 
                round 
                block
                class="btn-gradient success-btn"
                @click="handleFinish(order)"
              >
                完成服务
              </van-button>
            </div>
          </div>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showToast, showSuccessToast, showFailToast, showConfirmDialog } from 'vant';

const list = ref([]);
const fullList = ref([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const activeTab = ref('all');
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
    const res = await axios.get(`/api/orders/worker/${userInfo.value.id}`);
    const data = Array.isArray(res.data) ? res.data : (res.data.records || []);
    fullList.value = data;
    filterList();
    loading.value = false;
    finished.value = true;
  } catch (err) {
    loading.value = false;
    finished.value = true;
    showFailToast('加载失败');
  }
};

const filterList = () => {
  if (activeTab.value === 'all') {
    list.value = fullList.value;
  } else {
    list.value = fullList.value.filter(item => item.status === activeTab.value);
  }
};

watch(activeTab, filterList);

const onRefresh = () => {
  finished.value = false;
  loading.value = true;
  onLoad();
};

const getStatusText = (status) => {
  const map = {
    'WAITING_GRAB': '待抢单',
    'WAITING_USER_CONFIRM': '待用户确认',
    'WAITING_START': '待上门',
    'SERVICE_ING': '服务中',
    'FINISHED': '已完成',
    'RATED': '已评价'
  };
  return map[status] || status;
};

const getStatusType = (status) => {
  if (status === 'FINISHED' || status === 'RATED') return 'success';
  if (status === 'SERVICE_ING') return 'primary';
  if (status === 'WAITING_USER_CONFIRM') return 'warning';
  return 'primary';
};

const handleStart = (order) => {
  showConfirmDialog({
    title: '确认开始',
    message: '确认已到达现场并开始服务吗？',
    theme: 'round-button',
    confirmButtonColor: '#2563EB'
  }).then(async () => {
    try {
      await axios.post(`/api/orders/${order.id}/start`);
      showSuccessToast('已开始服务');
      onRefresh();
    } catch (err) {
      showFailToast('操作失败');
    }
  });
};

const handleFinish = (order) => {
  showConfirmDialog({
    title: '确认完工',
    message: '确认服务已完成吗？',
    theme: 'round-button',
    confirmButtonColor: '#10B981'
  }).then(async () => {
    try {
      await axios.post(`/api/orders/${order.id}/finish`);
      showSuccessToast('已确认完工');
      onRefresh();
    } catch (err) {
      showFailToast('操作失败');
    }
  });
};

const formatTime = (time) => {
  return time ? time.replace('T', ' ').substring(5, 16) : '';
};
</script>

<style scoped>
.order-list-page {
  background-color: #f8fafc;
  padding: 0;
}

.header-fixed {
  position: sticky;
  top: 0;
  z-index: 10;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  padding-bottom: 8px;
}

:deep(.custom-nav) {
  background: transparent;
}

.list-content {
  padding: 16px;
  min-height: calc(100vh - 100px);
}

.order-card {
  padding: 0;
  overflow: hidden;
}

.card-header {
  padding: 12px 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0,0,0,0.03);
  background: rgba(249, 250, 251, 0.5);
}

.order-time {
  font-size: 13px;
  color: #6B7280;
}

.card-body {
  padding: 16px;
}

.info-group {
  margin-bottom: 12px;
}

.info-group:last-child {
  margin-bottom: 0;
}

.info-group .label {
  font-size: 12px;
  color: #9CA3AF;
  margin-bottom: 4px;
}

.value-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.value-row .name {
  font-weight: 500;
  color: #1F2937;
}

.phone-link {
  color: var(--primary-color);
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
  text-decoration: none;
  background: rgba(37, 99, 235, 0.1);
  padding: 4px 10px;
  border-radius: 20px;
}

.value {
  color: #374151;
  font-size: 14px;
  line-height: 1.5;
}

.value.content {
  color: #4B5563;
}

.card-footer {
  padding: 12px 16px;
  border-top: 1px solid rgba(0,0,0,0.03);
}

.success-btn {
  background: linear-gradient(135deg, #10B981 0%, #34D399 100%) !important;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}
</style>