<template>
  <div class="task-list">
    <van-nav-bar title="我的任务" />
    <van-list>
      <van-cell-group v-for="order in orders" :key="order.id" inset style="margin-top: 10px;">
        <van-cell :title="order.serviceContent" :value="statusMap[order.status]" :label="order.address" />
        <van-cell title="联系电话" :value="order.contactPhone" />
        
        <div class="action-bar" style="text-align: right; padding: 10px;">
          <van-button v-if="order.status === 'SERVICE_ING'" size="small" type="primary" @click="finishOrder(order.id)">确认完工</van-button>
          <span v-if="order.status === 'FINISHED'" style="color: #999; font-size: 12px;">等待用户评价</span>
        </div>
      </van-cell-group>
      <div v-if="orders.length === 0" style="text-align: center; padding: 20px; color: #999;">
        暂无任务
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

const statusMap = {
    'SERVICE_ING': '服务中',
    'FINISHED': '已完工',
    'RATED': '已评价'
};

const fetchOrders = async () => {
    try {
        // 模拟当前师傅ID = 3
        const res = await axios.get('/api/orders/worker/3');
        orders.value = res.data;
    } catch (err) {
        showToast('获取任务失败');
    }
};

const finishOrder = async (orderId) => {
    try {
        const res = await axios.post(`/api/orders/${orderId}/finish`);
        if (res.data === '完工确认成功') {
            showToast('操作成功');
            fetchOrders();
        } else {
            showToast(res.data);
        }
    } catch (err) {
        showToast('请求失败');
    }
};

onMounted(fetchOrders);
</script>
