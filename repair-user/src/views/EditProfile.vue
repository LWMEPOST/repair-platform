<template>
  <div class="edit-profile-page">
    <div class="header-section">
      <van-nav-bar
        title="修改信息"
        left-arrow
        @click-left="$router.back()"
        :border="false"
        class="custom-nav"
      />
    </div>

    <div class="content-wrapper">
      <div class="glass-card form-card">
        <van-form @submit="onSubmit">
          <div class="avatar-section">
             <van-uploader v-model="fileList" max-count="1" :after-read="afterRead" :show-upload="false">
                <div class="avatar-wrapper">
                  <van-image
                      round
                      width="100px"
                      height="100px"
                      :src="form.avatar || 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'"
                      class="user-avatar"
                  />
                  <div class="edit-badge">
                    <van-icon name="photograph" />
                  </div>
                </div>
             </van-uploader>
             <div class="avatar-tip">点击头像更换照片</div>
          </div>

          <div class="input-group">
            <div class="label">昵称</div>
            <van-field
              v-model="form.name"
              name="name"
              placeholder="请输入新昵称"
              :rules="[{ required: true, message: '请填写昵称' }]"
              class="custom-input"
            >
              <template #left-icon>
                <van-icon name="user-o" class="input-icon" />
              </template>
            </van-field>
          </div>

          <div class="input-group">
            <div class="label">手机号</div>
            <van-field
              v-model="form.username"
              name="username"
              placeholder="请输入新手机号"
              :rules="[{ required: true, message: '请填写手机号' }]"
              class="custom-input"
            >
              <template #left-icon>
                <van-icon name="phone-o" class="input-icon" />
              </template>
            </van-field>
          </div>

          <div class="submit-btn-wrapper">
            <van-button 
              round 
              block 
              type="primary" 
              native-type="submit"
              class="btn-gradient submit-btn"
              :loading="loading"
            >
              保存修改
            </van-button>
          </div>
        </van-form>
      </div>
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
const loading = ref(false);
const form = ref({
  name: '',
  username: '',
  avatar: ''
});
const fileList = ref([]);

onMounted(async () => {
  const userStr = localStorage.getItem('userInfo');
  if (userStr) {
    const localUser = JSON.parse(userStr);
    try {
        // 获取最新信息
        const res = await axios.get(`/api/users/${localUser.id}`);
        userInfo.value = res.data;
        form.value.name = userInfo.value.name;
        form.value.username = userInfo.value.username;
        form.value.avatar = userInfo.value.avatar;
    } catch (e) {
        userInfo.value = localUser;
        form.value.name = localUser.name;
        form.value.username = localUser.username;
    }
  }
});

const afterRead = async (file) => {
    file.status = 'uploading';
    file.message = '上传中...';

    const formData = new FormData();
    formData.append('file', file.file);

    try {
        const res = await axios.post('/api/common/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
        form.value.avatar = res.data;
        file.status = 'done';
        file.message = '上传成功';
    } catch (e) {
        file.status = 'failed';
        file.message = '上传失败';
        showFailToast('上传失败');
    }
};

const onSubmit = async () => {
  loading.value = true;
  try {
    await axios.put('/api/users', {
      id: userInfo.value.id,
      name: form.value.name,
      username: form.value.username,
      avatar: form.value.avatar
    });
    
    // 更新本地缓存 (部分更新)
    userInfo.value.name = form.value.name;
    userInfo.value.username = form.value.username;
    userInfo.value.avatar = form.value.avatar;
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value));
    
    showSuccessToast('修改成功');
    setTimeout(() => {
      router.back();
    }, 1000);
  } catch (error) {
    showFailToast('修改失败');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.edit-profile-page {
  min-height: 100vh;
  background-color: #f8fafc;
}

.header-section {
  background: var(--primary-gradient);
  padding-bottom: 80px;
  border-bottom-left-radius: 32px;
  border-bottom-right-radius: 32px;
}

:deep(.custom-nav) {
  background: transparent;
  --van-nav-bar-title-text-color: #fff;
  --van-nav-bar-icon-color: #fff;
}

.content-wrapper {
  padding: 0 16px;
  margin-top: -60px;
}

.form-card {
  padding: 32px 20px;
}

.avatar-section {
  text-align: center;
  margin-bottom: 32px;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
}

.user-avatar {
  border: 4px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.edit-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background: var(--primary-color);
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  border: 2px solid white;
}

.avatar-tip {
  font-size: 14px;
  color: #6b7280;
  margin-top: 12px;
}

.input-group {
  margin-bottom: 20px;
}

.input-group .label {
  font-size: 14px;
  color: #374151;
  font-weight: 500;
  margin-bottom: 8px;
  margin-left: 4px;
}

.custom-input {
  background: #F8FAFC;
  border-radius: 12px;
  padding: 12px 16px;
  border: 1px solid transparent;
  transition: all 0.3s;
}

.custom-input:focus-within {
  background: #fff;
  border-color: var(--primary-light);
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
}

.input-icon {
  font-size: 18px;
  color: #9CA3AF;
  margin-right: 8px;
}

.submit-btn-wrapper {
  margin-top: 40px;
}

.submit-btn {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  box-shadow: 0 10px 20px rgba(37, 99, 235, 0.2);
}
</style>
