<template>
  <div class="auth-page">
    <van-nav-bar
      title="实名认证"
      left-arrow
      @click-left="$router.back()"
    />
    
    <div v-if="authStatus === 'AUTHED'" class="status-box success">
      <van-icon name="checked" size="64" color="#07c160" />
      <p>已通过认证</p>
    </div>
    
    <div v-else-if="authStatus === 'AUDITING'" class="status-box warning">
      <van-icon name="clock" size="64" color="#ff976a" />
      <p>审核中，请耐心等待</p>
    </div>

    <div v-else class="form-container">
      <van-form @submit="onSubmit">
        <van-cell-group inset>
          <van-field
            v-model="form.realName"
            name="realName"
            label="真实姓名"
            placeholder="请输入真实姓名"
            :rules="[{ required: true, message: '请填写姓名' }]"
          />
          <van-field
            v-model="form.idCard"
            name="idCard"
            label="身份证号"
            placeholder="请输入身份证号"
            :rules="[{ required: true, message: '请填写身份证号' }]"
          />
          
          <div style="padding: 16px;">
            <div class="upload-label">上传身份证正面</div>
            <van-uploader v-model="fileListFront" max-count="1" />
            <div class="upload-label">上传身份证背面</div>
            <van-uploader v-model="fileListBack" max-count="1" />
          </div>
        </van-cell-group>
        
        <div style="margin: 16px;">
          <van-button round block type="primary" native-type="submit">
            提交认证
          </van-button>
        </div>
      </van-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { showSuccessToast, showFailToast } from 'vant';

const router = useRouter();
const userInfo = ref({});
const authStatus = ref('UNAUTH');
const form = ref({
  realName: '',
  idCard: ''
});
const fileListFront = ref([]);
const fileListBack = ref([]);

onMounted(async () => {
  const userStr = localStorage.getItem('userInfo');
  if (userStr) {
    userInfo.value = JSON.parse(userStr);
    try {
      const res = await axios.get(`/api/worker/info/${userInfo.value.id}`);
      if (res.data) {
        authStatus.value = res.data.authStatus || 'UNAUTH';
        form.value.realName = res.data.realName;
        form.value.idCard = res.data.idCard;
        // 模拟回显图片
      }
    } catch (e) {
      console.error(e);
    }
  }
});

const onSubmit = async () => {
  try {
    const payload = {
      userId: userInfo.value.id,
      realName: form.value.realName,
      idCard: form.value.idCard,
      idCardFront: 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg', // 模拟上传后的URL
      idCardBack: 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'
    };
    
    await axios.post('/api/worker/auth', payload);
    showSuccessToast('提交成功');
    authStatus.value = 'AUDITING';
  } catch (error) {
    showFailToast('提交失败');
  }
};
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background-color: #f7f8fa;
}
.status-box {
  text-align: center;
  padding-top: 50px;
}
.status-box p {
  margin-top: 20px;
  font-size: 18px;
  color: #666;
}
.form-container {
  padding-top: 20px;
}
.upload-label {
  font-size: 14px;
  color: #646566;
  margin-bottom: 10px;
  margin-top: 10px;
}
</style>
