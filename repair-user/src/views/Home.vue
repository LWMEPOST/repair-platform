<template>
  <div class="home-page page-container">
    <!-- 顶部欢迎区 -->
    <div class="header-section">
      <h1 class="welcome-title">专业的<br>上门维修服务</h1>
      <p class="welcome-subtitle">极速响应 · 价格透明 · 售后无忧</p>
    </div>

    <!-- 轮播图 -->
    <div class="banner-wrapper">
      <van-swipe class="my-swipe" :autoplay="3000" indicator-color="white">
        <van-swipe-item v-for="(img, index) in banners" :key="index">
          <div class="banner-item" :style="{ backgroundImage: `url(${img})` }"></div>
        </van-swipe-item>
      </van-swipe>
    </div>

    <!-- 快捷入口卡片 -->
    <div class="action-cards">
      <div class="action-item primary" @click="router.push('/create-order')">
        <div class="icon-box">
          <van-icon name="plus" />
        </div>
        <div class="text-box">
          <h3>立即预约</h3>
          <p>快速发布维修需求</p>
        </div>
        <van-icon name="arrow" class="arrow-icon" />
      </div>

      <div class="action-item secondary" @click="router.push('/orders')">
        <div class="icon-box">
          <van-icon name="orders-o" />
        </div>
        <div class="text-box">
          <h3>我的订单</h3>
          <p>查看服务进度</p>
        </div>
        <van-icon name="arrow" class="arrow-icon" />
      </div>
    </div>

    <!-- 资讯推荐 -->
    <div class="section-title">
      <h3>维修指南</h3>
      <span class="more" @click="activeTab = ''">查看更多</span>
    </div>

    <van-tabs v-model:active="activeTab" background="transparent" line-width="20px" @click-tab="onTabClick" class="custom-tabs">
      <van-tab title="全部" name=""></van-tab>
      <van-tab title="维修指南" name="维修指南"></van-tab>
      <van-tab title="家电科普" name="家电科普"></van-tab>
    </van-tabs>

    <van-list
      v-model:loading="loading"
      :finished="finished"
      finished-text="没有更多了"
      @load="onLoad"
      class="article-list"
    >
      <div v-for="item in list" :key="item.id" class="glass-card article-card" @click="goDetail(item.id)">
        <div class="article-content">
          <div class="article-main">
            <h4 class="article-title">{{ item.title }}</h4>
            <div class="article-tags">
              <span class="tag">{{ item.category }}</span>
              <span class="time">{{ formatTime(item.createTime) }}</span>
            </div>
          </div>
          <van-image
            v-if="item.coverImg"
            class="article-img"
            :src="item.coverImg"
            fit="cover"
          />
        </div>
      </div>
    </van-list>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const banners = ref([
  'https://images.unsplash.com/photo-1581092921461-eab62e97a783?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80',
  'https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80'
]);

const list = ref([]);
const loading = ref(false);
const finished = ref(false);
const activeTab = ref('');
const page = ref(1);

const loadBanners = async () => {
  try {
    const res = await axios.get('/api/articles', {
      params: { page: 1, size: 5 } // 多取几个防止没有封面
    });
    const records = res.data.records || [];
    const images = records
      .filter(item => item.coverImg)
      .map(item => item.coverImg)
      .slice(0, 3);
      
    if (images.length > 0) {
      banners.value = images;
    }
  } catch (e) {
    console.error('Failed to load banner images', e);
  }
};

const onLoad = async () => {
  try {
    const res = await axios.get('/api/articles', {
      params: {
        page: page.value,
        size: 10,
        category: activeTab.value
      }
    });
    
    const records = res.data.records || [];
    if (page.value === 1) {
      list.value = records;
      // 只有在第一页且没有特定分类时加载 Banner
      if (!activeTab.value) {
        loadBanners();
      }
    } else {
      list.value.push(...records);
    }

    if (records.length < 10) {
      finished.value = true;
    } else {
      page.value++;
    }
    
    loading.value = false;
  } catch (err) {
    loading.value = false;
    finished.value = true;
  }
};

const onTabClick = () => {
  page.value = 1;
  list.value = [];
  finished.value = false;
  loading.value = true;
  onLoad();
};

const goDetail = (id) => {
  router.push(`/article/${id}`);
};

const formatTime = (time) => {
  return time ? time.split('T')[0] : '';
};
</script>

<style scoped>
.home-page {
  background: var(--bg-gradient);
}

.header-section {
  padding: 20px 10px;
  margin-bottom: 10px;
}

.welcome-title {
  font-size: 28px;
  color: var(--primary-color);
  line-height: 1.2;
  margin-bottom: 8px;
}

.welcome-subtitle {
  color: #64748B;
  font-size: 14px;
}

.banner-wrapper {
  border-radius: var(--card-radius);
  overflow: hidden;
  box-shadow: var(--glass-shadow);
  margin-bottom: 24px;
}

.banner-item {
  width: 100%;
  height: 180px;
  background-size: cover;
  background-position: center;
}

.action-cards {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 30px;
}

.action-item {
  display: flex;
  align-items: center;
  padding: 24px;
  cursor: pointer;
  transition: transform 0.2s;
  position: relative;
  overflow: hidden;
}

.action-item:active {
  transform: scale(0.98);
}

.action-item.primary {
  background: var(--primary-gradient);
  border: none;
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.2);
}

.action-item.secondary {
  background: linear-gradient(135deg, #F97316 0%, #FB923C 100%);
  border: none;
  box-shadow: 0 10px 20px rgba(249, 115, 22, 0.2);
}

.icon-box {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
}

.primary .icon-box {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  box-shadow: none;
}

.secondary .icon-box {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  box-shadow: none;
}

.action-item.primary .text-box h3,
.action-item.secondary .text-box h3 {
  color: white;
}

.action-item.primary .text-box p,
.action-item.secondary .text-box p {
  color: rgba(255, 255, 255, 0.9);
}

.action-item.primary .arrow-icon,
.action-item.secondary .arrow-icon {
  color: rgba(255, 255, 255, 0.8);
}

.text-box h3 {
  font-size: 18px;
  margin-bottom: 4px;
  color: #1E293B;
}

.text-box p {
  margin: 0;
  font-size: 13px;
  color: #64748B;
}

.arrow-icon {
  margin-left: auto;
  color: #CBD5E1;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 0 4px;
}

.section-title h3 {
  font-size: 18px;
  color: #1E293B;
}

.more {
  font-size: 13px;
  color: var(--primary-color);
}

.custom-tabs {
  margin-bottom: 16px;
}

:deep(.van-tab) {
  font-weight: normal;
  color: #64748B;
}

:deep(.van-tab--active) {
  font-weight: 600;
  color: #1E293B;
}

.article-card {
  padding: 16px;
  border: none;
  background: rgba(255, 255, 255, 0.6);
}

.article-content {
  display: flex;
  justify-content: space-between;
}

.article-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-right: 12px;
}

.article-title {
  font-size: 15px;
  color: #1E293B;
  line-height: 1.5;
  margin-bottom: 8px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-tags {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #94A3B8;
}

.tag {
  background: rgba(37, 99, 235, 0.1);
  color: var(--primary-color);
  padding: 2px 6px;
  border-radius: 4px;
  margin-right: 8px;
}

.article-img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
}
</style>
