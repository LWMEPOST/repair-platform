<template>
  <div class="order-detail-page">
    <div class="header-section">
      <van-nav-bar
        title="订单详情"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
      <div v-if="order" class="status-header">
        <h2>{{ getStatusText(order.status) }}</h2>
        <p v-if="order.status === 'WAITING_USER_CONFIRM'">请确认是否由该师傅为您服务</p>
        <p v-else>{{ getStatusDesc(order.status) }}</p>
      </div>
    </div>
    
    <div v-if="order" class="content-wrapper">
      <!-- 师傅信息卡片 -->
      <div v-if="order.workerId" class="glass-card worker-card" @click="viewWorker(order.workerId)">
        <div class="card-title">
          <span class="label">接单师傅</span>
          <van-icon name="arrow" class="arrow-icon" />
        </div>
        <div class="worker-content">
          <van-image
            round
            width="60"
            height="60"
            src="https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg"
            class="worker-avatar"
          />
          <div class="info-text">
            <div class="name">点击查看师傅详情</div>
            <div class="sub">
              <van-tag type="primary" plain size="medium">实名认证</van-tag>
              <span class="rating">
                  <van-icon name="star" color="#FF9500" /> 4.8
              </span>
            </div>
          </div>
        </div>
        <!-- 确认按钮 -->
        <div v-if="order.status === 'WAITING_USER_CONFIRM'" class="action-btns">
          <van-button plain type="danger" size="small" round @click.stop="handleConfirm(false)">拒绝</van-button>
          <van-button type="primary" size="small" round class="btn-gradient" @click.stop="handleConfirm(true)">确认师傅</van-button>
        </div>
      </div>

      <!-- 订单信息 -->
      <div class="glass-card info-card">
        <div class="card-title">订单信息</div>
        <div class="info-list">
          <div class="info-item">
            <span class="label">订单编号</span>
            <span class="value">{{ order.orderNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">服务内容</span>
            <span class="value">{{ order.serviceContent }}</span>
          </div>
          <div class="info-item">
            <span class="label">联系人</span>
            <span class="value">{{ order.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">联系电话</span>
            <span class="value">{{ order.contactPhone }}</span>
          </div>
          <div class="info-item">
            <span class="label">上门地址</span>
            <span class="value">{{ order.address }}</span>
          </div>
          <div class="info-item">
            <span class="label">下单时间</span>
            <span class="value">{{ formatTime(order.createTime) }}</span>
          </div>
           <div class="info-item">
            <span class="label">预估费用</span>
            <span class="price">¥{{ order.price }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { showToast, showSuccessToast, showConfirmDialog } from 'vant';

const route = useRoute();
const router = useRouter();
const order = ref(null);

onMounted(() => {
  loadOrder();
});

const loadOrder = async () => {
  try {
      // 临时用列表接口筛选
      const userInfo = JSON.parse(localStorage.getItem('userInfo'));
      const res = await axios.get(`/api/orders/user/${userInfo.id}`);
      order.value = res.data.find(o => o.id == route.params.id);
  } catch (e) {
      console.error(e);
  }
};

const getStatusText = (status) => {
  const map = {
    'CREATED': '待支付',
    'WAITING_GRAB': '待接单',
    'WAITING_USER_CONFIRM': '待确认',
    'WAITING_START': '师傅赶往中',
    'SERVICE_ING': '服务中',
    'FINISHED': '已完成',
    'RATED': '已评价'
  };
  return map[status] || status;
};

const getStatusDesc = (status) => {
    const map = {
        'CREATED': '请尽快完成支付',
        'WAITING_GRAB': '正在为您寻找附近的专业师傅',
        'WAITING_START': '师傅正在赶往您的位置，请保持电话畅通',
        'SERVICE_ING': '师傅正在为您进行维修服务',
        'FINISHED': '服务已完成，请对服务进行评价',
        'RATED': '感谢您的评价'
    };
    return map[status] || '';
};

const viewWorker = (workerId) => {
  router.push(`/worker/profile/${workerId}`);
};

const handleConfirm = (accept) => {
  const title = accept ? '确认师傅' : '拒绝师傅';
  const msg = accept ? '确认由该师傅为您服务吗？' : '拒绝后订单将重新进入抢单池。';
  
  showConfirmDialog({
    title: title,
    message: msg,
    theme: 'round-button',
    confirmButtonColor: '#2563EB'
  }).then(async () => {
    try {
      await axios.post(`/api/orders/${order.value.id}/confirm`, null, {
        params: { accept }
      });
      showSuccessToast('操作成功');
      loadOrder();
    } catch (e) {
      showToast('操作失败');
    }
  });
};

const formatTime = (time) => {
  return time ? time.replace('T', ' ').substring(0, 16) : '';
};
</script>

<style scoped>
.order-detail-page {
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

.status-header {
  padding: 10px 24px 0;
  color: white;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

.status-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.status-header p {
  margin: 8px 0 0;
  opacity: 1;
  font-size: 14px;
}

.content-wrapper {
  padding: 0 16px;
  margin-top: -60px;
}

.glass-card {
  margin-bottom: 16px;
  padding: 20px;
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  color: #1F2937;
  margin-bottom: 16px;
}

.arrow-icon {
  color: #9CA3AF;
  font-size: 16px;
}

.worker-content {
  display: flex;
  align-items: center;
}

.worker-avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.info-text {
  margin-left: 16px;
}

.name {
  font-weight: 600;
  font-size: 16px;
  color: #111827;
  margin-bottom: 6px;
}

.sub {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #6B7280;
}

.rating {
  display: flex;
  align-items: center;
  gap: 2px;
  color: #4B5563;
  font-weight: 500;
}

.action-btns {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid rgba(0,0,0,0.05);
  padding-top: 16px;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  font-size: 14px;
}

.info-item .label {
  color: #6B7280;
  min-width: 70px;
}

.info-item .value {
  color: #1F2937;
  text-align: right;
  flex: 1;
}

.info-item .price {
  color: var(--accent-color);
  font-weight: 700;
  font-size: 16px;
}
</style>
