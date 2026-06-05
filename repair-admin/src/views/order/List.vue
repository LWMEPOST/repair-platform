<template>
  <div class="order-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <div class="header-actions">
            <el-input
              v-model="queryParams.orderNo"
              placeholder="订单号"
              style="width: 200px; margin-right: 10px;"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
            <el-select v-model="queryParams.status" placeholder="订单状态" style="width: 150px; margin-right: 10px;" clearable @change="handleSearch">
              <el-option label="已创建" value="CREATED" />
              <el-option label="已支付" value="PAID" />
              <el-option label="待抢单" value="WAITING_GRAB" />
              <el-option label="待确认" value="WAITING_USER_CONFIRM" />
              <el-option label="服务中" value="SERVICE_ING" />
              <el-option label="已完工" value="FINISHED" />
              <el-option label="已评价" value="RATED" />
            </el-select>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="serviceContent" label="服务内容" show-overflow-tooltip />
        <el-table-column prop="price" label="金额" width="100">
          <template #default="scope">
            ¥ {{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column prop="address" label="地址" show-overflow-tooltip />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="primary" link @click="handleDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryParams = reactive({
  page: 1,
  size: 10,
  orderNo: '',
  status: ''
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await axios.get('/orders/list', { params: queryParams })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryParams.page = 1
  fetchList()
}

const handleSizeChange = (val) => {
  queryParams.size = val
  fetchList()
}

const handleCurrentChange = (val) => {
  queryParams.page = val
  fetchList()
}

const handleDetail = (row) => {
  // TODO: Implement detail view
  console.log('Detail', row)
}

const getStatusType = (status) => {
  const map = {
    CREATED: 'info',
    PAID: 'primary',
    WAITING_GRAB: 'warning',
    WAITING_USER_CONFIRM: 'warning',
    SERVICE_ING: 'danger',
    FINISHED: 'success',
    RATED: 'success'
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
  fetchList()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>