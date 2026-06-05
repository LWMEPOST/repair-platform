<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>管理后台登录</span>
        </div>
      </template>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: center; color: #999; font-size: 12px;">
        测试账号: admin / 123456
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = reactive({
  username: 'admin',
  password: '123456'
})

const onSubmit = async () => {
  try {
    const res = await axios.post('/auth/login', {
      username: form.username,
      password: form.password,
      role: 'ADMIN'
    })
    
    if (res.data && res.data.token) {
      ElMessage.success('登录成功')
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userInfo', JSON.stringify(res.data))
      axios.defaults.headers.common['Authorization'] = res.data.token
      router.push('/')
    } else {
      ElMessage.error('登录失败')
    }
  } catch (err) {
    ElMessage.error('登录失败: ' + (err.response?.data?.message || err.message))
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.login-card {
  width: 400px;
}
.card-header {
  text-align: center;
  font-weight: bold;
}
</style>
