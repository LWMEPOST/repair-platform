<template>
  <router-view />
  <!-- 只有在主页或个人中心才显示底部导航 -->
  <van-tabbar route fixed placeholder v-if="showTabbar" class="glass-tabbar" :border="false">
    <van-tabbar-item replace to="/" icon="wap-home-o">首页</van-tabbar-item>
    <van-tabbar-item replace to="/create-order" icon="add-o">发布</van-tabbar-item>
    <van-tabbar-item replace to="/orders" icon="orders-o">订单</van-tabbar-item>
    <van-tabbar-item replace to="/profile" icon="user-o">我的</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const showTabbar = computed(() => {
  return ['/', '/profile', '/orders', '/create-order'].includes(route.path);
});
</script>

<style scoped>
.glass-tabbar {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-top: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.05);
}
</style>
