<template>
  <div class="earnings-page">
    <van-nav-bar
      title="我的收益"
      left-arrow
      @click-left="$router.back()"
    />
    
    <div class="header-card">
      <div class="label">总收益 (元)</div>
      <div class="amount">{{ formatMoney(totalEarnings) }}</div>
    </div>

    <van-cell-group title="收益明细">
      <van-list
        v-model:loading="loading"
        :finished="finished"
        finished-text="没有更多了"
      >
        <van-cell 
            v-for="item in list" 
            :key="item.id" 
            :title="item.serviceContent" 
            :value="`+${formatMoney(item.price)}`" 
            :label="formatTime(item.finishTime)"
            value-class="income-value"
        />
      </van-list>
    </van-cell-group>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { showFailToast } from 'vant';

const totalEarnings = ref('0.00');
const list = ref([]);
const loading = ref(false);
const finished = ref(true); 

onMounted(async () => {
    loadData();
});

const loadData = async () => {
    loading.value = true;
    try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'));
        if (!userInfo) return;
        
        const res = await axios.get(`/api/worker/earnings/${userInfo.id}`);
        totalEarnings.value = res.data.total;
        list.value = res.data.records;
    } catch (e) {
        console.error(e);
        showFailToast('加载失败');
    } finally {
        loading.value = false;
        finished.value = true;
    }
};

const formatTime = (time) => {
    return time ? time.replace('T', ' ').substring(0, 16) : '';
};

const formatMoney = (val) => {
    return Number(val || 0).toFixed(2);
};
</script>

<style scoped>
.earnings-page {
    min-height: 100vh;
    background-color: #f7f8fa;
}
.header-card {
    background: #1989fa;
    color: #fff;
    padding: 40px 20px;
    text-align: center;
    margin-bottom: 12px;
}
.label {
    font-size: 14px;
    opacity: 0.9;
    margin-bottom: 10px;
}
.amount {
    font-size: 40px;
    font-weight: bold;
}
:deep(.income-value) {
    color: #ee0a24;
    font-weight: bold;
}
</style>
