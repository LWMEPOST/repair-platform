<template>
  <div class="order-pool">
    <van-nav-bar title="抢单大厅" />
    <van-list>
      <van-cell v-for="order in orders" :key="order.id" :title="order.serviceContent" :label="order.address" :value="'￥' + order.price">
        <template #right-icon>
          <van-button size="small" type="primary" @click="grabOrder(order.id)">抢单</van-button>
        </template>
      </van-cell>
      <div v-if="orders.length === 0" style="text-align: center; padding: 20px; color: #999;">
        暂无待抢订单
      </div>
    </van-list>
    <div style="padding: 16px;">
        <van-button block type="default" @click="fetchOrders">刷新列表</van-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { showToast } from 'vant';

const orders = ref([]);

const fetchOrders = async () => {
  try {
      const res = await axios.get('/api/orders/waiting');
      orders.value = res.data;
  } catch (err) {
      console.error(err);
      showToast('获取订单列表失败');
  }
};

const grabOrder = async (orderId) => {
  try {
    // 假设当前师傅ID为 3 (对应数据库初始数据)
    const workerId = 3;
    const res = await axios.post(`/api/orders/${orderId}/grab`, null, {
        params: { workerId }
    });
    
    if (res.data === '抢单成功') {
        showToast('抢单成功！');
        // 刷新列表
        fetchOrders();
    } else {
        showToast(res.data);
    }
  } catch (err) {
    console.error(err);
    showToast('抢单请求失败');
  }
};

onMounted(fetchOrders);
</script>
