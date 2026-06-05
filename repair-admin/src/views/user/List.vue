<template>
  <div class="user-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户列表</span>
          <div class="header-actions">
            <el-input
              v-model="queryParams.username"
              placeholder="搜索手机号"
              style="width: 200px; margin-right: 10px;"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
            <el-button type="primary" @click="handleSearch">搜索</el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="账号/手机号" width="150" />
        <el-table-column prop="name" label="昵称" width="150" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="scope">
            <el-tag :type="getRoleType(scope.row.role)">{{ getRoleText(scope.row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
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
  username: ''
})

const fetchList = async () => {
  loading.value = true
  try {
    const res = await axios.get('/users/list', { params: queryParams })
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

const handleEdit = (row) => {
  // TODO: Implement edit
  console.log('Edit', row)
}

const getRoleType = (role) => {
  const map = {
    ADMIN: 'danger',
    WORKER: 'success',
    USER: 'primary'
  }
  return map[role] || 'info'
}

const getRoleText = (role) => {
  const map = {
    ADMIN: '管理员',
    WORKER: '维修师傅',
    USER: '普通用户'
  }
  return map[role] || role
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