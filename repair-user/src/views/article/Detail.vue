<template>
  <div class="page-container">
    <div class="header-section">
      <van-nav-bar
        title="文章详情"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
    </div>

    <div class="content-wrapper">
      <div v-if="article" class="glass-card article-card">
        <h1 class="title">{{ article.title }}</h1>
        <div class="meta">
          <van-tag type="primary" plain round class="category-tag">{{ article.category }}</van-tag>
          <span class="time">{{ formatTime(article.createTime) }}</span>
        </div>
        
        <van-image
          v-if="article.coverImg"
          width="100%"
          height="200"
          fit="cover"
          :src="article.coverImg"
          class="article-cover"
        />
        
        <div class="content">{{ article.content }}</div>
      </div>
      <van-loading v-else vertical class="loading-state">加载中...</van-loading>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const article = ref(null);

onMounted(async () => {
  const id = route.params.id;
  try {
    const res = await axios.get(`/api/articles/${id}`);
    article.value = res.data;
  } catch (err) {
    console.error(err);
  }
});

const formatTime = (time) => {
  return time ? time.replace('T', ' ').substring(0, 16) : '';
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

.article-card {
  padding: 24px;
}

.title {
  font-size: 22px;
  font-weight: 600;
  color: #1F2937;
  margin: 0 0 16px 0;
  line-height: 1.4;
}

.meta {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  font-size: 13px;
  color: #64748B;
}

.category-tag {
  margin-right: 12px;
}

.article-cover {
  border-radius: 12px;
  margin-bottom: 20px;
  overflow: hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.content {
  font-size: 16px;
  line-height: 1.8;
  color: #374151;
  white-space: pre-wrap;
  text-align: justify;
}

.loading-state {
  padding-top: 40px;
  color: #fff;
}
</style>
