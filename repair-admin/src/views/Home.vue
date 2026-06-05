<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>总用户数</span>
              <el-tag type="success">日</el-tag>
            </div>
          </template>
          <div class="card-body">
            <div class="stat-value">{{ stats.userCount?.toLocaleString() }}</div>
            <div class="stat-desc">
              <span>日同比 {{ stats.userGrowth }} <el-icon color="red"><CaretTop /></el-icon></span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>总订单数</span>
              <el-tag type="warning">周</el-tag>
            </div>
          </template>
          <div class="card-body">
            <div class="stat-value">{{ stats.orderCount?.toLocaleString() }}</div>
            <div class="stat-desc">
              <span>周同比 {{ stats.orderGrowth }} <el-icon color="green"><CaretBottom /></el-icon></span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>总交易额</span>
              <el-tag type="danger">月</el-tag>
            </div>
          </template>
          <div class="card-body">
            <div class="stat-value">¥ {{ stats.totalAmount?.toLocaleString() }}</div>
            <div class="stat-desc">
              <span>月同比 {{ stats.amountGrowth }} <el-icon color="red"><CaretTop /></el-icon></span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <template #header>
            <div class="card-header">
              <span>待处理工单</span>
              <el-tag>实时</el-tag>
            </div>
          </template>
          <div class="card-body">
            <div class="stat-value">{{ stats.pendingOrders }}</div>
            <div class="stat-desc">
              <span>需尽快处理</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近订单</span>
              <el-button text>查看更多</el-button>
            </div>
          </template>
          <el-table :data="recentOrders" style="width: 100%">
            <el-table-column prop="orderNo" label="订单号" width="180" />
            <el-table-column prop="serviceContent" label="服务内容" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统公告</span>
            </div>
          </template>
          <div v-if="notices.length > 0">
            <div v-for="item in notices" :key="item.id" class="notice-item">
              <span class="notice-title">{{ item.title }}</span>
              <span class="notice-time">{{ formatTime(item.createTime) }}</span>
            </div>
          </div>
          <div v-else class="empty-text">暂无公告</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const recentOrders = ref([])
const stats = ref({})
const notices = ref([])

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleDateString()
}

const fetchStats = async () => {
  try {
    const res = await axios.get('/dashboard/stats')
    stats.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const fetchNotices = async () => {
  try {
    const res = await axios.get('/dashboard/notices')
    notices.value = res.data
  } catch (err) {
    console.error(err)
  }
}

const fetchOrders = async () => {
  try {
    const res = await axios.get('/orders/list', {
      params: { page: 1, size: 5 }
    })
    recentOrders.value = res.data.records
  } catch (err) {
    console.error(err)
  }
}

const getStatusType = (status) => {
  const map = {
    CREATED: 'info',
    PAID: 'primary',
    WAITING_GRAB: 'warning',
    SERVICE_ING: 'danger',
    FINISHED: 'success'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    CREATED: '已创建',
    PAID: '已支付',
    WAITING_GRAB: '待抢单',
    WAITING_USER_CONFIRM: '待确认',
    SERVICE_ING: '服务中',
    FINISHED: '已完工',
    RATED: '已评价'
  }
  return map[status] || status
}

onMounted(() => {
  fetchStats()
  fetchNotices()
  fetchOrders()
})
</script>

<style scoped>
.stat-card {
  height: 180px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin: 10px 0;
}
.stat-desc {
  color: #909399;
  font-size: 14px;
}
.notice-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #EBEEF5;
}
</style>