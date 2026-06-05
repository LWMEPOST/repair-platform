<template>
  <div class="profile-page">
    <div class="profile-header">
      <div class="header-content">
        <h2>个人中心</h2>
        <p>管理您的账户与订单</p>
      </div>
    </div>

    <div class="content-wrapper">
      <div class="glass-card user-card">
        <div class="avatar-box">
          <van-image
            round
            width="80px"
            height="80px"
            :src="userInfo?.avatar || 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'"
            class="user-avatar"
          />
        </div>
        <div class="user-info">
          <div class="username">{{ userInfo?.name || '未登录' }}</div>
          <div class="phone">{{ userInfo?.username || '请先登录' }}</div>
        </div>
      </div>

      <div class="glass-card menu-card">
        <van-cell-group :border="false">
          <van-cell title="修改信息" icon="edit" is-link to="/edit-profile" class="custom-cell" />
          <van-cell title="我的订单" icon="orders-o" is-link to="/orders" class="custom-cell" />
          <van-cell title="联系客服" icon="service-o" is-link to="/service" class="custom-cell" />
          <van-cell title="关于我们" icon="info-o" is-link to="/about" class="custom-cell" />
        </van-cell-group>
      </div>

      <div class="action-wrapper">
        <van-button 
          block 
          round 
          class="logout-btn" 
          @click="logout"
        >
          退出登录
        </van-button>
      </div>
    </div>

    <van-tabbar route fixed placeholder class="glass-tabbar">
      <van-tabbar-item replace to="/" icon="wap-home-o">首页</van-tabbar-item>
      <van-tabbar-item replace to="/profile" icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const userInfo = ref(null);

onMounted(async () => {
  const userStr = localStorage.getItem('userInfo');
  if (userStr) {
    const localUser = JSON.parse(userStr);
    try {
        const res = await axios.get(`/api/users/${localUser.id}`);
        userInfo.value = res.data;
        // 同步更新本地缓存
        localStorage.setItem('userInfo', JSON.stringify(res.data));
    } catch (e) {
        userInfo.value = localUser;
    }
  }
});


const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('userInfo');
  router.push('/login');
};
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background-color: #f8fafc;
  padding-bottom: 50px;
}

.profile-header {
  background: var(--primary-gradient);
  padding: 40px 24px 100px;
  color: white;
  border-bottom-left-radius: 32px;
  border-bottom-right-radius: 32px;
}

.header-content h2 {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
  letter-spacing: -0.5px;
}

.header-content p {
  opacity: 0.9;
  font-size: 15px;
  margin: 0;
  font-weight: 400;
}

.content-wrapper {
  padding: 0 16px;
  margin-top: -80px;
  position: relative;
  z-index: 10;
}

.user-card {
  display: flex;
  align-items: center;
  padding: 24px;
}

.avatar-box {
  margin-right: 20px;
  border: 3px solid rgba(255, 255, 255, 0.8);
  border-radius: 50%;
  padding: 2px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.user-info .username {
  font-size: 22px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 4px;
}

.user-info .phone {
  color: #6b7280;
  font-size: 15px;
}

.menu-card {
  padding: 12px 0;
  overflow: hidden;
}

.custom-cell {
  background: transparent;
  padding: 18px 24px;
  align-items: center;
}

.custom-cell:after {
  left: 24px;
  right: 24px;
  border-bottom-color: rgba(0, 0, 0, 0.05);
}

:deep(.van-cell__left-icon) {
  font-size: 22px;
  color: var(--primary-color);
  margin-right: 16px;
}

:deep(.van-cell__title) {
  font-size: 16px;
  color: #374151;
  font-weight: 500;
}

.action-wrapper {
  margin-top: 32px;
  margin-bottom: 24px;
}

.logout-btn {
  background: #fee2e2;
  color: #ef4444;
  border: none;
  font-weight: 600;
  height: 52px;
  font-size: 16px;
}

.glass-tabbar {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-top: 1px solid rgba(0, 0, 0, 0.05);
}

:deep(.van-tabbar-item--active) {
  color: var(--primary-color);
  background: transparent;
}
</style>
