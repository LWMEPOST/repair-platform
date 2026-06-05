<template>
  <div class="profile-page page-container">
    <div class="profile-header">
      <div class="user-card glass-card">
        <div class="avatar-wrapper">
          <van-image
            round
            width="80"
            height="80"
            :src="userInfo?.avatar || 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'"
            class="avatar"
          />
        </div>
        <div class="info-content">
          <div class="name-row">
            <span class="name">{{ userInfo?.name || '未登录' }}</span>
            <van-tag type="primary" plain size="medium" class="role-tag">专业师傅</van-tag>
          </div>
          <div class="phone">{{ userInfo?.username || '' }}</div>
        </div>
      </div>
      
      <!-- 快捷统计 -->
      <div class="stats-card glass-card">
        <div class="stat-item">
          <span class="value">{{ earnings }}</span>
          <span class="label">总收益</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="value">{{ authStatusText }}</span>
          <span class="label">认证状态</span>
        </div>
      </div>
    </div>

    <div class="menu-section">
      <div class="glass-card menu-card">
        <van-cell-group :border="false">
          <van-cell title="实名认证" is-link to="/worker/auth" class="menu-item">
            <template #icon>
              <div class="icon-box blue">
                <van-icon name="idcard" />
              </div>
            </template>
            <template #value>
              <van-tag :type="getAuthStatusColor(authStatus)">{{ authStatusText }}</van-tag>
            </template>
          </van-cell>
          
          <van-cell title="技能管理" is-link to="/worker/skills" class="menu-item">
            <template #icon>
              <div class="icon-box orange">
                <van-icon name="setting-o" />
              </div>
            </template>
          </van-cell>
          
          <van-cell title="收益明细" is-link to="/worker/earnings" class="menu-item">
            <template #icon>
              <div class="icon-box green">
                <van-icon name="gold-coin-o" />
              </div>
            </template>
          </van-cell>
        </van-cell-group>
      </div>

      <div class="logout-wrapper">
        <van-button block round class="logout-btn" @click="logout">退出登录</van-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showConfirmDialog } from 'vant';

const router = useRouter();
const userInfo = ref(null);
const authStatus = ref('UNAUTH');
const earnings = ref('¥0.00');

const authStatusText = computed(() => {
  const map = {
    'UNAUTH': '未认证',
    'AUDITING': '审核中',
    'AUTHED': '已认证',
    'REJECTED': '未通过'
  };
  return map[authStatus.value] || '未认证';
});

onMounted(async () => {
  const userStr = localStorage.getItem('userInfo');
  if (userStr) {
    const localUser = JSON.parse(userStr);
    userInfo.value = localUser;
    
    try {
      const userRes = await axios.get(`/api/users/${localUser.id}`);
      userInfo.value = { ...localUser, ...userRes.data };
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value));
    } catch (e) {
      console.error(e);
    }

    try {
      const infoRes = await axios.get(`/api/worker/info/${localUser.id}`);
      if (infoRes.data) {
        authStatus.value = infoRes.data.authStatus;
      }
    } catch (e) {
      console.error(e);
    }

    try {
      const earnRes = await axios.get(`/api/worker/earnings/${localUser.id}`);
      if (earnRes.data) {
        earnings.value = `¥${Number(earnRes.data.total || 0).toFixed(2)}`;
      }
    } catch (e) {
      console.error(e);
    }
  }
});

const getAuthStatusColor = (status) => {
  const map = {
    'UNAUTH': 'default',
    'AUDITING': 'warning',
    'AUTHED': 'success',
    'REJECTED': 'danger'
  };
  return map[status] || 'default';
};

const logout = () => {
  showConfirmDialog({
    title: '退出登录',
    message: '确定要退出当前账号吗？',
    theme: 'round-button',
  }).then(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    router.push('/login');
  });
};
</script>

<style scoped>
.profile-page {
  padding: 0;
  background-color: #f8fafc;
}

.profile-header {
  background: var(--primary-gradient);
  padding: 60px 20px 80px;
  border-bottom-left-radius: 32px;
  border-bottom-right-radius: 32px;
  position: relative;
  margin-bottom: 60px;
}

.user-card {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  margin-bottom: 0;
}

.avatar {
  border: 3px solid rgba(255, 255, 255, 0.3);
}

.info-content {
  margin-left: 16px;
  color: white;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.name {
  font-size: 20px;
  font-weight: 600;
}

.phone {
  font-size: 14px;
  opacity: 0.8;
}

.stats-card {
  position: absolute;
  bottom: -40px;
  left: 20px;
  right: 20px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 20px;
  margin-bottom: 0;
  z-index: 2;
}

.stat-item {
  text-align: center;
}

.stat-item .value {
  display: block;
  font-size: 18px;
  font-weight: 700;
  color: #1F2937;
  margin-bottom: 4px;
}

.stat-item .label {
  font-size: 12px;
  color: #6B7280;
}

.stat-divider {
  width: 1px;
  height: 30px;
  background: #E5E7EB;
}

.menu-section {
  padding: 0 20px;
}

.menu-card {
  padding: 0;
  overflow: hidden;
}

.menu-item {
  padding: 16px 20px;
  align-items: center;
  background: transparent;
}

.icon-box {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  font-size: 18px;
}

.icon-box.blue {
  background: #EFF6FF;
  color: #2563EB;
}

.icon-box.orange {
  background: #FFF7ED;
  color: #F97316;
}

.icon-box.green {
  background: #ECFDF5;
  color: #10B981;
}

.logout-wrapper {
  margin-top: 30px;
  padding: 0 20px;
}

.logout-btn {
  background: white;
  color: #EF4444;
  border: 1px solid #FEE2E2;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.05);
}
</style>