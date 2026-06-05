<template>
  <router-view />
  <van-tabbar route fixed placeholder v-if="showTabbar" class="glass-tabbar">
    <van-tabbar-item replace to="/" icon="home-o">抢单大厅</van-tabbar-item>
    <van-tabbar-item replace to="/orders" icon="orders-o">我的任务</van-tabbar-item>
    <van-tabbar-item replace to="/profile" icon="user-o">个人中心</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const showTabbar = computed(() => {
  return ['/', '/profile', '/orders'].includes(route.path);
});
</script>

<style scoped>
.glass-tabbar {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-top: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.05);
}

:deep(.van-tabbar-item--active) {
  background: transparent;
  font-weight: 600;
}
</style>
