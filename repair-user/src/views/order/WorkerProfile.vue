<template>
  <div class="worker-profile-page">
    <div class="header-section">
      <van-nav-bar
        title="师傅详情"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
      
      <div v-if="info" class="profile-content">
        <van-image
          round
          width="80"
          height="80"
          src="https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg"
          class="avatar"
        />
        <div class="name">{{ info.realName }}</div>
        <div class="tags">
          <van-tag type="success" v-if="info.authStatus === 'AUTHED'" plain round>已实名认证</van-tag>
          <div class="rating-tag">
             <van-icon name="star" color="#FF9500" />
             <span>{{ info.score }}</span>
          </div>
        </div>
        <div class="stats">
          <span>累计接单 {{ info.orderCount }}</span>
        </div>
      </div>
    </div>
    
    <div v-if="info" class="content-wrapper">
      <div class="glass-card section-card">
        <div class="card-title">技能证书</div>
        <div class="skills-list" v-if="info.skills && info.skills.length > 0">
           <div v-for="skill in info.skills" :key="skill.id" class="skill-item">
             <van-icon name="certificate" color="#2563EB" class="skill-icon" />
             <span>{{ skill.skillName }}</span>
           </div>
        </div>
        <van-empty v-else description="暂无展示技能" image-size="60" />
      </div>

      <div class="glass-card section-card">
        <div class="card-title">用户评价 ({{ info.comments ? info.comments.length : 0 }})</div>
        <div v-if="info.comments && info.comments.length > 0">
            <div v-for="(comment, index) in info.comments" :key="index" class="comment-item">
                <div class="comment-header">
                    <van-image
                        round
                        width="32"
                        height="32"
                        :src="comment.avatar || 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'"
                    />
                    <div class="user-meta">
                        <span class="username">{{ comment.userName }}</span>
                        <van-rate :model-value="comment.score" readonly size="10" color="#F59E0B" void-color="#E5E7EB" />
                    </div>
                    <div class="comment-time">{{ formatTime(comment.createTime) }}</div>
                </div>
                <div class="comment-content">{{ comment.content }}</div>
            </div>
        </div>
        <van-empty v-else description="暂无评价" image-size="60" />
      </div>
    </div>
    <div v-else class="loading-box">
      <van-loading type="spinner" color="#2563EB" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const info = ref(null);

onMounted(async () => {
  const workerId = route.params.id;
  try {
    const res = await axios.get(`/api/worker/public/${workerId}`);
    info.value = res.data;
  } catch (e) {
    console.error(e);
  }
});

const formatTime = (time) => {
  return time ? time.replace('T', ' ').substring(0, 10) : '';
};
</script>

<style scoped>
.worker-profile-page {
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

.profile-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #fff;
  padding-top: 10px;
}

.avatar {
  border: 4px solid rgba(255, 255, 255, 0.3);
  margin-bottom: 12px;
}

.name {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 8px;
}

.tags {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.rating-tag {
  display: flex;
  align-items: center;
  gap: 4px;
  background: rgba(255, 255, 255, 0.2);
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  backdrop-filter: blur(4px);
}

.stats {
  font-size: 14px;
  opacity: 0.9;
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
  font-size: 16px;
  font-weight: 600;
  color: #1F2937;
  margin-bottom: 16px;
  padding-left: 8px;
  border-left: 4px solid var(--primary-color);
}

.skill-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  background: #F3F4F6;
  border-radius: 8px;
  margin-bottom: 8px;
  font-size: 14px;
  color: #374151;
}

.comment-item {
  padding: 16px 0;
  border-bottom: 1px solid #F3F4F6;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.user-meta {
  flex: 1;
  margin-left: 12px;
  display: flex;
  flex-direction: column;
}

.username {
  font-size: 14px;
  color: #1F2937;
  font-weight: 500;
  margin-bottom: 2px;
}

.comment-time {
  font-size: 12px;
  color: #9CA3AF;
}

.comment-content {
  font-size: 14px;
  color: #4B5563;
  line-height: 1.5;
  padding-left: 44px;
}

.loading-box {
  text-align: center;
  padding-top: 100px;
}
</style>
