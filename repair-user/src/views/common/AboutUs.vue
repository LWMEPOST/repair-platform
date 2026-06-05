<template>
  <div class="page-container">
    <div class="header-section">
      <van-nav-bar
        title="关于我们"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
    </div>

    <div class="content-wrapper">
      <div class="glass-card about-card">
        <div class="logo-area">
          <div class="logo-circle">
            <van-image
              width="60"
              height="60"
              src="https://fastly.jsdelivr.net/npm/@vant/assets/logo.png"
            />
          </div>
          <h3>上门维修平台</h3>
          <p class="version">Version 1.0.0</p>
        </div>
        
        <div class="text-area">
          <p>我们致力于为用户提供便捷、专业、透明的上门维修服务。</p>
          <p>平台汇聚了水电维修、家电清洗、管道疏通等各领域的专业师傅，所有师傅均经过严格的实名认证和技能考核，确保为您提供最优质的服务体验。</p>
        </div>
        
        <div class="footer-info">
          <p>© 2024 上门维修平台 版权所有</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
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

.about-card {
  padding: 40px 24px;
  text-align: center;
}

.logo-circle {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.logo-area h3 {
  font-size: 20px;
  font-weight: 600;
  color: #1F2937;
  margin: 0 0 8px;
}

.version {
  color: #94A3B8;
  font-size: 13px;
  margin: 0 0 32px;
  font-family: monospace;
}

.text-area {
  text-align: left;
  color: #4B5563;
  font-size: 15px;
  line-height: 1.8;
  margin-bottom: 40px;
}

.text-area p {
  margin-bottom: 16px;
}

.footer-info {
  color: #CBD5E1;
  font-size: 12px;
}
</style>
