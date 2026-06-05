<template>
  <div class="page-container">
    <div class="header-section">
      <van-nav-bar
        title="联系客服"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
    </div>

    <div class="content-wrapper">
      <div class="glass-card service-card">
        <div class="service-header">
           <div class="icon-wrapper">
             <van-icon name="service" size="32" color="#2563EB" />
           </div>
           <h3>客服中心</h3>
           <p>在线时间: 9:00 - 21:00</p>
        </div>

        <div class="action-list">
          <div class="action-item" @click="call">
            <div class="item-icon phone">
              <van-icon name="phone" />
            </div>
            <div class="item-info">
              <span class="label">客服电话</span>
              <span class="value">400-888-8888</span>
            </div>
            <van-icon name="arrow" class="arrow" />
          </div>

          <div class="action-item">
            <div class="item-icon chat">
              <van-icon name="chat" />
            </div>
            <div class="item-info">
              <span class="label">在线客服</span>
              <span class="value">点击咨询</span>
            </div>
            <van-icon name="arrow" class="arrow" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const call = () => {
  window.location.href = 'tel:4008888888';
};
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: var(--page-bg);
  padding-bottom: 20px;
}

.header-section {
  height: 160px;
  background: var(--primary-gradient);
  border-bottom-left-radius: 24px;
  border-bottom-right-radius: 24px;
  position: relative;
  z-index: 1;
}

.custom-nav {
  background: transparent;
  --van-nav-bar-title-text-color: #fff;
  --van-nav-bar-icon-color: #fff;
}

.content-wrapper {
  padding: 0 16px;
  margin-top: -80px;
  position: relative;
  z-index: 2;
}

.glass-card {
  background: var(--glass-bg);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.07);
  border-radius: 16px;
  overflow: hidden;
}

.service-card {
  padding: 32px 20px;
}

.service-header {
  text-align: center;
  margin-bottom: 32px;
}

.icon-wrapper {
  width: 64px;
  height: 64px;
  background: rgba(37, 99, 235, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.service-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1F2937;
  margin: 0 0 8px;
}

.service-header p {
  color: #64748B;
  font-size: 13px;
}

.action-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.action-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  transition: all 0.3s;
}

.action-item:active {
  background: rgba(255, 255, 255, 0.8);
  transform: scale(0.98);
}

.item-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-right: 16px;
}

.item-icon.phone {
  background: #EFF6FF;
  color: #2563EB;
}

.item-icon.chat {
  background: #FFF7ED;
  color: #F97316;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.label {
  font-size: 15px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 4px;
}

.value {
  font-size: 13px;
  color: #94A3B8;
}

.arrow {
  color: #CBD5E1;
}
</style>
